package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.TeamHistory;
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
 * Test Team History data access object
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TeamHistoryDAO.class, Config.class, BigQueryUtils.class, GCSUtils.class})
public class TeamHistoryDAOTest {

    @Autowired
    private TeamHistoryDAO teamHistoryDAO;

    @Autowired
    private Config config;

    @Before
    public void setUp() {
        config.setDebug("true");
    }

    @Test
    public void shouldFetchAllTeamsFor2018() throws Exception {
        List<TeamHistory> teamHistoryList = teamHistoryDAO.fetch(2018);
        assertNotNull(teamHistoryList);
        assertEquals(32, teamHistoryList.size());
    }

    @Test
    public void shouldFetchBrazilFor2018() throws Exception {
        TeamHistory teamHistory = teamHistoryDAO.fetch("Brazil", 2018);
        assertNotNull(teamHistory);
        assertEquals("Brazil", teamHistory.getName());
        assertEquals(new Integer(20), teamHistory.getPreviousAppearances());
        assertEquals(new Integer(5), teamHistory.getPreviousTitles());
        assertEquals(new Integer(7), teamHistory.getPreviousFinals());
        assertEquals(new Integer(11), teamHistory.getPreviousSemifinals());
    }

    @Test
    public void shouldFetchAllTeamsFor2006() throws Exception {
        List<TeamHistory> teamHistoryList = teamHistoryDAO.fetch(2006);
        assertNotNull(teamHistoryList);
        assertEquals(32, teamHistoryList.size());
    }

    @Test
    public void shouldFetchBrazilFor2006() throws Exception {
        TeamHistory teamHistory = teamHistoryDAO.fetch("Brazil", 2006);
        assertNotNull(teamHistory);
        assertEquals("Brazil", teamHistory.getName());
        assertEquals(new Integer(17), teamHistory.getPreviousAppearances());
        assertEquals(new Integer(5), teamHistory.getPreviousTitles());
        assertEquals(new Integer(7), teamHistory.getPreviousFinals());
        assertEquals(new Integer(10), teamHistory.getPreviousSemifinals());
    }

    @Test
    public void shouldFetchAllTeamsFor2010() throws Exception {
        List<TeamHistory> teamHistoryList = teamHistoryDAO.fetch(2010);
        assertNotNull(teamHistoryList);
        assertEquals(32, teamHistoryList.size());
    }

    @Test
    public void shouldFetchBrazilFor2010() throws Exception {
        TeamHistory teamHistory = teamHistoryDAO.fetch("Brazil", 2010);
        assertNotNull(teamHistory);
        assertEquals("Brazil", teamHistory.getName());
        assertEquals(new Integer(18), teamHistory.getPreviousAppearances());
        assertEquals(new Integer(5), teamHistory.getPreviousTitles());
        assertEquals(new Integer(7), teamHistory.getPreviousFinals());
        assertEquals(new Integer(10), teamHistory.getPreviousSemifinals());
    }

    @Test
    public void shouldFetchAllTeamsFor2014() throws Exception {
        List<TeamHistory> teamHistoryList = teamHistoryDAO.fetch(2014);
        assertNotNull(teamHistoryList);
        assertEquals(32, teamHistoryList.size());
    }

    @Test
    public void shouldFetchBrazilFor2014() throws Exception {
        TeamHistory teamHistory = teamHistoryDAO.fetch("Brazil", 2014);
        assertNotNull(teamHistory);
        assertEquals("Brazil", teamHistory.getName());
        assertEquals(new Integer(19), teamHistory.getPreviousAppearances());
        assertEquals(new Integer(5), teamHistory.getPreviousTitles());
        assertEquals(new Integer(7), teamHistory.getPreviousFinals());
        assertEquals(new Integer(10), teamHistory.getPreviousSemifinals());
    }

}
