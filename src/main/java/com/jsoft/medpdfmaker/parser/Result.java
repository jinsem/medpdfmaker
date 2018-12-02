package com.jsoft.medpdfmaker.parser;

/**
 * Enum represents possible file parsing result
 */
public enum Result {

    /**
     * Everything was OK. No errors, not warnings
     */
    OK(0),

    /**
     * Warnings were found, but processing went well
     */
    WARNING(1),

    /**
     * Some serious errors found, data cannot be passed to the next steps
     */
    ERROR(2);

    final int importance;

    Result(int importance) {
        this.importance = importance;
    }

    public int getImportance() {
        return importance;
    }

    public static Result moreImportant(Result curResult, Result newResult) {
        if (curResult.getImportance() == newResult.getImportance()) {
            return curResult;
        } else if (curResult.getImportance() < newResult.getImportance()) {
            return newResult;
        } else {
            return curResult;
        }
    }
}

