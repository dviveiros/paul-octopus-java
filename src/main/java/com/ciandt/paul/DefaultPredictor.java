package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;

/**
 * Default predictor to be used if not specified by the -p argument
 */
public class DefaultPredictor implements Predictor {

    private Predictor innerPredictor;

    /**
     * Constructor
     */
    public DefaultPredictor() {
        innerPredictor = new OneZeroPredictor();
    }

    @Override
    public Prediction predict(Match match, Context context) {
        return innerPredictor.predict(match, context);
    }
}
