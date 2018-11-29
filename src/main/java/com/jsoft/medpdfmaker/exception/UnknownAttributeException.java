package com.jsoft.medpdfmaker.exception;

public class UnknownAttributeException extends AppException {

    public UnknownAttributeException() {
        super();
    }

    public UnknownAttributeException(String message) {
        super(message);
    }

    public UnknownAttributeException(String message, Throwable cause) {
        super(message, cause);
    }
}
