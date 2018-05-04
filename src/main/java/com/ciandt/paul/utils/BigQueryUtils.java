package com.ciandt.paul.utils;

import com.ciandt.paul.GeneralConfig;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility class to handle Big Query operations
 */
@Service
public class BigQueryUtils {

    private static Logger logger = LoggerFactory.getLogger(BigQueryUtils.class.getName());

    @Autowired
    private GeneralConfig generalConfig;

    private static BigQuery bigQuery;
    private final static String CREDENTIALS_PATH = "project-paul-the-octopus-secret.json";

    public BigQuery getBigQuery() throws IOException {
        if (bigQuery != null) {
            return bigQuery;
        } else {
            GoogleCredentials credentials;
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(CREDENTIALS_PATH);
            //File credentialsPath = new File(CREDENTIALS_PATH);
            //FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);
            //credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
            credentials = ServiceAccountCredentials.fromStream(in);
            BigQuery bigquery = BigQueryOptions.newBuilder().setCredentials(credentials).build().getService();
            return bigquery;
        }
    }

    /**
     * Executes a query on BigQuery
     *
     * @param query Query to be executed
     * @return List of results as strings
     * @throws IOException
     * @throws InterruptedException
     */
    public List<List<String>> executeQuery(String query) throws IOException, InterruptedException {

        if (generalConfig.isDebugEnabled()) {
            logger.debug("Executing query on BigQuery: " + query);
        }

        BigQuery bigquery = this.getBigQuery();

        List<List<String>> result = new ArrayList<List<String>>();
        List<String> line = null;

        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                        query)
                        .setUseLegacySql(false)
                        .build();

        // Create a job ID so that we can safely retry.
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        com.google.cloud.bigquery.Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        // Wait for the query to complete.
        queryJob = queryJob.waitFor();

        // Check for errors
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }

        QueryResponse response = bigquery.getQueryResults(jobId);

        TableResult tableResult = queryJob.getQueryResults();

        // Print all pages of the results.
        for (FieldValueList row : tableResult.iterateAll()) {
            line = new ArrayList<String>();
            for (int i = 0; i < row.size(); i++) {
                line.add(row.get(i).getStringValue());
            }
            result.add(line);
        }

        if (generalConfig.isDebugEnabled()) {
            logger.debug("Returing: " + result.size());
        }

        return result;
    }

}

