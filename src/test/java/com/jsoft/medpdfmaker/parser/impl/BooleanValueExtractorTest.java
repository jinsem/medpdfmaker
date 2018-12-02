package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.poi.ss.usermodel.CellType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BooleanValueExtractorTest {

    private BooleanValueExtractor booleanValueExtractor;
    private Cell cellMock;

    @BeforeEach
    void setUp() {
        booleanValueExtractor = new BooleanValueExtractor();
        cellMock = mock(Cell.class);
    }

    @Test
    void canParse() {
        assertEquals(FieldType.BOOLEAN, booleanValueExtractor.canParse());
    }

    @Test
    void extractValueBlank() {
        when(cellMock.getCellType()).thenReturn(CellType.BLANK);
        assertFalse(booleanValueExtractor.extractValue(cellMock));
    }

    @Test
    void extractValueBoolean() {
        when(cellMock.getCellType()).thenReturn(CellType.BOOLEAN);
        when(cellMock.getBooleanCellValue()).thenReturn(true);
        assertTrue(booleanValueExtractor.extractValue(cellMock));
    }

    @Test
    void extractValueNumeric() {
        when(cellMock.getCellType()).thenReturn(CellType.NUMERIC);
        when(cellMock.getNumericCellValue()).thenReturn(0.0);
        assertFalse(booleanValueExtractor.extractValue(cellMock));
        when(cellMock.getNumericCellValue()).thenReturn(10.0);
        assertTrue(booleanValueExtractor.extractValue(cellMock));
        when(cellMock.getNumericCellValue()).thenReturn(-3000.0);
        assertTrue(booleanValueExtractor.extractValue(cellMock));
    }

    @Test
    void extractValueString() {
        String[] values = new String[]{
                "YES", "CANCELLED", "YES(MUST)", "yes", "cancelled", "yes(must)",
                "YeS", "CanCElleD", "YES(must)", "Y ES", "YES BUT LONGER",
                "", " ", "junk", "null"
        };
        boolean[] expected = new boolean[]{
                true, true, true, true, true, true,
                true, true, true, true, true,
                false, false, false, false
        };
        when(cellMock.getCellType()).thenReturn(CellType.STRING);
        for (int i = 0; i < values.length; i++) {
            when(cellMock.getStringCellValue()).thenReturn(values[i]);
            assertEquals(expected[i], booleanValueExtractor.extractValue(cellMock), "Value: " + values[i]);
        }
    }

    @Test
    void extractValueError() {
        CellType[] notSupported = new CellType[]{_NONE, ERROR, FORMULA};
        for (CellType ns : notSupported) {
            when(cellMock.getCellType()).thenReturn(ns);
            assertFalse(booleanValueExtractor.extractValue(cellMock));
        }
    }
}