package com.jsoft.medpdfmaker.util;

import java.io.File;

/**
 * Utility methods related to file naming.
 */
public final class FileUtil {

    private FileUtil() {
        // no op
    }

    /**
     * Strip the last path separator from the input string if it is presented.
     * @param pathToProcess path to the folder that needs to be processed.
     * @return content of pathToProcess with last path separator symbol removed. If pathToProcess does not
     * have path separator symbol in the end, it will be removed unmodified.
     */
    public static String stripLastSlashIfNeeded(final String pathToProcess) {
        int lastCharIdx = pathToProcess.length() - 1;
        if (pathToProcess.charAt(lastCharIdx) == File.separatorChar) {
            return pathToProcess.substring(0, lastCharIdx);
        }
        return pathToProcess;
    }
}
