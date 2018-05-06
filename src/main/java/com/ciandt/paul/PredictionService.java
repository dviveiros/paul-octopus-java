package com.ciandt.paul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles feed requests and processing
 */
@Service
public class PredictionService {

    private static Logger logger = LoggerFactory.getLogger(PredictionService.class.getName());

    @Autowired
    private Config config;

    /**
     * Start the prediction process
     */
    public void predict(String worldCupYear) {
        if (config.isDebugEnabled()) {
            logger.debug("Initializing prediction for year: " + worldCupYear);
        }


    }
}
