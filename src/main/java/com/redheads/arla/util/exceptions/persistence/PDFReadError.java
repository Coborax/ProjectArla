package com.redheads.arla.util.exceptions.persistence;

public class PDFReadError extends Exception {
    public PDFReadError(String message) {
        super(message);
    }
    public PDFReadError(String message, Throwable cause) {
        super(message, cause);
    }
}
