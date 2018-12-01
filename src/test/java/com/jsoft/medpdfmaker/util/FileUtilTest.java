package com.jsoft.medpdfmaker.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilTest {

    @Test
    void stripLastSlashIfNeeded() {
        String testPath1 = Paths.get("home", "me", "test").toString();
        String testPath2 = Paths.get("home", "me2", "test").toString() + File .separator;
        String testPath2Expected = Paths.get("home", "me2", "test").toString();
        assertEquals(testPath1, FileUtil.stripLastSlashIfNeeded(testPath1));
        assertEquals(testPath2Expected, FileUtil.stripLastSlashIfNeeded(testPath2));
    }
}