package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DecimalMoneyValueExtractorTest {

    private DataFormatter dataFormatterMock;
    private DecimalMoneyValueExtractor decimalMoneyValueExtractor;
    private Cell cellMock;

    @BeforeEach
    void setUp() {
        dataFormatterMock = mock(DataFormatter.class);
        decimalMoneyValueExtractor = new DecimalMoneyValueExtractor(dataFormatterMock);
        cellMock = mock(Cell.class);
    }

    @Test
    void invalidConstructorArgTest() {
        assertThrows(IllegalArgumentException.class, () -> new DecimalMoneyValueExtractor(null));
    }

    @Test
    void canParse() {
        assertEquals(FieldType.DECIMAL_MONEY, decimalMoneyValueExtractor.canParse());
    }

    @Test
    void extractValue() {
    }
}