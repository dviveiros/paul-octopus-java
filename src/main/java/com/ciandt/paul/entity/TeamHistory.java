package com.ciandt.paul.entity;

import org.apache.commons.csv.CSVRecord;

import java.util.List;

public class TeamHistory {

    private String name;
    private Integer previousTitles;
    private Integer previousAppearances;
    private Integer previousFinals;
    private Integer previousSemifinals;

    /**
     * Constructor
     */
    public TeamHistory(List<String> row) {
        this.setName(row.get(0));
        this.setPreviousAppearances(Integer.parseInt(row.get(1)));
        this.setPreviousTitles(Integer.parseInt(row.get(2)));
        this.setPreviousFinals(Integer.parseInt(row.get(3)));
        this.setPreviousSemifinals(Integer.parseInt(row.get(4)));
    }

    /**
     * Constructor
     */
    public TeamHistory(CSVRecord record) {
        this.setName(record.get(0));
        this.setPreviousAppearances(Integer.parseInt(record.get(1)));
        this.setPreviousTitles(Integer.parseInt(record.get(2)));
        this.setPreviousFinals(Integer.parseInt(record.get(3)));
        this.setPreviousSemifinals(Integer.parseInt(record.get(4)));
    }

    public String getName() {
        return name;
    }

    public Integer getPreviousTitles() {
        return previousTitles;
    }

    public Integer getPreviousAppearances() {
        return previousAppearances;
    }

    public Integer getPreviousFinals() {
        return previousFinals;
    }

    public Integer getPreviousSemifinals() {
        return previousSemifinals;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setPreviousTitles(Integer previousTitles) {
        this.previousTitles = previousTitles;
    }

    private void setPreviousAppearances(Integer previousAppearances) {
        this.previousAppearances = previousAppearances;
    }

    private void setPreviousFinals(Integer previousFinals) {
        this.previousFinals = previousFinals;
    }

    private void setPreviousSemifinals(Integer previousSemifinals) {
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
