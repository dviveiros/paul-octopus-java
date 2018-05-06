package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.FifaRank;
import com.ciandt.paul.utils.BigQueryUtils;
import com.ciandt.paul.utils.GCSUtils;
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
@SpringBootTest(classes = {FifaRankDAO.class, Config.class, BigQueryUtils.class, GCSUtils.class})
public class FifaRankDAOTest {

    @Autowired
    private FifaRankDAO fifaRankDAO;

    @Autowired
    private Config config;

    @Before
    public void setUp() {
        config.setDebug("true");
    }

    @Test
    public void shouldFetchFifaRankFor2018() throws Exception {
        List<FifaRank> fifaRankList = fifaRankDAO.fetch(2018);
        assertNotNull(fifaRankList);
        assertEquals(211, fifaRankList.size());
    }

    @Test
    public void shouldFetchFifaRankFor2006() throws Exception {
        List<FifaRank> fifaRankList = fifaRankDAO.fetch(2006);
        assertNotNull(fifaRankList);
        assertEquals(32, fifaRankList.size());

        assertEquals("BRA", fifaRankList.get(0).getTeamCode());
        assertEquals("Brazil", fifaRankList.get(0).getTeamName());
        assertEquals(new Integer(827), fifaRankList.get(0).getPoints());

        assertEquals("TOG", fifaRankList.get(31).getTeamCode());
        assertEquals("Togo", fifaRankList.get(31).getTeamName());
        assertEquals(new Integer(569), fifaRankList.get(31).getPoints());
    }

    @Test
    public void shouldFetchFifaRankFor2010() throws Exception {
        List<FifaRank> fifaRankList = fifaRankDAO.fetch(2010);
        assertNotNull(fifaRankList);
        assertEquals(32, fifaRankList.size());

        assertEquals("BRA", fifaRankList.get(0).getTeamCode());
        assertEquals("Brazil", fifaRankList.get(0).getTeamName());
        assertEquals(new Integer(1611), fifaRankList.get(0).getPoints());

        assertEquals("NKO", fifaRankList.get(31).getTeamCode());
        assertEquals("North Korea", fifaRankList.get(31).getTeamName());
        assertEquals(new Integer(292), fifaRankList.get(31).getPoints());
    }

    @Test
    public void shouldFetchFifaRankFor2014() throws Exception {
        List<FifaRank> fifaRankList = fifaRankDAO.fetch(2014);
        assertNotNull(fifaRankList);
        assertEquals(32, fifaRankList.size());

        assertEquals("ESP", fifaRankList.get(0).getTeamCode());
        assertEquals("Spain", fifaRankList.get(0).getTeamName());
        assertEquals(new Integer(1485), fifaRankList.get(0).getPoints());

        assertEquals("AUS", fifaRankList.get(31).getTeamCode());
        assertEquals("Australia", fifaRankList.get(31).getTeamName());
        assertEquals(new Integer(526), fifaRankList.get(31).getPoints());
    }
}
