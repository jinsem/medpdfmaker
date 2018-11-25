package com.jsoft.medpdfmaker.util;

import org.apache.poi.ss.usermodel.Cell;
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

    public static void logParsingError(Logger log, String description, Cell cell) {
        if (log.isErrorEnabled()) {
            final String message;
            if (cell == null) {
                message = String.format("Data parsing error: %s", description);
            } else {
                message = String.format("Data parsing error on sheet: [%s], row: [%d], cell: [%d]: %s",
                        cell.getSheet().getSheetName(), cell.getRowIndex(), cell.getColumnIndex(), description);
            }
            log.error(message);
        }
    }
}
