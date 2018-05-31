package com.ciandt.paul;

import com.ciandt.paul.utils.GCSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Creates the predictor algorithm
 */
@Service
public class PredictorFactory {

    private static Logger logger = LoggerFactory.getLogger(PredictorFactory.class.getName());

    @Autowired
    private Config config;
    @Autowired
    private GCSUtils gcsUtils;

    /**
     * Creates the predictor algorithm
     */
    public Predictor createsPredictor() {
        return this.createsPredictor(config.getDefaultPredictor());
    }

    /**
     * Creates the predictor algorithm
     */
    public Predictor createsPredictor(String strPredictor) {

        Predictor predictor = null;

        try {
            Class clazz = Class.forName("com.ciandt.paul." + strPredictor);
            predictor = (Predictor) clazz.newInstance();
            predictor.setGcsUtils(gcsUtils);
        } catch (ClassNotFoundException e) {
            logger.error("Predictor class not found: " + strPredictor);
            System.exit(1);
        } catch (IllegalAccessException e) {
            logger.error("Error creating Predictor class: " + strPredictor);
            System.exit(1);
        } catch (InstantiationException e) {
            logger.error("Error creating Predictor class: " + strPredictor);
            System.exit(1);
        }

        return predictor;
    }
}
