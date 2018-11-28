package com.jsoft.medpdfmaker.parser;

/**
 * Enum represents possible file parsing result
 */
public enum ParsingResult {

    /**
     * Everything was OK. No errors, not warnings
     */
    OK(0),

    /**
     * Warnings were found, but processing went well
     */
    WARNING(1),

    /**
     * Some serious errors found, data cannot be passed ot the next steps
     */
    ERROR(2);

    final int importance;

    ParsingResult(int importance) {
        this.importance = importance;
    }

    public int getImportance() {
        return importance;
    }

    public static ParsingResult moreImportant(ParsingResult curResult, ParsingResult newResult) {
        if (curResult.getImportance() == newResult.getImportance()) {
            return curResult;
        } else if (curResult.getImportance() < newResult.getImportance()) {
            return newResult;
        } else {
            return curResult;
        }
    }
}

