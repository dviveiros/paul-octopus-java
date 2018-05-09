package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.context.ContextBuilder;
import com.ciandt.paul.dao.DataNotAvailableException;
import com.ciandt.paul.dao.MatchDAO;
import com.ciandt.paul.entity.HistoricalMatch;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;
import com.ciandt.paul.utils.GCSUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles feed requests and processing
 */
@Service
public class PredictionService {

    private static Logger logger = LoggerFactory.getLogger(PredictionService.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private MatchDAO matchDAO;
    @Autowired
    private ContextBuilder contextBuilder;
    @Autowired
    private PredictorFactory predictorFactory;
    @Autowired
    private GCSUtils gcsUtils;

    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000 %");

    /**
     * Start the prediction process
     * @param generateFile If this method should generate the predictions.csv file or not
     * @param strPredictor Name of the Predictor class to be used
     */
    public void predict(Boolean generateFile, String strPredictor)
            throws InterruptedException, DataNotAvailableException, IOException {

        //Year to be predicted
        Integer worldCupYear = config.getWorldCupYear();
        Integer[] trainingYears = config.getTrainingYears();
        Integer[] trainingScores = new Integer[trainingYears.length];
        Double[] trainingPerformance = new Double[trainingYears.length];
        Double totalPerformance = 0d;

        Predictor predictor = predictorFactory.createsPredictor(strPredictor);

        //Predictions for current world cup
        logger.info("Predicting results for year: " + worldCupYear);
        List<Prediction> predictionList2018 = this.predict(predictor, worldCupYear);

        //Predictions for the past (training data)
        List<Prediction> predictionsForTraining = null;
        for (int i = 0; i < trainingYears.length; i++) {
            logger.info("Predicting results for year: " + trainingYears[i]);
            predictionsForTraining = this.predict(predictor, trainingYears[i]);
            trainingScores[i] = this.calculatePerformance(predictionsForTraining, trainingYears[i]);
            trainingPerformance[i] = new Double(trainingScores[i]) / config.getMaxScorePerWorldCup();
            totalPerformance += trainingPerformance[i];
        }

        //Generate file?
        if (generateFile) {
            String csvContent = this.generateCSVContent(predictionList2018);
            FileWriter fileWriter = new FileWriter(config.getPredictionsFilename());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(csvContent);
            bufferedWriter.flush();
            bufferedWriter.close();
        }

        // Final output
        logger.info("**********************************************");
        logger.info("* Algorithm performance");
        for (int i = 0; i < trainingYears.length; i++) {
            logger.info("* " + trainingYears[i] + ": Score = " + trainingScores[i]
                    + ", Performance = " + decimalFormat.format(trainingPerformance[i]));
        }
        logger.info("* ");
        logger.info("* Overall performance = " + decimalFormat.format(totalPerformance / trainingYears.length));
        logger.info("**********************************************");
    }

    /**
     * Upload the prediction to GCS
     */
    public void uploadPredictions(String username) throws IOException {

        File file = new File(config.getPredictionsFilename());
        if (!file.exists()) {
            throw new FileNotFoundException("Prediction file not found: " + config.getPredictionsFilename());
        }

        //read the content
        String fileContent = FileUtils.readFileToString(file, "UTF-8");
        logger.info("Uploading file " + config.getPredictionsFilename() + " to bucket " +
                config.getPredictionsBucketPrefix() + username);
        gcsUtils.writeFile(config.getPredictionsBucketPrefix() + username,
                config.getPredictionsFilename(), fileContent);
        logger.info("Upload completed!");
    }

    /**
     * Predict the results for a world cup
     */
    List<Prediction> predict(Predictor predictor, Integer year)
            throws InterruptedException, DataNotAvailableException, IOException {
        List<Prediction> predictions = new ArrayList<>();

        List<Match> matchList = null;
        matchList = matchDAO.fetch(year);

        Prediction prediction = null;
        for (Match match : matchList) {
            Context context = contextBuilder.build(match, year);
            prediction = predictor.predict(match, context);
            if (config.isDebugEnabled()) {
                logger.debug("New prediction for World Cup " + year + ": " + prediction);
            }
            predictions.add(prediction);
        }

        return predictions;
    }

    /**
     * Generates the prediction file to be uploaded to GCS
     */
    String generateCSVContent(List<Prediction> predictions) throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT
                .withHeader("home", "home_score", "away_score", "away"));
        for (Prediction prediction : predictions) {
            csvPrinter.printRecord(prediction.getMatch().getHomeTeam(),
                    prediction.getHomeScore(),
                    prediction.getAwayScore(),
                    prediction.getMatch().getAwayTeam());
        }

        csvPrinter.flush();

        String csvContent = stringWriter.toString();

        if (config.isDebugEnabled()) {
            logger.debug("CSV content generated:");
            logger.debug(csvContent);
        }

        return csvContent;
    }

    /**
     * Calculates the performance for a given year
     */
    Integer calculatePerformance(List<Prediction> predictions, Integer year) throws InterruptedException, DataNotAvailableException, IOException {
        List<HistoricalMatch> actualResults = matchDAO.fetchResults(year);
        Integer total = 0;
        Integer score = 0;
        for (Prediction prediction : predictions) {
            score = this.calculateScore(prediction, actualResults);
            total += score;
        }
        return total;
    }

    /**
     * Calculates the score for a prediction according to the rules of the competition
     */
    Integer calculateScore(Prediction prediction, List<HistoricalMatch> actualResults)
            throws InterruptedException, DataNotAvailableException, IOException {

        //find the appropriate match
        Match match = prediction.getMatch();
        HistoricalMatch actualResult = null;
        for (HistoricalMatch currentMatch : actualResults) {
            if (match.getHomeTeam().equals(currentMatch.getHomeTeam()) &&
                    match.getAwayTeam().equals(currentMatch.getAwayTeam())) {
                actualResult = currentMatch;
                break;
            }
        }

        if (actualResult == null) {
            throw new RuntimeException("Result not found for prediction " + prediction);
        }

        Integer diffGoals = prediction.getHomeScore() - prediction.getAwayScore();
        boolean isDraw = diffGoals == 0;
        boolean homeWins = diffGoals > 0;
        boolean awayWins = diffGoals < 0;

        //exact score (valid for both draw and non-draw)
        if (prediction.getHomeScore().equals(actualResult.getHomeScore()) &&
                prediction.getAwayScore().equals(actualResult.getAwayScore())) {
            return 25;
        }

        //Draw
        if (isDraw) {
            if (actualResult.getAwayScore() - actualResult.getHomeScore() == 0) { //it's a draw with different score
                this.logScore(prediction, actualResult, 15);
                return 15;
            } else {
                this.logScore(prediction, actualResult, 4);
                return 4; // you get points anyway
            }
        }

        //home wins
        if (homeWins) {
            if (actualResult.getHomeScore() - actualResult.getAwayScore() <= 0) { //wrong prediction
                this.logScore(prediction, actualResult, 0);
                return 0;
            } else if (prediction.getHomeScore().equals(actualResult.getHomeScore())) {
                this.logScore(prediction, actualResult, 18);
                return 18;
            } else if ((actualResult.getHomeScore() - actualResult.getAwayScore()) ==
                    (prediction.getHomeScore() - prediction.getAwayScore())) {
                this.logScore(prediction, actualResult, 15);
                return 15;
            } else if (prediction.getAwayScore().equals(actualResult.getAwayScore())) {
                this.logScore(prediction, actualResult, 12);
                return 12;
            } else {
                this.logScore(prediction, actualResult, 10);
                return 10;
            }
        }

        //away wins
        if (awayWins) {
            if (actualResult.getHomeScore() - actualResult.getAwayScore() >= 0) { //wrong prediction
                this.logScore(prediction, actualResult, 0);
                return 0;
            } else if (prediction.getAwayScore().equals(actualResult.getAwayScore())) {
                this.logScore(prediction, actualResult, 18);
                return 18;
            } else if ((actualResult.getHomeScore() - actualResult.getAwayScore()) ==
                    (prediction.getHomeScore() - prediction.getAwayScore())) {
                this.logScore(prediction, actualResult, 15);
                return 15;
            } else if (prediction.getHomeScore().equals(actualResult.getHomeScore())) {
                this.logScore(prediction, actualResult, 12);
                return 12;
            } else {
                this.logScore(prediction, actualResult, 10);
                return 10;
            }
        }

        this.logScore(prediction, actualResult, 0);
        return 0;
    }

    private void logScore(Prediction prediction, HistoricalMatch match, Integer score) {
        if (config.isDebugEnabled()) {
            logger.debug("Score for match [" + prediction.getMatch().getHomeTeam() + " x " + prediction.getMatch().getAwayTeam() +
                    "] = " + score + " (prediction: " + prediction.getHomeScore() + " x " + prediction.getAwayScore() +
                    " // actual = " + match.getHomeScore() + " x " + match.getAwayScore() + ")");
        }
    }

}
