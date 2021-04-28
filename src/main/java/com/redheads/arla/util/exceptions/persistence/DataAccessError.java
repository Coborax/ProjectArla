package com.redheads.arla.util.exceptions.persistence;

public class DataAccessError extends Exception {

    public DataAccessError(String message) {
        super(message);
    }

    public DataAccessError(String message, Throwable cause) {
        super(message, cause);
    }
}
