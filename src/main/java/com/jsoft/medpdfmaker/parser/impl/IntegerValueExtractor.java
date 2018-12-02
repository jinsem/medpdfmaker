package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class IntegerValueExtractor implements ValueExtractor<Integer> {

    private final DataFormatter formatter;

    public IntegerValueExtractor(DataFormatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("formatter cannot be null");
        }
        this.formatter = formatter;
    }

    @Override
    public FieldType canParse() {
        return FieldType.INTEGER;
    }

    @Override
    public Integer extractValue(Cell cell) {
        Integer result;
        switch (cell.getCellType()) {
            case NUMERIC:
                result = fetchInt(cell.getNumericCellValue());
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

    private int fetchInt(Double cellValue) {
        return (int)Math.round(cellValue);
    }

    private Integer getIntegerFromString(Cell cell) {
        final String strVal = formatter.formatCellValue(cell);
        try {
            return StringUtils.isEmpty(strVal) ? null : fetchInt(Double.valueOf(strVal.trim()));
        } catch (NumberFormatException e) {
            throw new ValueExtractException(String.format("Unable to parse string value %s to integer", strVal), cell);
        }
    }
}
