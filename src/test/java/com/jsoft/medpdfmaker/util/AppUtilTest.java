package com.jsoft.medpdfmaker.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppUtilTest {

    @Test
    void curDateTimeAsString() {
        final LocalDate dateBefore = LocalDate.now();
        final String dateStr = AppUtil.curDateTimeAsString();
        final LocalDate dateAfter = LocalDate.now();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm-ss");
        final LocalDate dateFromStr = LocalDate.parse(dateStr, fmt);
        // This is needed to avoid assert error if test is executed at midnight
        assertTrue(dateBefore.isBefore(dateFromStr) || dateBefore.equals(dateFromStr));
        assertTrue(dateAfter.isAfter(dateFromStr) || dateAfter.equals(dateFromStr));
    }

    @Test
    void splitByCharCount() {
        Assertions.assertTrue(Arrays.equals(AppUtil.splitByCharCount("ABC", 1), 
            new String[]{"A", "B", "C"}));
        Assertions.assertTrue(Arrays.equals(AppUtil.splitByCharCount("ABC", 2), 
            new String[]{"AB", "C"}));
    }
}