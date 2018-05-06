package com.ciandt.paul;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration
 */
@Configuration
public class Config {

    private String debug = "false";
    private String datasetBucket = "project-paul-the-octopus-datasets";

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

    public void setDatasetBucket(String datasetBucket) {
        this.datasetBucket = datasetBucket;
    }
}
