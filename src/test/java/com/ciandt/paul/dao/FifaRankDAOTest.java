package com.ciandt.paul.dao;

import com.ciandt.paul.GeneralConfig;
import com.ciandt.paul.entity.FifaRank;
import com.ciandt.paul.utils.BigQueryUtils;
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
 * Test FifaRank data access object
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FifaRankDAO.class, GeneralConfig.class, BigQueryUtils.class})
public class FifaRankDAOTest {

    @Autowired
    private FifaRankDAO fifaRankDAO;

    @Autowired
    private GeneralConfig config;

    @Before
    public void setUp() {
        config.setDebug("true");
    }

    @Test
    public void shouldFetchFifaRankPerYear() throws Exception {
        List<FifaRank> fifaRankList = fifaRankDAO.fetch(2018);
        assertNotNull(fifaRankList);
        assertEquals(211, fifaRankList.size());
    }
}
