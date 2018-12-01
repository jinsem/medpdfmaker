package com.jsoft.medpdfmaker.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilTest {

    @Test
    void stripLastSlashIfNeeded() {
        String testPath1 = "home/me/test";
        String testPath2 = "home/me2/test/";
        String testPath2Expected = "home/me2/test";
        assertEquals(testPath1, FileUtil.stripLastSlashIfNeeded(testPath1));
        assertEquals(testPath2Expected, FileUtil.stripLastSlashIfNeeded(testPath2));
    }
}