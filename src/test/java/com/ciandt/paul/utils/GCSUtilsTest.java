package com.ciandt.paul.utils;

import com.ciandt.paul.Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Test BigQuery services
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GCSUtils.class, Config.class})
public class GCSUtilsTest {

    @Autowired
    private GCSUtils gcsUtils;

    @Autowired
    private Config config;

    @Before
    public void setUp() {
        config.setDebug("true");
    }

    /**
     * Test the read method
     */
    @Test
    public void shouldReadFileFromGCS() throws Exception {
        String strContent = gcsUtils.readFile(config.getDatasetBucket(), "fifa_rank_2014.csv");
        assertNotNull(strContent);
    }
}
