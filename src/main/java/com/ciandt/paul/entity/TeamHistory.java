package com.ciandt.paul.entity;

public class TeamHistory {

    private String name;
    private Integer previousTitles;
    private Integer previousAppearances;
    private Integer previousFinals;
    private Integer previousSemifinals;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPreviousTitles() {
        return previousTitles;
    }

    public void setPreviousTitles(Integer previousTitles) {
        this.previousTitles = previousTitles;
    }

    public Integer getPreviousAppearances() {
        return previousAppearances;
    }

    public void setPreviousAppearances(Integer previousAppearances) {
        this.previousAppearances = previousAppearances;
    }

    public Integer getPreviousFinals() {
        return previousFinals;
    }

    public void setPreviousFinals(Integer previousFinals) {
        this.previousFinals = previousFinals;
    }

    public Integer getPreviousSemifinals() {
        return previousSemifinals;
    }

    public void setPreviousSemifinals(Integer previousSemifinals) {
        this.previousSemifinals = previousSemifinals;
    }

    @Override
    public String toString() {
        return "TeamHistory{" +
                "name='" + name + '\'' +
                ", previousTitles=" + previousTitles +
                ", previousAppearances=" + previousAppearances +
                ", previousFinals=" + previousFinals +
                ", previousSemifinals=" + previousSemifinals +
                '}';
    }
}
