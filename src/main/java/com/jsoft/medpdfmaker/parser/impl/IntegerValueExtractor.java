package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class IntegerValueExtractor implements ValueExtractor<Integer> {

    @Override
    public FieldType canParse() {
        return FieldType.INTEGER;
    }

    @Override
    public Integer extractValue(Cell cell) {
        Integer result;
        switch (cell.getCellType()) {
            case NUMERIC:
                result = (int)Math.round(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue() ? 1 : 0;
                break;
            case STRING:
                result = getIntegerFromString(cell);
                break;
            default:
                result = null;
        }
        return result;
    }

    private Integer getIntegerFromString(Cell cell) {
        final String strVal = cell.getStringCellValue();
        try {
            return StringUtils.isEmpty(strVal) ? null : Integer.valueOf(strVal.trim());
        } catch (NumberFormatException e) {
            throw new ValueExtractException(String.format("Unable to parse string value %s to integer", strVal), cell);
        }
    }
}
