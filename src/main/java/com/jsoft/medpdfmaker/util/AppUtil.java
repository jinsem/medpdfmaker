package com.jsoft.medpdfmaker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Set of utility functions that do not belong to a specific category.
 */
public final class AppUtil {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm-ss");

    private AppUtil() {
    }

    /**
     * Get string representation of current date and time.
     * @return  string representation of current date and time.
     */
    public static String curDateTimeAsString() {
        return FORMAT.format(LocalDateTime.now());
    }
}
