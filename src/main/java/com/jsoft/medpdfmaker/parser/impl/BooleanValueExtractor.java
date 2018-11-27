package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Extract boolean value from cells.
 */
public class BooleanValueExtractor implements ValueExtractor<Boolean> {

    private final Set<String> possibleTrues = new HashSet<>(
            Arrays.asList("YES", "CANCELLED", "YES (MUST)", "YES(MUST)")
    );

    @Override
    public FieldType canParse() {
        return FieldType.BOOLEAN;
    }

    /**
     * Extract boolean value from the cell.
     * Basic extraction rules:
     * - If cell is null, return null.
     * - If cell's value type is boolean, return boolean value stored in cell.
     * - if cell's value type is numeric, return true if value is greater than zero, false otherwise.
     * - if cell's value is string, return true of value of the cell equals or contains some predefined True values marekrs.
     * @param cell Cell object tht holds value to extract.
     * @return boolean value extracted from the cell.
     */
    @Override
    public Boolean extractValue(Cell cell) {
        boolean result;
        if (cell == null) {
            return false;
        }
        if (CellType.BLANK.equals(cell.getCellType())) {
            result = false;
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            result = cell.getBooleanCellValue();
        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            final double numVal = cell.getNumericCellValue();
            result = Math.abs(0 - numVal) > 0.01;
        } else if (CellType.STRING.equals(cell.getCellType())) {
            final String strVal = StringUtils.upperCase(StringUtils.trim(cell.getStringCellValue()));
            result = strVal != null && valueIsTrue(strVal);
        } else {
            result = false;
        }
        return result;
    }

    private boolean valueIsTrue(String strVal) {
        boolean result = possibleTrues.contains(strVal);
        if (!result) {
            for (String possibleTrue : possibleTrues) {
                if (possibleTrue.contains(strVal)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
