package com.ciandt.paul;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration
 */
@Configuration
public class Config {

    private Integer worldCupYear = 2018;
    private Integer[] trainingYears = {2006, 2010, 2014};
    private String debug = "false";
    private String datasetBucket = "project-paul-the-octopus-datasets";
    private String predictionsFilename = "predictions.csv";
    private String predictionsBucketPrefix = "ciandt_projectoctopus_2018_";

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public boolean isDebugEnabled() {
        return "true".equals(this.getDebug());
    }

    public String getDatasetBucket() {
        return datasetBucket;
    }

    public Integer getWorldCupYear() {
        return worldCupYear;
    }

    public Integer[] getTrainingYears() {
        return trainingYears;
    }

    public String getPredictionsFilename() {
        return predictionsFilename;
    }

    public String getPredictionsBucketPrefix() {
        return predictionsBucketPrefix;
    }
}
