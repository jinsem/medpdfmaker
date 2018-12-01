package com.jsoft.medpdfmaker;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppPropertiesTest {

    @Test
    void createValidation() {
        assertThrows(IllegalArgumentException.class, () -> new AppProperties(null));
        final Environment environmentMock = mock(Environment.class);
        assertThrows(IllegalArgumentException.class, () -> new AppProperties(environmentMock));
        when(environmentMock.getProperty(Constants.CHARGES_PROP)).thenReturn(null, "not a number", "-1", "0.0", "0");
        for (int i=0; i<5; i++) {
            assertThrows(IllegalArgumentException.class, () -> new AppProperties(environmentMock));
        }
    }

    @Test
    void getParameters() {
        final Environment environmentMock = mock(Environment.class);
        String placeOfService = "Place of service";
        final String procedures = "Procedures";
        final String taxId = "Tax ID";
        final String provider = "Provider";
        when(environmentMock.getProperty(Constants.PLACE_OF_SERVICE_PROP)).thenReturn(placeOfService);
        when(environmentMock.getProperty(Constants.PROCEDURES_PROP)).thenReturn(procedures);
        when(environmentMock.getProperty(Constants.CHARGES_PROP)).thenReturn("50.0");
        when(environmentMock.getProperty(Constants.FEDERAL_TAX_ID_PROP)).thenReturn(taxId);
        when(environmentMock.getProperty(Constants.PROVIDER_PROP)).thenReturn(provider);
        final AppProperties appProperties = new AppProperties(environmentMock);
        assertEquals(placeOfService, appProperties.getPlaceOfService());
        assertEquals(procedures, appProperties.getProcedures());
        assertEquals(taxId, appProperties.getFederalTaxID());
        assertEquals(provider, appProperties.getProvider());
        assertEquals(50.0, appProperties.getCharges(), 0.01);
    }
}