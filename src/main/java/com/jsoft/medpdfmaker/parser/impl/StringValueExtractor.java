package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

/**
 * Extract string value from the cell.
 */
public class StringValueExtractor implements ValueExtractor<String> {

    private final DataFormatter formatter = new DataFormatter();

    @Override
    public FieldType canParse() {
        return FieldType.STRING;
    }

    @Override
    public String extractValue(Cell cell) {
        return formatter.formatCellValue(cell);
    }
}
