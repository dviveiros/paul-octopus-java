package com.ciandt.paul;

import com.ciandt.paul.context.ContextBuilder;
import com.ciandt.paul.dao.FifaRankDAO;
import com.ciandt.paul.dao.MatchDAO;
import com.ciandt.paul.dao.TeamHistoryDAO;
import com.ciandt.paul.entity.Prediction;
import com.ciandt.paul.utils.BigQueryUtils;
import com.ciandt.paul.utils.GCSUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Test the prediction service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PredictionService.class, MatchDAO.class, Config.class, BigQueryUtils.class,
        GCSUtils.class, ContextBuilder.class, FifaRankDAO.class, TeamHistoryDAO.class, PredictorFactory.class})
public class PredictionServiceTest {

    @Autowired
    private Config config;
    @Autowired
    private PredictionService predictionService;
    @Autowired
    private PredictorFactory predictorFactory;


    @Before
    public void setUp() {
        config.setDebug("true");
    }

    /**
     * Test the prediction method
     */
    @Test
    public void shouldPredict2018() throws Exception {
        Predictor predictor = predictorFactory.createsPredictor();
        List<Prediction> predictionList = predictionService.predict(predictor, 2018);
        assertNotNull(predictionList);
        assertEquals(48, predictionList.size());
    }

    /**
     * Test the prediction method
     */
    @Test
    public void shouldPredict2006() throws Exception {
        Predictor predictor = predictorFactory.createsPredictor();
        List<Prediction> predictionList = predictionService.predict(predictor, 2006);
        assertNotNull(predictionList);
        assertEquals(48, predictionList.size());
    }

    /**
     * Test the prediction method
     */
    @Test
    public void shouldPredict2010() throws Exception {
        Predictor predictor = predictorFactory.createsPredictor();
        List<Prediction> predictionList = predictionService.predict(predictor, 2010);
        assertNotNull(predictionList);
        assertEquals(48, predictionList.size());
    }

    /**
     * Test the prediction method
     */
    @Test
    public void shouldPredict2014() throws Exception {
        Predictor predictor = predictorFactory.createsPredictor();
        List<Prediction> predictionList = predictionService.predict(predictor, 2014);
        assertNotNull(predictionList);
        assertEquals(48, predictionList.size());
    }
}
