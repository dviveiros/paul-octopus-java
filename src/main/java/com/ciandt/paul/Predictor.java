package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;

/**
 * Predictive algorithm for a World Cup game. This is the hearth of this project. Your performance will be as
 * good as this algorithm... hopefully :-)
 */
public interface Predictor {

    /**
     * Return a prediction for a specific match
     */
    public Prediction predict(Match match, Context context);
}
