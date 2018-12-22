package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalMoneyValueExtractor implements ValueExtractor<BigDecimal> {

    private final DataFormatter formatter;

    public DecimalMoneyValueExtractor(DataFormatter formatter) {
        if (formatter == null) {
            throw new IllegalArgumentException("formatter cannot be null");
        }
        this.formatter = formatter;
    }

    @Override
    public FieldType canParse() {
        return FieldType.DECIMAL_MONEY;
    }

    @Override
    public BigDecimal extractValue(Cell cell) {
        BigDecimal result;
        switch (cell.getCellType()) {
            case NUMERIC:
                result = fetchDecimal(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue() ? BigDecimal.ONE : BigDecimal.ZERO;
                break;
            case STRING:
                result = getDecimalFromString(cell);
                break;
            default:
                result = null;
        }
        return result;
    }

    private BigDecimal fetchDecimal(double cellValue) {
        long tmpCellValue = Math.round(cellValue * 100);
        return BigDecimal.valueOf(tmpCellValue).movePointLeft(2);
    }

    private BigDecimal getDecimalFromString(Cell cell) {
        final String strVal = formatter.formatCellValue(cell);
        try {
            return StringUtils.isEmpty(strVal) ? null : new BigDecimal(strVal.trim()).setScale(2, RoundingMode.UP);
        } catch (NumberFormatException e) {
            throw new ValueExtractException(String.format("Unable to parse string value %s to decimal money", strVal), cell);
        }
    }
}
