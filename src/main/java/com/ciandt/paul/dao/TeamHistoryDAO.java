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

    private static Map<String, TeamHistory> allTeamHistory2018;
    private static Map<String, TeamHistory> allTeamHistory2006;
    private static Map<String, TeamHistory> allTeamHistory2010;
    private static Map<String, TeamHistory> allTeamHistory2014;

    /**
     * Return the history for a specific team.
     */
    public TeamHistory fetch(String teamName, Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        Map<String, TeamHistory> teamMap = this.buildTeamHashMap(year);
        return teamMap.get(teamName);
    }

    /**
     * Return the history for all teams
     */
    public List<TeamHistory> fetch(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        Map<String, TeamHistory> teamHistoryMap = this.buildTeamHashMap(year);
        List<TeamHistory> teamHistoryList = new ArrayList<>();
        teamHistoryList.addAll(teamHistoryMap.values());
        return teamHistoryList;
    }

    /**
     * Build the internal hashmap
     */
    private Map<String, TeamHistory> buildTeamHashMap(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        if (config.isDebugEnabled()) {
            logger.debug("Loading team information for " + year);
        }

        if (year == 2018) { //data on BigQuery
            if (allTeamHistory2018 == null) {
                allTeamHistory2018 = new HashMap<>();

                TeamHistory teamHistory = null;
                String query = "SELECT * FROM paul_the_octopus_dataset.teams order by previous__titles desc";
                List<List<String>> queryResult = bigQueryUtils.executeQuery(query);

                for (List<String> row : queryResult) {
                    teamHistory = new TeamHistory(row);
                    allTeamHistory2018.put(teamHistory.getName(), teamHistory);
                }
            }

            if (config.isDebugEnabled()) {
                logger.debug("Found " + allTeamHistory2018.size() + " records for " + year);
            }
            return allTeamHistory2018;
        } else {
            if ((year == 2006) || (year == 2010) || (year == 2014)) { //data on GCS

                String filename = "teams_" + year + ".csv";
                String content = gcsUtils.readFile(config.getDatasetBucket(), filename);
                Reader in = new StringReader(content);
                Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
                TeamHistory teamHistory = null;
                for (CSVRecord record : records) {
                    teamHistory = new TeamHistory(record);

                    if (year == 2006) {
                        if (allTeamHistory2006 == null) {
                            allTeamHistory2006 = new HashMap<>();
                        }
                        allTeamHistory2006.put(teamHistory.getName(), teamHistory);
                    } else if (year == 2010) {
                        if (allTeamHistory2010 == null) {
                            allTeamHistory2010 = new HashMap<>();
                        }
                        allTeamHistory2010.put(teamHistory.getName(), teamHistory);
                    } else if (year == 2014) {
                        if (allTeamHistory2014 == null) {
                            allTeamHistory2014 = new HashMap<>();
                        }
                        allTeamHistory2014.put(teamHistory.getName(), teamHistory);
                    } else {
                        throw new RuntimeException("This code should be unreacheable");
                    }
                }

                if (year == 2006) {
                    if (config.isDebugEnabled()) {
                        logger.debug("Found " + allTeamHistory2006.size() + " records for " + year);
                    }
                    return allTeamHistory2006;
                } else if (year == 2010) {
                    if (config.isDebugEnabled()) {
                        logger.debug("Found " + allTeamHistory2010.size() + " records for " + year);
                    }
                    return allTeamHistory2010;
                } else if (year == 2014) {
                    if (config.isDebugEnabled()) {
                        logger.debug("Found " + allTeamHistory2014.size() + " records for " + year);
                    }
                    return allTeamHistory2014;
                } else {
                    throw new RuntimeException("This code should be unreacheable");
                }
            } else { //data unavailable
                throw new DataNotAvailableException("TeamHistory", year);
            }
        }
    }
}
