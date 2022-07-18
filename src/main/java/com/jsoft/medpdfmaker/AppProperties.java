package com.jsoft.medpdfmaker;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AppProperties {

    public static final String PLACE_OF_SERVICE_PROP = "PlaceOfService";
    public static final String PROCEDURES_PROP = "Procedures";
    public static final String CHARGES_PROP = "Charges";
    public static final String FEDERAL_TAX_ID_PROP = "FederalTaxID";
    public static final String PROVIDER_PROP = "Provider";
    public static final String MAX_PAGES_IN_PDF_FILE = "MaxPagesInPdfFile";
    public static final String WORK_DAY_START = "WorkDayStart";
    public static final String WORK_DAY_END = "WorkDayEnd";
    public static final String HOSPITAL_ADDRESSES = "HospitalAddresses";

    private final String placeOfService;
    private final String procedures;
    private final BigDecimal charges;
    private final String federalTaxID;
    private final String provider;
    private final int maxPagesInPdfFile;
    private final LocalTime workDayStart;
    private final LocalTime workDayEnd;
    private final Set<String> hospitalAddresses = new HashSet<>();

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
        workDayStart = fetchTime(environment.getProperty(WORK_DAY_START), WORK_DAY_START);
        workDayEnd = fetchTime(environment.getProperty(WORK_DAY_END), WORK_DAY_END);
        String hospitalAddressesStr = environment.getProperty(HOSPITAL_ADDRESSES, "");
        hospitalAddresses.addAll(
                Arrays.asList(hospitalAddressesStr.split("\n"))
        );
    }

    private BigDecimal fetchCharges(String chargesStr) {
        BigDecimal result;
        if (StringUtils.isBlank(chargesStr)) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value must be set");
        }
        try {
            result = new BigDecimal(chargesStr).setScale(2, RoundingMode.UP);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(CHARGES_PROP + " property value is not a number");
        }
        if (result.compareTo(BigDecimal.ZERO) <= 0) {
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

    private LocalTime fetchTime(String timeString, String propertyName) {
        try {
            return LocalTime.parse(timeString, DateTimeFormatter.ISO_LOCAL_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException(propertyName + " property value is invalid, time string was expected: " + timeString);
        }
    }

    public String getPlaceOfService() {
        return placeOfService;
    }

    public String getProcedures() {
        return procedures;
    }

    public BigDecimal getCharges() {
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

    public LocalTime getWorkDayStart() {
        return workDayStart;
    }

    public LocalTime getWorkDayEnd() {
        return workDayEnd;
    }

    public Set<String> getHospitalAddresses() {
        return hospitalAddresses;
    }
}

