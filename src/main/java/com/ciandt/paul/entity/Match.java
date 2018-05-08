package com.ciandt.paul.entity;

import java.util.List;

/**
 * World cup match
 */
public class Match {

    private Integer year;
    private String homeTeam;
    private String awayTeam;

    /**
     * Constructor
     */
    protected Match() {
    }

    /**
     * Constructor
     */
    public Match(List<String> row) {
        super();
        this.setYear(Integer.parseInt(row.get(0)));
        this.setHomeTeam(row.get(1));
        this.setAwayTeam(row.get(2));
    }

    public Integer getYear() {
        return year;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    protected void setYear(Integer year) {
        this.year = year;
    }

    protected void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    protected void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        return "Match{" +
                "year=" + year +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam +
                '}';
    }
}
