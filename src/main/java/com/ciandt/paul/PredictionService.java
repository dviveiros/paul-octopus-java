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

        if (config.isDebugEnabled()) {
            logger.debug("Initializing prediction for year: " + worldCupYear);
        }
        Predictor predictor = predictorFactory.createsPredictor();
    }

    /**
     * Predict the results for 2018
     */
    List<Prediction> predict2018(Predictor predictor) throws InterruptedException, DataNotAvailableException, IOException {
        List<Prediction> predictions = new ArrayList<>();

        //Creates predictions for 2018
        if (config.isDebugEnabled()) {
            logger.debug("Predicting results for 2018");
        }
        List<Match> matchList = matchDAO.fetch2018Matches();
        Prediction prediction = null;
        for (Match match : matchList) {
            Context context = contextBuilder.build(match, 2018);
            prediction = predictor.predict(match, context);
            if (config.isDebugEnabled()) {
                logger.debug("New prediction for World Cup 2018: " + prediction);
            }
            predictions.add(prediction);
        }

        return predictions;
    }
}
