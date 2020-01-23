package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Extract boolean value from cells.
 */
public class BooleanValueExtractor implements ValueExtractor<Boolean> {

    private final Set<String> possibleTrues = new HashSet<>(
            Arrays.asList("YES", "CANCELLED", "YES(MUST)", "1")
    );

    @Override
    public FieldType canParse() {
        return FieldType.BOOLEAN;
    }

    /**
     * Extract boolean value from the cell.
     * Basic extraction rules:
     * - If cell's value type is boolean, return boolean value stored in cell.
     * - if cell's value type is numeric, return true if value is greater than zero, false otherwise.
     * - if cell's value is string, return true of value of the cell equals or contains some predefined True values marekrs.
     * @param cell Cell object tht holds value to extract.
     * @return boolean value extracted from the cell.
     */
    @Override
    public Boolean extractValue(Cell cell) {
        boolean result;
        switch (cell.getCellType()) {
            case BLANK:
                result = false;
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                result = Math.round(cell.getNumericCellValue()) != 0;
                break;
            case STRING:
                final String strVal = StringUtils.remove(cell.getStringCellValue(), " ");
                result = StringUtils.isNotEmpty(strVal) && valueIsTrue(strVal.trim().toUpperCase());
                break;
            default:
                result = false;
        }
        return result;
    }

    private boolean valueIsTrue(final String strVal) {
        boolean result = possibleTrues.contains(strVal);
        if (!result) {
            for (final String possibleTrue : possibleTrues) {
                result = strVal.contains(possibleTrue);
                if (result) {
                    break;
                }
            }
        }
        return result;
    }
}
