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
     */
    public void predict(String strPredictor)
            throws Exception {

        //Year to be predicted
        Integer worldCupYear = config.getWorldCupYear();
        Integer[] trainingYears = config.getTrainingYears();

        Predictor predictor = predictorFactory.createsPredictor(strPredictor);

        List<String> prefixes = readFilePrefixes();
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT
                .withHeader("Prefix", "2006", "2010", "2014", "Overall").withIgnoreEmptyLines().withRecordSeparator("\r\n"));

        for (String filePrefix : prefixes) {
            Double totalPerformance = 0d;
            Integer[] trainingScores = new Integer[trainingYears.length];
            Double[] trainingPerformance = new Double[trainingYears.length];

            //Predictions for current world cup
            logger.info("[" + filePrefix + "] Predicting results for year: " + worldCupYear);
            List<Prediction> predictionList2018 = this.predict(predictor, worldCupYear, filePrefix);

            //Predictions for the past (training data)
            List<Prediction> predictionsForTraining = null;
            for (int i = 0; i < trainingYears.length; i++) {
                logger.info("Predicting results for year: " + trainingYears[i]);
                predictionsForTraining = this.predict(predictor, trainingYears[i], filePrefix);
                trainingScores[i] = this.calculatePerformance(predictionsForTraining, trainingYears[i]);
                trainingPerformance[i] = new Double(trainingScores[i]) / config.getMaxScorePerWorldCup();
                totalPerformance += trainingPerformance[i];
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

            csvPrinter.printRecord(filePrefix,
                    decimalFormat.format(trainingPerformance[0]),
                    decimalFormat.format(trainingPerformance[1]),
                    decimalFormat.format(trainingPerformance[2]),
                    decimalFormat.format(totalPerformance / 3));
        }

        csvPrinter.flush();
        String csvContent = stringWriter.toString();

        FileWriter fileWriter = new FileWriter("batch_" + System.currentTimeMillis() + ".csv");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(csvContent);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private List<String> readFilePrefixes() throws IOException {
        List<String> prefixes = new ArrayList<>();

        FileReader fileReader = new FileReader("batch.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            prefixes.add(line);
        }

        return prefixes;
    }

    /**
     * Predict the results for a world cup
     */
    List<Prediction> predict(Predictor predictor, Integer year, String filePrefix)
            throws Exception {
        List<Prediction> predictions = new ArrayList<>();

        List<Match> matchList = null;
        matchList = matchDAO.fetch(year);

        Prediction prediction = null;
        for (Match match : matchList) {
            Context context = contextBuilder.build(match, year);
            prediction = predictor.predict(match, context, filePrefix);
            if (config.isDebugEnabled()) {
                logger.debug("New prediction for World Cup " + year + ": " + prediction);
            }
            predictions.add(prediction);
        }

        return predictions;
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
