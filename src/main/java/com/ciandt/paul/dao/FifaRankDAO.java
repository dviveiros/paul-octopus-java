package com.ciandt.paul.dao;

import com.ciandt.paul.GeneralConfig;
import com.ciandt.paul.entity.FifaRank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Read the rank for a specific year
     */
    public List<FifaRank> fetch(String year) {
        List<FifaRank> fifaRankList = new ArrayList<>();


        return fifaRankList;
    }
}
