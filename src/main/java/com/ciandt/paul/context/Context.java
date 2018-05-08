package com.ciandt.paul.context;

import com.ciandt.paul.entity.FifaRank;
import com.ciandt.paul.entity.HistoricalMatch;
import com.ciandt.paul.entity.TeamHistory;

import java.util.List;

/**
 * Context with data to be used to create a prediction
 */
public class Context {

    private TeamHistory homeTeamHistory;
    private TeamHistory awayTeamHistory;
    private FifaRank homeFifaRank;
    private FifaRank awayFifaRank;
    private List<HistoricalMatch> historicalMatches;
    private ContextBuilder contextBuilder;

    /**
     * Constructor
     */
    Context() {
    }

    public TeamHistory getHomeTeamHistory() {
        return homeTeamHistory;
    }

    void setHomeTeamHistory(TeamHistory homeTeamHistory) {
        this.homeTeamHistory = homeTeamHistory;
    }

    public TeamHistory getAwayTeamHistory() {
        return awayTeamHistory;
    }

    void setAwayTeamHistory(TeamHistory awayTeamHistory) {
        this.awayTeamHistory = awayTeamHistory;
    }

    public FifaRank getHomeFifaRank() {
        return homeFifaRank;
    }

    void setHomeFifaRank(FifaRank homeFifaRank) {
        this.homeFifaRank = homeFifaRank;
    }

    public FifaRank getAwayFifaRank() {
        return awayFifaRank;
    }

    void setAwayFifaRank(FifaRank awayFifaRank) {
        this.awayFifaRank = awayFifaRank;
    }

    public List<HistoricalMatch> getHistoricalMatches() {
        return historicalMatches;
    }

    void setHistoricalMatches(List<HistoricalMatch> historicalMatches) {
        this.historicalMatches = historicalMatches;
    }


}
