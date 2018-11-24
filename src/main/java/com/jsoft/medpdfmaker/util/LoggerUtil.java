package com.jsoft.medpdfmaker.util;

import org.slf4j.Logger;

public final class LoggerUtil {

    private LoggerUtil() {
        // no op
    }

    public static void warn(Logger log, String message) {
        if (log.isWarnEnabled()) {
            log.warn(message);
        }
    }
}
