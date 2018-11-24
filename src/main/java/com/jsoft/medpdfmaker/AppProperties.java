package com.jsoft.medpdfmaker;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;

public class AppProperties {

    private static final String PLACE_OF_SERVICE_PROP = "PlaceOfService";
    private static final String PROCEDURES_PROP = "Procedures";
    private static final String CHARGES_PROP = "Charges";
    private static final String FEDERAL_TAX_ID_PROP = "FederalTaxID";
    private static final String PROVIDER_PROP = "Provider";

    private final String placeOfService;
    private final String procedures;
    private final double charges;
    private final String federalTaxID;
    private final String provider;

    public AppProperties(final Environment environment) {
        if (environment == null) {
            throw new IllegalArgumentException("environment must not be null");
        }
        placeOfService = environment.getProperty(PLACE_OF_SERVICE_PROP);
        procedures = environment.getProperty(PROCEDURES_PROP);
        String chargesStr = environment.getProperty(CHARGES_PROP);
        if (StringUtils.isBlank(chargesStr)) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value must be set");
        }
        try {
            charges = Double.valueOf(chargesStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value is not a number");
        }
        federalTaxID = environment.getProperty(FEDERAL_TAX_ID_PROP);
        provider = environment.getProperty(PROVIDER_PROP);
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

