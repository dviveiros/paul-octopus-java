package com.ciandt.paul.utils;

import com.ciandt.paul.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test BigQuery services
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BigQueryUtils.class, Config.class})
public class BigQueryUtilsTest {

    @Autowired
    private BigQueryUtils bigQueryUtils;

    @Autowired
    private Config config;

    @Before
    public void setUp() {
        config.setDebug("true");
    }

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
