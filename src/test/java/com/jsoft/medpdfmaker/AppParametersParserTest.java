package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.exception.ParametersParsingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppParametersParserTest {

    private PrintStream originalOut;
    private PrintStream outMock;
    private AppParametersParser appParametersParser;

    @BeforeEach
    void setUp() {
        outMock = mock(PrintStream.class);
        originalOut = System.out;
        System.setOut(outMock);
        appParametersParser = new AppParametersParser();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void parseInvalidInputFileParameter() {
        // no input file name
        assertThrows(ParametersParsingException.class, () -> appParametersParser.parse());
        // 2 parameters
        assertThrows(ParametersParsingException.class, () -> appParametersParser.parse("file1", "file2"));
        // file does not exist
        assertThrows(ParametersParsingException.class, () -> appParametersParser.parse("/some/junk/file"));
    }

    @Test
    void printHelp() {
        appParametersParser.printHelp();
        final ArgumentCaptor<byte[]> byteCaptor = new ArgumentCaptor<>();
        verify(outMock, atLeast(1)).write(byteCaptor.capture(), any(Integer.class), any(Integer.class));
        final String printed = new String(byteCaptor.getValue());
        assertNotNull(printed);
        final String[] expectedElements = new String[]{
                AppParametersParser.HELP_OPTION_FULL,
                AppParametersParser.OUTPUT_FOLDER_OPTION_FULL,
                AppParametersParser.INPUT_FILE_SHEETS_TO_PROCESS_FULL
        };
        for (String expectedElement : expectedElements) {
            assertTrue(printed.contains("--" + expectedElement));
        }
    }
}