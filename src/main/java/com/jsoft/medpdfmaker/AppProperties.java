package com.jsoft.medpdfmaker;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;

public class AppProperties {

    private static final String PLACE_OF_SERVICE = "PlaceOfService";
    private static final String PROCEDURES = "Procedures";
    private static final String CHARGES = "Charges";
    private static final String FEDERAL_TAX_ID = "FederalTaxID";
    private static final String PROVIDER = "Provider";

    private final String placeOfService;
    private final String procedures;
    private final double charges;
    private final String federalTaxID;
    private final String provider;

    public AppProperties(final Environment environment) {
        if (environment == null) {
            throw new IllegalArgumentException("environment must not be null");
        }
        placeOfService = environment.getProperty(PLACE_OF_SERVICE);
        procedures = environment.getProperty(PROCEDURES);
        String chargesStr = environment.getProperty(CHARGES);
        if (StringUtils.isBlank(chargesStr)) {
            throw new IllegalArgumentException(CHARGES + " property value must be set");
        }
        try {
            charges = Double.valueOf(chargesStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(CHARGES + " property value is not a number");
        }
        federalTaxID = environment.getProperty(FEDERAL_TAX_ID);
        provider = environment.getProperty(PROVIDER);
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
}

