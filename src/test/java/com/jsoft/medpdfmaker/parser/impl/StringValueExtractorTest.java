package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StringValueExtractorTest {

    private StringValueExtractor stringValueExtractor;
    private DataFormatter dataFormatterMock;
    private Cell cellMock;

    @BeforeEach
    void setUp() {
        dataFormatterMock = mock(DataFormatter.class);
        stringValueExtractor = new StringValueExtractor(dataFormatterMock);
        cellMock = mock(Cell.class);
    }

    @Test
    void createValidation() {
        assertThrows(IllegalArgumentException.class, () -> new StringValueExtractor(null));
    }

    @Test
    void canParse() {
        assertEquals(FieldType.STRING, stringValueExtractor.canParse());
    }

    @Test
    void extractValue() {
        when(dataFormatterMock.formatCellValue(cellMock)).thenReturn(null);
        assertNull(stringValueExtractor.extractValue(cellMock));

        when(dataFormatterMock.formatCellValue(cellMock)).thenReturn("");
        assertNull(stringValueExtractor.extractValue(cellMock));

        when(dataFormatterMock.formatCellValue(cellMock)).thenReturn("xyz");
        assertEquals("xyz", stringValueExtractor.extractValue(cellMock));
    }
}