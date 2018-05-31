package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;
import com.ciandt.paul.utils.GCSUtils;

/**
 * Predictive algorithm for a World Cup game. This is the hearth of this project. Your performance will be as
 * good as this algorithm... hopefully :-)
 */
public abstract class Predictor {

    private GCSUtils gcsUtils;

    /**
     * Return a prediction for a specific match
     */
    public abstract Prediction predict(Match match, Context context, String filePrefix) throws Exception;

    public GCSUtils getGcsUtils() {
        return gcsUtils;
    }

    public void setGcsUtils(GCSUtils gcsUtils) {
        this.gcsUtils = gcsUtils;
    }
}
