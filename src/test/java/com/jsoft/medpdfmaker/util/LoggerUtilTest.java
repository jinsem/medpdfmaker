package com.jsoft.medpdfmaker.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoggerUtilTest {

    @Mock
    private Logger loggerMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void warn() {
        when(loggerMock.isWarnEnabled()).thenReturn(false);
        final String msg = "my message";
        LoggerUtil.warn(loggerMock, msg);
        verify(loggerMock, times(1)).isWarnEnabled();
        verify(loggerMock, times(0)).warn(any());
        when(loggerMock.isWarnEnabled()).thenReturn(true);
        LoggerUtil.warn(loggerMock, msg);
        verify(loggerMock, times(2)).isWarnEnabled();
        verify(loggerMock, times(1)).warn(msg);
    }

    @Test
    void logParsingError() {
        when(loggerMock.isErrorEnabled()).thenReturn(false);
        final String msg = "my message";
        LoggerUtil.logParsingError(loggerMock, msg, null);
        verify(loggerMock, times(1)).isErrorEnabled();
        verify(loggerMock, times(0)).error(any());

        when(loggerMock.isErrorEnabled()).thenReturn(true);
        final ArgumentCaptor<String> nullLogArgument = ArgumentCaptor.forClass(String.class);
        LoggerUtil.logParsingError(loggerMock, msg, null);
        verify(loggerMock, times(1)).error(nullLogArgument.capture());
        assertTrue(nullLogArgument.getValue().contains(msg));

        final Cell cellMock = mock(Cell.class);
        final Sheet sheetMock = mock(Sheet.class);
        when(sheetMock.getSheetName()).thenReturn("A name");
        when(cellMock.getSheet()).thenReturn(sheetMock);
        when(cellMock.getRowIndex()).thenReturn(100);
        when(cellMock.getColumnIndex()).thenReturn(200);

        LoggerUtil.logParsingError(loggerMock, msg, cellMock);
        final ArgumentCaptor<String> notNullLogArgument = ArgumentCaptor.forClass(String.class);
        verify(loggerMock, times(3)).isErrorEnabled();
        verify(loggerMock, times(2)).error(notNullLogArgument.capture());
        final String argValue = notNullLogArgument.getValue();
        assertNotNull(argValue);
        assertTrue(argValue.contains(msg));
        assertTrue(argValue.contains("sheet: [A name]"));
        assertTrue(argValue.contains("row: [100]"));
        assertTrue(argValue.contains("cell: [200]"));
    }
}