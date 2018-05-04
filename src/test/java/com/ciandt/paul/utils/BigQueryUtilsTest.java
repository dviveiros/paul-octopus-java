package com.ciandt.paul.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test BigQuery services
 */
public class BigQueryUtilsTest {

    private BigQueryUtils bigQueryUtils = new BigQueryUtils();

    /**
     * Test the query method
     */
    @Test
    public void shouldQueryDataFromBQ() throws Exception {
        List<List<String>> result = bigQueryUtils.executeQuery("SELECT * FROM paul_the_octopus_dataset.fifa_rank LIMIT 10");
        assertNotNull(result);
        assertEquals(10, result.size());
    }
}
