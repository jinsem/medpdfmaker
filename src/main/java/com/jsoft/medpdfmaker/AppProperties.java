package com.jsoft.medpdfmaker;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

public class AppProperties {

    public static final String PLACE_OF_SERVICE_PROP = "PlaceOfService";
    public static final String PROCEDURES_PROP = "Procedures";
    public static final String CHARGES_PROP = "Charges";
    public static final String FEDERAL_TAX_ID_PROP = "FederalTaxID";
    public static final String PROVIDER_PROP = "Provider";
    public static final String MAX_PAGES_IN_PDF_FILE = "MaxPagesInPdfFile";

    private final String placeOfService;
    private final String procedures;
    private final double charges;
    private final String federalTaxID;
    private final String provider;
    private final int maxPagesInPdfFile;

    public AppProperties(final Environment environment) {
        if (environment == null) {
            throw new IllegalArgumentException("environment must not be null");
        }
        placeOfService = environment.getProperty(PLACE_OF_SERVICE_PROP);
        procedures = environment.getProperty(PROCEDURES_PROP);
        final String chargesStr = environment.getProperty(CHARGES_PROP);
        charges = fetchCharges(chargesStr);
        federalTaxID = environment.getProperty(FEDERAL_TAX_ID_PROP);
        provider = environment.getProperty(PROVIDER_PROP);
        final String strMaxPagesInPdfFile = environment.getProperty(MAX_PAGES_IN_PDF_FILE);
        maxPagesInPdfFile = fetchMaxPagesInPdfFile(strMaxPagesInPdfFile);
    }

    private double fetchCharges(String chargesStr) {
        double result;
        if (StringUtils.isBlank(chargesStr)) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value must be set");
        }
        try {
            result = Double.valueOf(chargesStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value is not a number");
        }
        if (result <= 0) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value must be integer or decimal value greater than zero");
        }
        return result;
    }

    private int fetchMaxPagesInPdfFile(String strMaxPagesInPdfFile) {
        int result = Integer.MAX_VALUE;
        if (StringUtils.isBlank(strMaxPagesInPdfFile)) {
            return result;
        }
        try {
            int tmpResult = Integer.parseInt(strMaxPagesInPdfFile);
            if (tmpResult > 0) {
                result = tmpResult;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MAX_PAGES_IN_PDF_FILE + " property value is not an integer number");
        }
        return result;
    }

    public String getPlaceOfService() {
        return placeOfService;
    }

    public String getProcedures() {
        return procedures;
    }

    public double getCharges() {
        return charges;
    }

    public String getFederalTaxID() {
        return federalTaxID;
    }

    public String getProvider() {
        return provider;
    }

    public int getMaxPagesInPdfFile() {
        return maxPagesInPdfFile;
    }

    public boolean isCompositePdfEnabled() {
        return maxPagesInPdfFile != 1;
    }
}

