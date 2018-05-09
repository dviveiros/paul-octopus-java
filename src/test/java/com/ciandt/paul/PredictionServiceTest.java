package com.ciandt.paul;

import com.ciandt.paul.context.ContextBuilder;
import com.ciandt.paul.dao.FifaRankDAO;
import com.ciandt.paul.dao.MatchDAO;
import com.ciandt.paul.dao.TeamHistoryDAO;
import com.ciandt.paul.entity.HistoricalMatch;
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
        GCSUtils.class, ContextBuilder.class, FifaRankDAO.class, TeamHistoryDAO.class, PredictorFactory.class,
        MatchDAO.class})
public class PredictionServiceTest {

    @Autowired
    private Config config;
    @Autowired
    private PredictionService predictionService;
    @Autowired
    private PredictorFactory predictorFactory;
    @Autowired
    private MatchDAO matchDAO;


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

    /**
     * Test the CSV generation method
     */
    @Test
    public void shouldGenerateCSVFile() throws Exception {
        Predictor predictor = predictorFactory.createsPredictor();
        List<Prediction> predictionList = predictionService.predict(predictor, 2018);
        String csvContent = predictionService.generateCSVContent(predictionList);
        assertNotNull(csvContent);
        assertEquals(49, csvContent.split("\n").length);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet25PointsForExactResultHomeWins() throws Exception {
        //2006: Costa Rica 1x2 Polonia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Costa Rica", "Poland", 1, 2, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(25), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet25PointsForExactResultAwayWins() throws Exception {
        //2006: Spain 3x1 Tunisia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Spain", "Tunisia", 3, 1, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(25), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet18PointsForWinnerScoreHome() throws Exception {
        //2006: Costa Rica 1x2 Polonia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Costa Rica", "Poland", 0, 2, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(18), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet18PointsForWinnerScoreAway() throws Exception {
        //2006: Spain 3x1 Tunisia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Spain", "Tunisia", 3, 0, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(18), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet15PointsForDiffGoalsHome() throws Exception {
        //2006: Costa Rica 1x2 Polonia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Costa Rica", "Poland", 2, 3, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(15), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet15PointsForDiffGoalsAway() throws Exception {
        //2006: Spain 3x1 Tunisia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Spain", "Tunisia", 4, 2, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(15), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet12PointsForLoserScoreHome() throws Exception {
        //2006: Costa Rica 1x2 Polonia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Costa Rica", "Poland", 1, 3, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(12), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet12PointsForLoserScoreAway() throws Exception {
        //2006: Spain 3x1 Tunisia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Spain", "Tunisia", 4, 1, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(12), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet10PointsForTheWinnerHome() throws Exception {
        //2006: Costa Rica 1x2 Polonia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Costa Rica", "Poland", 0, 3, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(10), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet10PointsForTheWinnerAway() throws Exception {
        //2006: Spain 3x1 Tunisia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Spain", "Tunisia", 1, 0, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(10), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet25PointsForTheExactScoreDraw() throws Exception {
        //2006: France 1x1 South Korea
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("France", "South Korea", 1, 1, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(25), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet15PointsForGuessingADraw() throws Exception {
        //2006: France 1x1 South Korea
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("France", "South Korea", 2, 2, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(15), score);
    }

    /**
     * Test the rules
     */
    @Test
    public void shouldGet4PointsForMissingADraw() throws Exception {
        //2006: Spain 3x1 Tunisia
        List<HistoricalMatch> matches2006 = matchDAO.fetchResults(2006);
        Prediction prediction = new Prediction("Spain", "Tunisia", 1, 1, 2006);
        Integer score = predictionService.calculateScore(prediction, matches2006);
        assertEquals(new Integer(4), score);
    }

}
