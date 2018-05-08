package com.ciandt.paul.dao;

public class DataNotAvailableException extends Exception {

    /**
     * Constructor
     */
    public DataNotAvailableException(String dataType, Integer year) {
        super("Data [" + dataType + "] not available for " + year);
    }
}
