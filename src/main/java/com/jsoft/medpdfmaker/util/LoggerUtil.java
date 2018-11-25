package com.jsoft.medpdfmaker.util;

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
}
