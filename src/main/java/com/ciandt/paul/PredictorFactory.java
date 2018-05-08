package com.ciandt.paul;

import org.springframework.stereotype.Service;

/**
 * Creates the predictor algorithm
 */
@Service
public class PredictorFactory {

    /**
     * Creates the predictor algorithm
     */
    public Predictor createsPredictor() {
        return new OneZeroPredictor();
    }
}
