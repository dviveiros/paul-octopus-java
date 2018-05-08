package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.FifaRank;
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
 * Class responsible for reading Fifa Rank data
 */
@Service
public class FifaRankDAO {

    private static Logger logger = LoggerFactory.getLogger(FifaRankDAO.class.getName());

    @Autowired
    private Config config;
    @Autowired
    private BigQueryUtils bigQueryUtils;
    @Autowired
    private GCSUtils gcsUtils;

    private static Map<Integer, List<FifaRank>> cache;

    static {
        cache = new HashMap();
    }

    /**
     * Read the rank for a specific year
     */
    public List<FifaRank> fetch(Integer year) throws IOException, InterruptedException, DataNotAvailableException {

        //check the cache
        if (cache.get(year) != null) {
            return cache.get(year);
        }

        List<FifaRank> fifaRankList = new ArrayList<>();
        FifaRank fifaRank = null;

        // The data for 2006, 2010 and 2014 is inside GCS, bucket project-paul-the-octopus-datasets
        // 2018 is on BigQuery
        if ((year == 2006) || (year == 2010) || (year == 2014)) {
            String filename = "fifa_rank_" + year + ".csv";
            String content = gcsUtils.readFile(config.getDatasetBucket(), filename);
            Reader in = new StringReader(content);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                fifaRank = new FifaRank(record);
                fifaRankList.add(fifaRank);
            }
        } else {
            List<List<String>> queryResult = null;

            String query = "SELECT * FROM paul_the_octopus_dataset.fifa_rank LIMIT 300";
            queryResult = bigQueryUtils.executeQuery(query);

            if ((queryResult == null) || (queryResult.size() == 0)) {
                throw new DataNotAvailableException("FifaRank", year);
            }

            for (List<String> row : queryResult) {
                fifaRank = new FifaRank(row);
                fifaRankList.add(fifaRank);
            }
        }

        cache.put(year, fifaRankList);
        return fifaRankList;
    }
}
