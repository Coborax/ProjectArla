package com.redheads.arla.util.exceptions.persistence;

public class ExcelReadError extends Exception  {

    public ExcelReadError(String message) {
        super(message);
    }
    public ExcelReadError(String message, Throwable cause) {
        super(message, cause);
    }

}
