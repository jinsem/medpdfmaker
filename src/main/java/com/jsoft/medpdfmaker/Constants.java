package com.jsoft.medpdfmaker;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constants {

    private Constants() {
        //no-op
    }
    
    public static final Path TMP_FOLDER_PATH = Paths.get(System.getProperty("java.io.tmpdir"));
    public static final String WORK_FOLDER_PREF = "medpdfmaker";

    public static final String PDF_TEMPLATE_RESOURCE_PATH = "template/Form.pdf";
}