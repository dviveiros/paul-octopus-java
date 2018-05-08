package com.ciandt.paul.utils;

import com.ciandt.paul.Config;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Utility class to handle GCS operations
 */
@Service
public class GCSUtils {

    private static Logger logger = LoggerFactory.getLogger(GCSUtils.class.getName());

    @Autowired
    private Config config;

    private static Storage storage;
    private final static String CREDENTIALS_PATH = "project-paul-the-octopus-secret.json";

    /**
     * Read the content of a text file from GCS
     */
    public String readFile(String bucket, String filename) throws IOException {
        if (config.isDebugEnabled()) {
            logger.debug("Reading file from GCS... Bucket name = " + bucket + ", filename = " + filename);
        }
        Blob blob = getStorage().get(bucket, filename);
        return new String(blob.getContent());
    }

    /**
     * Creates an instance of GCS service
     *
     * @return Instance of GCS service
     * @throws IOException
     */
    private Storage getStorage() throws IOException {
        if (storage != null) {
            return storage;
        } else {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            return storage;
        }
    }


}
