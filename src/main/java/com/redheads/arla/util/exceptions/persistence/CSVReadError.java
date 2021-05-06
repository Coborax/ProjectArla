package com.redheads.arla.util.exceptions.persistence;

public class CSVReadError extends Exception  {

    public CSVReadError(String message) {
        super(message);
    }
    public CSVReadError(String message, Throwable cause) {
        super(message, cause);
    }

}
