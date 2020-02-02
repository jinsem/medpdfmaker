package com.jsoft.medpdfmaker.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;

/**
 * Utility methods to simplify logging.
 */
public final class LoggerUtil {

    private LoggerUtil() {
        // no op
    }

    /**
     * Add warning to the log. Warn message will be added only if warn is enabled for the provided logger.
     * @param log Logger instance to use.
     * @param message actual log message.
     */
    public static void warn(Logger log, String message) {
        if (log.isWarnEnabled()) {
            log.warn(message);
        }
    }

    public static void info(Logger log, String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }

    /**
     * Print data parsing detailed error.
     * @param log Logger instance to use
     * @param description short description of the problem.
     * @param cell Cell reference that contains data that cannot be parsed. If this value is null, function just ignores it.
     */
    public static void logCellParsingError(Logger log, String description, Cell cell) {
        if (log.isErrorEnabled()) {
            final String message;
            if (cell == null) {
                message = String.format("Data parsing error: %s", description);
            } else {
                message = String.format("Data parsing error on sheet: [%s], row: [%d], cell: [%d]: %s",
                        cell.getSheet().getSheetName(), cell.getRowIndex()+1, cell.getColumnIndex()+1, description);
            }
            log.error(message);
        }
    }

    public static void logRowParsingError(Logger log, String description, Row row) {
        if (log.isErrorEnabled()) {
            final String message;
            if (row == null) {
                message = String.format("Data parsing error: %s", description);
            } else {
                message = String.format("Data parsing error on sheet: [%s], row: [%d]: %s",
                        row.getSheet().getSheetName(), row.getRowNum(), description);
            }
            log.error(message);
        }
    }
}
