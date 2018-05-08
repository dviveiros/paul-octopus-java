package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.context.ContextBuilder;
import com.ciandt.paul.dao.DataNotAvailableException;
import com.ciandt.paul.dao.MatchDAO;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    /**
     * Start the prediction process
     */
    public void predict() throws InterruptedException, DataNotAvailableException, IOException {

        //Year to be predicted
        Integer worldCupYear = config.getWorldCupYear();
        Integer[] trainingYears = config.getTrainingYears();

        Predictor predictor = predictorFactory.createsPredictor();

        //Predictions for current world cup
        if (config.isDebugEnabled()) {
            logger.debug("Initializing prediction for year: " + worldCupYear);
        }
        List<Prediction> predictionList2018 = this.predict(predictor, worldCupYear);

        //Predictions for the past (training data)
        List<Prediction> predictionsForTraining = null;
        for (int i = 0; i < trainingYears.length; i++) {
            if (config.isDebugEnabled()) {
                logger.debug("Initializing prediction for year: " + trainingYears[i]);
            }
            predictionsForTraining = this.predict(predictor, trainingYears[i]);
        }
    }

    /**
     * Predict the results for a world cup
     */
    List<Prediction> predict(Predictor predictor, Integer year) throws InterruptedException, DataNotAvailableException, IOException {
        List<Prediction> predictions = new ArrayList<>();

        //Creates predictions
        if (config.isDebugEnabled()) {
            logger.debug("Predicting results for " + year);
        }
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

}
