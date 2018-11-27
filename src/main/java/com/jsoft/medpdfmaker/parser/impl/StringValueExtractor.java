package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class StringValueExtractor implements ValueExtractor<String> {

    @Override
    public FieldType canParse() {
        return FieldType.STRING;
    }

    @Override
    public String extractValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String result;
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            result = String.valueOf(cell.getNumericCellValue());
        } else {
            result = cell.getStringCellValue();
        }
        return result;
    }
}
