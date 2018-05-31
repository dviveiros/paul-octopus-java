package com.ciandt.paul;

import com.ciandt.paul.context.Context;
import com.ciandt.paul.entity.HistoricalMatch;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.entity.Prediction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVPredictor extends Predictor {

    private static Logger logger = LoggerFactory.getLogger(CSVPredictor.class.getName());
    private static final String BUCKET = "ciandt_projectoctopus_2018_leonardo";

    private static Map<Integer, List<HistoricalMatch>> listMap;

    static {
        listMap = new HashMap<>();
    }

    @Override
    public Prediction predict(Match match, Context context, String filePrefix) throws IOException {

        Prediction prediction = null;
        Integer year = match.getYear();
        List<HistoricalMatch> matchResults = listMap.get(year);

        if (matchResults == null) {
            matchResults = loadYear(year, filePrefix);
            listMap.put(year, matchResults);
        }

        for (HistoricalMatch historicalMatch : matchResults) {
            if (match.getHomeTeam().equals(historicalMatch.getHomeTeam())
                    && match.getAwayTeam().equals(historicalMatch.getAwayTeam())) {
                prediction = new Prediction(match);
                prediction.setHomeScore(historicalMatch.getHomeScore());
                prediction.setAwayScore(historicalMatch.getAwayScore());
            }
        }

        if (prediction == null) {
            throw new RuntimeException("Match not found: " + match);
        }

        return prediction;
    }

    private List<HistoricalMatch> loadYear(Integer year, String filePrefix) throws IOException {
        List<HistoricalMatch> matchResults = new ArrayList<>();

        String filename = null;
        if (year == 2018) {
            filename = "predictions.csv";
        } else {
            filename = filePrefix + "_" + year + ".csv";
        }

        logger.info("Reading file " + filename + " on bucket " + BUCKET);

        String content = getGcsUtils().readFile(BUCKET, filename);
        Reader in = new StringReader(content);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
        HistoricalMatch matchResult = null;
        for (CSVRecord record : records) {
            matchResult = new HistoricalMatch(record, year);
            logger.info("Adding result: " + matchResult);
            matchResults.add(matchResult);
        }

        return matchResults;
    }
}
