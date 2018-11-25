package com.jsoft.medpdfmaker;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;

import static com.jsoft.medpdfmaker.Constants.*;

public class AppProperties {

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
        final String chargesStr = environment.getProperty(CHARGES_PROP);
        charges = fetchCharges(chargesStr);
        federalTaxID = environment.getProperty(FEDERAL_TAX_ID_PROP);
        provider = environment.getProperty(PROVIDER_PROP);
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

