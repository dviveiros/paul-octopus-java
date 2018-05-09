package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.HistoricalMatch;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.utils.BigQueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for reading matches data
 */
@Service
public class MatchDAO {

    private static Logger logger = LoggerFactory.getLogger(MatchDAO.class.getName());

    @Autowired
    private Config config;
    @Autowired
    private BigQueryUtils bigQueryUtils;

    private static List<HistoricalMatch> allMatches;
    private static Map<Integer, List<Match>> matchesCache;

    static {
        matchesCache = new HashMap<>();
    }

    /**
     * Return the matches to be predicted for a specific year
     */
    public List<Match> fetch(Integer year) throws IOException, InterruptedException, DataNotAvailableException {
        if (matchesCache.get(year) != null) {
            return matchesCache.get(year);
        } else {
            if (config.isDebugEnabled()) {
                logger.debug("Loading 2018 matches");
            }

            List<Match> matchList = new ArrayList<>();
            Match match = null;
            String query = null;

            if (year == 2018) {
                query = "SELECT 2018, home, away FROM paul_the_octopus_dataset.matches order by date";
            } else {
                query = "SELECT " + year + ", home, away FROM paul_the_octopus_dataset.matches_history where year=" + year;
            }

            List<List<String>> queryResult = bigQueryUtils.executeQuery(query);
            for (List<String> row : queryResult) {
                match = new Match(row);
                matchList.add(match);
            }
            matchesCache.put(year, matchList);

            if (config.isDebugEnabled()) {
                logger.debug("Data loaded. Found " + matchList.size() + " games");
            }

            if (matchList.size() == 0) {
                throw new DataNotAvailableException("Match", 2018);
            }

            return matchList;
        }
    }

    /**
     * Return history data prior to this world cup
     *
     * @param year This method will return data prior to this year
     * @return List of matches prior to the year ordered by year desc
     */
    public List<HistoricalMatch> fetchHistoryData(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        this.loadHistoricalMatches();

        List<HistoricalMatch> matchList = new ArrayList<>();
        for (HistoricalMatch match : allMatches) {
            if (match.getYear() >= year) {
                continue;
            } else {
                matchList.add(match);
            }
        }

        if ((matchList.size() == 0) && (year > 1930)) {
            throw new DataNotAvailableException("HistoricalMatch", year);
        }

        return matchList;
    }

    /**
     * Return the actual results for a specific year
     *
     * @param year Year for the results
     * @return Results for the year
     */
    public List<HistoricalMatch> fetchResults(Integer year)
            throws IOException, InterruptedException, DataNotAvailableException {
        this.loadHistoricalMatches();

        List<HistoricalMatch> matchList = new ArrayList<>();
        for (HistoricalMatch match : allMatches) {
            if (match.getYear().equals(year)) {
                matchList.add(match);
            }
        }

        if (matchList.size() == 0) {
            throw new DataNotAvailableException("HistoricalMatch", year);
        }

        return matchList;
    }

    /**
     * Load matches
     */
    private void loadHistoricalMatches() throws IOException, InterruptedException {
        //check the cache
        if (allMatches == null) {
            allMatches = new ArrayList<>();

            if (config.isDebugEnabled()) {
                logger.debug("Loading history matches data");
            }


            HistoricalMatch match = null;
            String query = "SELECT * FROM paul_the_octopus_dataset.matches_history order by year desc";
            List<List<String>> queryResult = bigQueryUtils.executeQuery(query);

            for (List<String> row : queryResult) {
                match = new HistoricalMatch(row);
                allMatches.add(match);
            }

            if (config.isDebugEnabled()) {
                logger.debug("Data loaded. Found " + allMatches.size() + " games");
            }
        }
    }

}

