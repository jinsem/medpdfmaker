package com.jsoft.medpdfmaker.util;

import java.io.File;

public final class FileUtil {

    private FileUtil() {
        // no op
    }

    public static String stripLastSlashIfNeeded(String configuredValue) {
        int lastCharIdx = configuredValue.length() - 1;
        if (configuredValue.charAt(lastCharIdx) == File.separatorChar) {
            return configuredValue.substring(0, lastCharIdx);
        }
        return configuredValue;
    }
}
