package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;

/**
 * Dummy predictor that predicts that all games will be 1x0
 */
public class OneZeroPredictor implements Predictor {


    @Override
    public Prediction predict(Match match, Context context) {
        Prediction prediction = new Prediction(match);
        prediction.setHomeScore(1);
        prediction.setAwayScore(0);
        return prediction;
    }
}
