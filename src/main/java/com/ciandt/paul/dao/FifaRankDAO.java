package com.ciandt.paul.dao;

import com.ciandt.paul.GeneralConfig;
import com.ciandt.paul.entity.FifaRank;
import com.ciandt.paul.utils.BigQueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for reading Fifa Rank data for a specific year
 */
@Service
public class FifaRankDAO {

    private static Logger logger = LoggerFactory.getLogger(FifaRankDAO.class.getName());

    @Autowired
    private GeneralConfig config;
    @Autowired
    private BigQueryUtils bigQueryUtils;

    /**
     * Read the rank for a specific year
     */
    public List<FifaRank> fetch(Integer year) throws IOException, InterruptedException {
        List<FifaRank> fifaRankList = new ArrayList<>();
        List<List<String>> queryResult = null;

        String query = "SELECT * FROM paul_the_octopus_dataset.fifa_rank LIMIT 300";
        queryResult = bigQueryUtils.executeQuery(query);
        FifaRank fifaRank = null;

        for (List<String> row : queryResult) {
            fifaRank = new FifaRank();
            fifaRank.setRank(Integer.valueOf(row.get(0)));
            fifaRank.setTeamCode(row.get(1));
            fifaRank.setTeamName(row.get(2));
            fifaRank.setPoints(Integer.parseInt(row.get(3)));
            fifaRankList.add(fifaRank);
        }

        return fifaRankList;
    }
}
