package com.jsoft.medpdfmaker.exception;

public class ParametersParsingException extends AppException {

    public ParametersParsingException() {
        super();
    }

    public ParametersParsingException(String message) {
        super(message);
    }

    public ParametersParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
