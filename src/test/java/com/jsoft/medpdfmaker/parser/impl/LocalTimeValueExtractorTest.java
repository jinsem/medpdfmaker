package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.*;
import static org.apache.poi.ss.usermodel.DateUtil.getJavaDate;
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
    void extractValueDate() {
        // 41918.01 -> 10/6/2014 0:14:24
        double input = 41918.01;
        final Date expected = getJavaDate(input);
        when(cellMock.getCellType()).thenReturn(NUMERIC);
        CellStyle styleMock = mock(CellStyle.class);
        when(styleMock.getDataFormat()).thenReturn((short)15);
        when(cellMock.getCellStyle()).thenReturn(styleMock);
        when(cellMock.getNumericCellValue()).thenReturn(input);
        when(cellMock.getDateCellValue()).thenReturn(expected);
        LocalTime result = localTimeValueExtractor.extractValue(cellMock);
        assertNotNull(result);
    }


    @Test
    void extractValueNumeric() {
        when(cellMock.getCellType()).thenReturn(NUMERIC);
        // 41918.01 -> 10/6/2014 0:14:24
        when(cellMock.getNumericCellValue()).thenReturn(41918.01);
        LocalTime result = localTimeValueExtractor.extractValue(cellMock);
        assertNotNull(result);
        assertEquals(0, result.getHour());
        assertEquals(14, result.getMinute());
        assertEquals(24, result.getSecond());
    }


    @Test
    void extractValueBlank() {
        when(cellMock.getCellType()).thenReturn(CellType.BLANK);
        assertNull(localTimeValueExtractor.extractValue(cellMock));
    }

    @Test
    void extractValueStringValid() {
        when(cellMock.getCellType()).thenReturn(STRING);
        // +2 and +9 just to minimize number of steps.
        for (int h = 0; h < 24; h+=2) {
            for (int m = 0; m < 60; m+=9) {
                List<String> timeParts = new ArrayList<> (
                    Arrays.asList(
                        String.format("%d:%d", h, m),
                        String.format("%02d:%d", h, m),
                        String.format("%d:%02d", h, m),
                        String.format("%02d:%02d", h, m)
                    )
                );
                if (h < 13) {
                    timeParts.add(String.format("%d:%dAM", h, m));
                    timeParts.add(String.format("%d:%dam", h, m));
                    timeParts.add(String.format("%d:%d am", h, m));
                    timeParts.add(String.format("%d:%d   am", h, m));
                    timeParts.add(String.format("%d:%dpm", h, m));
                }
                for (String timePart : timeParts) {
                    when(cellMock.getStringCellValue()).thenReturn(timePart);
                    LocalTime result = localTimeValueExtractor.extractValue(cellMock);
                    final int expH;
                    if (timePart.toUpperCase().contains("PM")) {
                        expH = (h == 12) ? h : (h + 12);
                    } else {
                        expH = (timePart.toUpperCase().contains("AM") && h == 12) ? 0 : h;
                    }
                    assertEquals(expH, result.getHour(), "Convert from " + timePart);
                    assertEquals(m, result.getMinute(), "Convert from " + timePart);
                }
            }
        }
    }

    @Test
    void extractValueStringInvalid() {
        String[] invalidTimes = new String[]{"am", "25:01", "13:00 pm", "11:00 рm"};
        when(cellMock.getCellType()).thenReturn(STRING);
        for (String invalidTime : invalidTimes) {
            when(cellMock.getStringCellValue()).thenReturn(invalidTime);
            assertThrows(ValueExtractException.class, () -> localTimeValueExtractor.extractValue(cellMock));
        }
    }

    @Test
    void extractValueError() {
        CellType[] notSupported = new CellType[]{_NONE, ERROR, BOOLEAN, FORMULA};
        for (CellType ns : notSupported) {
            when(cellMock.getCellType()).thenReturn(ns);
            assertThrows(ValueExtractException.class, () -> localTimeValueExtractor.extractValue(cellMock));
        }
    }
}