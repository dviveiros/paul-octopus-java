package com.ciandt.paul.context;

import com.ciandt.paul.dao.DataNotAvailableException;
import com.ciandt.paul.dao.FifaRankDAO;
import com.ciandt.paul.dao.MatchDAO;
import com.ciandt.paul.dao.TeamHistoryDAO;
import com.ciandt.paul.entity.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Builds the context for the predictive algorithm
 */
@Service
public class ContextBuilder {

    @Autowired
    private FifaRankDAO fifaRankDAO;
    @Autowired
    private MatchDAO matchDAO;
    @Autowired
    private TeamHistoryDAO teamHistoryDAO;

    /**
     * Build the context
     */
    public Context build(Match match, Integer year) throws InterruptedException, DataNotAvailableException, IOException {
        Context context = new Context();

        //Fifa rank
        context.setHomeFifaRank(fifaRankDAO.fetch(match.getHomeTeam(), year));
        context.setAwayFifaRank(fifaRankDAO.fetch(match.getAwayTeam(), year));

        //Team history
        context.setHomeTeamHistory(teamHistoryDAO.fetch(match.getHomeTeam(), year));
        context.setAwayTeamHistory(teamHistoryDAO.fetch(match.getAwayTeam(), year));

        //Historical matches
        context.setHistoricalMatches(matchDAO.fetchHistoryData(year));

        return context;
    }
}
