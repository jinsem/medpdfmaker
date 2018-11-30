package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalTimeValueExtractorTest {

    private LocalTimeValueExtractor localTimeValueExtractor;
    private Cell cellMock;

    @BeforeEach
    void setUp() {
        localTimeValueExtractor = new LocalTimeValueExtractor();
        cellMock = mock(Cell.class);
    }

    @Test
    void canParse() {
        assertEquals(FieldType.TIME, localTimeValueExtractor.canParse());
    }

    @Test
    void extractValueNumeric() {
    }

    @Test
    void extractValueBlank() {
        when(cellMock.getCellType()).thenReturn(CellType.BLANK);
        assertNull(localTimeValueExtractor.extractValue(cellMock));
    }

    @Test
    void extractValueString() {
    }

    @Test
    void extractValueError() {
    }
}