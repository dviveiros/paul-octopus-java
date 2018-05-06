package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.Match;
import com.ciandt.paul.utils.BigQueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static List<Match> allMatches;

    /**
     * Return history data prior to this world cup
     *
     * @param year This method will return data prior to this year
     * @return List of matches prior to the year ordered by year desc
     */
    public List<Match> fetchHistoryData(Integer year) throws IOException, InterruptedException {

        //check the cache
        if (allMatches == null) {
            allMatches = new ArrayList<>();

            if (config.isDebugEnabled()) {
                logger.debug("Loading history matches data");
            }


            Match match = null;
            String query = "SELECT * FROM paul_the_octopus_dataset.matches_history order by year desc";
            List<List<String>> queryResult = bigQueryUtils.executeQuery(query);

            for (List<String> row : queryResult) {
                match = new Match();
                match.setYear(Integer.parseInt(row.get(0)));
                match.setHomeTeam(row.get(1));
                match.setHomeScore(Integer.parseInt(row.get(2)));
                match.setAwayScore(Integer.parseInt(row.get(3)));
                match.setAwayTeam(row.get(4));
                allMatches.add(match);
            }

            if (config.isDebugEnabled()) {
                logger.debug("Data loaded. Found " + allMatches.size() + " games");
            }
        }

        List<Match> matchList = new ArrayList<>();
        for (Match match : allMatches) {
            if (match.getYear() >= year) {
                continue;
            } else {
                matchList.add(match);
            }
        }

        return matchList;
    }

}

