package com.ciandt.paul.dao;

import com.ciandt.paul.Config;
import com.ciandt.paul.entity.TeamHistory;
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
@SpringBootTest(classes = {TeamHistoryDAO.class, Config.class, BigQueryUtils.class})
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
    public void shouldFetchAllTeams() throws Exception {
        List<TeamHistory> teamHistoryList = teamHistoryDAO.fetch(2018);
        assertNotNull(teamHistoryList);
        assertEquals(32, teamHistoryList.size());
    }

    @Test
    public void shouldFetchBrazil() throws Exception {
        TeamHistory teamHistory = teamHistoryDAO.fetch("Brazil", 2018);
        assertNotNull(teamHistory);
        assertEquals("Brazil", teamHistory.getName());
        assertEquals(new Integer(20), teamHistory.getPreviousAppearances());
        assertEquals(new Integer(5), teamHistory.getPreviousTitles());
        assertEquals(new Integer(7), teamHistory.getPreviousFinals());
        assertEquals(new Integer(11), teamHistory.getPreviousSemifinals());
    }

}
