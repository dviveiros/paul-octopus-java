package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;
import com.ciandt.paul.utils.GCSUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default predictor to be used if not specified by the -p argument
 */
public class DefaultPredictor extends Predictor {

    private Predictor innerPredictor;

    @Autowired
    private GCSUtils gcsUtils;
    @Autowired
    private Config config;

    /**
     * Constructor
     */
    public DefaultPredictor() {
        innerPredictor = new CSVPredictor();
    }

    @Override
    public Prediction predict(Match match, Context context, String filePrefix) throws Exception {
        return innerPredictor.predict(match, context, filePrefix);
    }
}
