package com.ciandt.paul.entity;

import java.util.List;

/**
 * A historical match is a match with a well-known result
 */
public class HistoricalMatch extends Match {

    private Integer homeScore;
    private Integer awayScore;

    /**
     * Constructor
     */
    public HistoricalMatch(List<String> row) {
        super();
        this.setYear(Integer.parseInt(row.get(0)));
        this.setHomeTeam(row.get(1));
        this.setHomeScore(Integer.parseInt(row.get(2)));
        this.setAwayScore(Integer.parseInt(row.get(3)));
        this.setAwayTeam(row.get(4));
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    private void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    private void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }
}
