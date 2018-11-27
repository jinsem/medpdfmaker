package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class IntegerValueExtractor implements ValueExtractor<Integer> {

    @Override
    public FieldType canParse() {
        return FieldType.INTEGER;
    }

    @Override
    public Integer extractValue(Cell cell) {
        Integer result = null;
        if (cell == null) {
            return result;
        }
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            double numVal = cell.getNumericCellValue();
            result = (int)Math.round(numVal);
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            boolean boolVal = cell.getBooleanCellValue();
            result = boolVal ? 0 : 1;
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String strVal = StringUtils.trim(cell.getStringCellValue());
            try {
                result = StringUtils.isEmpty(strVal) ? null : Integer.valueOf(strVal);
            } catch (NumberFormatException e) {
                result = null;
            }
        }
        return result;
    }
}
