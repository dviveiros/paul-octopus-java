package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.TeamHistory;
import com.ciandt.paul.utils.BigQueryUtils;
import com.ciandt.paul.utils.GCSUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
    @Autowired
    private GCSUtils gcsUtils;

    private static Map<String, TeamHistory> allTeamHistory;
    private static Map<Integer, List<TeamHistory>> historyCache;

    static {
        allTeamHistory = new HashMap<>();
        historyCache = new HashMap<>();
    }

    /**
     * Return the history for a specific team.
     */
    public TeamHistory fetch(String teamName, Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        if (historyCache.get(year) == null) {
            this.buildTeamHashMap(year);
        }

        return allTeamHistory.get(year + "_" + teamName);
    }

    /**
     * Return the history for all teams
     */
    public List<TeamHistory> fetch(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        if (historyCache.get(year) == null) {
            this.buildTeamHashMap(year);
        }

        return historyCache.get(year);
    }

    /**
     * Build the internal hashmap
     */
    private void buildTeamHashMap(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        List<TeamHistory> teamHistoryList = new ArrayList<>();
        if (config.isDebugEnabled()) {
            logger.debug("Loading team information for " + year);
        }

        if (year == 2018) { //data on BigQuery
            TeamHistory teamHistory = null;
            String query = "SELECT * FROM paul_the_octopus_dataset.teams order by previous__titles desc";
            List<List<String>> queryResult = bigQueryUtils.executeQuery(query);

            for (List<String> row : queryResult) {
                teamHistory = new TeamHistory(row);
                allTeamHistory.put(year + "_" + teamHistory.getName(), teamHistory);
                teamHistoryList.add(teamHistory);
            }
        } else {
            String filename = "teams_" + year + ".csv";
            String content = gcsUtils.readFile(config.getDatasetBucket(), filename);
            Reader in = new StringReader(content);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
            TeamHistory teamHistory = null;
            for (CSVRecord record : records) {
                teamHistory = new TeamHistory(record);
                allTeamHistory.put(year + "_" + teamHistory.getName(), teamHistory);
                teamHistoryList.add(teamHistory);
            }
        }

        historyCache.put(year, teamHistoryList);
        if (config.isDebugEnabled()) {
            logger.debug("Found " + teamHistoryList.size() + " records for " + year);
        }
    }
}
