package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;

/**
 * Dummy predictor that predicts that all games will be 0x0
 */
public class ZeroZeroPredictor implements Predictor {


    @Override
    public Prediction predict(Match match, Context context) {
        Prediction prediction = new Prediction(match);
        prediction.setHomeScore(0);
        prediction.setAwayScore(0);
        return prediction;
    }
}