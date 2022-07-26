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

    /**
     * Split string into equal chunks by count of chars.
     * @param str String to split
     * @param charCount number of chars in one chunk
     * @return array with string parts
     */
    public static String[] splitByCharCount(String str, int charCount) {
        return (charCount<1 || str==null) ? null : str.split("(?<=\\G.{" + charCount + "})");
    }    
}
