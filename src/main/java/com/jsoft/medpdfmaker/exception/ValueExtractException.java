package com.jsoft.medpdfmaker.exception;

import org.apache.poi.ss.usermodel.Cell;

public class ValueExtractException extends AppException {

    private final Cell cell;

    public ValueExtractException(Cell cell) {
        this.cell = cell;
    }

    public ValueExtractException(String message, Cell cell) {
        super(message);
        this.cell = cell;
    }

    public ValueExtractException(String message, Cell cell, Throwable cause) {
        super(message, cause);
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }
}
