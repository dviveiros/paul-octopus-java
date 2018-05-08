package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.TeamHistory;
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
 * Class responsible for reading team history data
 */
@Service
public class TeamHistoryDAO {

    private static Logger logger = LoggerFactory.getLogger(TeamHistoryDAO.class.getName());

    @Autowired
    private Config config;
    @Autowired
    private BigQueryUtils bigQueryUtils;

    private static Map<String, TeamHistory> allTeamHistory;

    /**
     * Return the history for a specific team.
     */
    public TeamHistory fetch(String teamName, Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        if (year != 2018) {
            throw new DataNotAvailableException("TeamHistory", year);
        }

        this.buildTeamHashMap();
        return allTeamHistory.get(teamName);
    }

    /**
     * Return the history for all teams
     */
    public List<TeamHistory> fetch(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        if (year != 2018) {
            throw new DataNotAvailableException("TeamHistory", year);
        }

        this.buildTeamHashMap();
        List<TeamHistory> teamHistoryList = new ArrayList<>();
        teamHistoryList.addAll(allTeamHistory.values());
        return teamHistoryList;
    }

    /**
     * Build the internal hashmap
     */
    private void buildTeamHashMap() throws IOException, InterruptedException {
        if (allTeamHistory == null) {
            allTeamHistory = new HashMap<>();

            TeamHistory teamHistory = null;
            String query = "SELECT * FROM paul_the_octopus_dataset.teams order by previous__titles desc";
            List<List<String>> queryResult = bigQueryUtils.executeQuery(query);

            for (List<String> row : queryResult) {
                teamHistory = new TeamHistory();
                teamHistory.setName(row.get(0));
                teamHistory.setPreviousAppearances(Integer.parseInt(row.get(1)));
                teamHistory.setPreviousTitles(Integer.parseInt(row.get(2)));
                teamHistory.setPreviousFinals(Integer.parseInt(row.get(3)));
                teamHistory.setPreviousSemifinals(Integer.parseInt(row.get(4)));

                allTeamHistory.put(teamHistory.getName(), teamHistory);
            }
        }
    }
}
