package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateValueExtractor implements ValueExtractor<LocalDate> {

    @Override
    public FieldType canParse() {
        return FieldType.DATE;
    }

    @Override
    public LocalDate extractValue(Cell cell) {
        LocalDate result = null;
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            if (DateUtil.isCellDateFormatted(cell)) {
                final Date dateVal = cell.getDateCellValue();
                result = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }
        // TODO: add invalid data handling
        return result;
    }
}
