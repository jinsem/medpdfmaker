package com.jsoft.medpdfmaker;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constants {

    private Constants() {
        //no-op
    }
    
    public static final Path TMP_FOLDER_PATH = Paths.get(System.getProperty("java.io.tmpdir"));
    public static final String WORK_FOLDER_PREF = "medpdfmaker";

    public static final String PDF_TEMPLATE_RESOURCE_PATH = "template/Form.pdf";

    public static final String PLACE_OF_SERVICE_PROP = "PlaceOfService";
    public static final String PROCEDURES_PROP = "Procedures";
    public static final String CHARGES_PROP = "Charges";
    public static final String FEDERAL_TAX_ID_PROP = "FederalTaxID";
    public static final String PROVIDER_PROP = "Provider";
}