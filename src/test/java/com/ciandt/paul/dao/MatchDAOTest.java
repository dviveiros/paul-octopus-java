package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.HistoricalMatch;
import com.ciandt.paul.entity.Match;
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
 * Test Match data access object
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MatchDAO.class, Config.class, BigQueryUtils.class})
public class MatchDAOTest {

    @Autowired
    private MatchDAO matchDAO;

    @Autowired
    private Config config;

    @Before
    public void setUp() {
        config.setDebug("true");
    }

    @Test
    public void shouldFetchHistoryFor2018() throws Exception {
        List<HistoricalMatch> matchList = matchDAO.fetchHistoryData(2018);
        assertNotNull(matchList);
        assertEquals(636, matchList.size());
    }

    @Test
    public void shouldFetchHistoryFor2014() throws Exception {
        List<HistoricalMatch> matchList = matchDAO.fetchHistoryData(2014);
        assertNotNull(matchList);
        assertEquals(588, matchList.size());
    }

    @Test
    public void shouldFetch2018Matches() throws Exception {
        List<Match> matchList = matchDAO.fetch(2018);
        assertNotNull(matchList);
        assertEquals(48, matchList.size());
    }

    @Test
    public void shouldFetch2006Matches() throws Exception {
        List<Match> matchList = matchDAO.fetch(2006);
        assertNotNull(matchList);
        assertEquals(48, matchList.size());
    }

    @Test
    public void shouldFetch2010Matches() throws Exception {
        List<Match> matchList = matchDAO.fetch(2010);
        assertNotNull(matchList);
        assertEquals(48, matchList.size());
    }

    @Test
    public void shouldFetch2014Matches() throws Exception {
        List<Match> matchList = matchDAO.fetch(2014);
        assertNotNull(matchList);
        assertEquals(48, matchList.size());
    }
}
