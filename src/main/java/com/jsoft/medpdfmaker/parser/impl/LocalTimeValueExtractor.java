package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LocalTimeValueExtractor implements ValueExtractor<LocalTime> {

    private static final List<DateTimeFormatter> TIME_FORMATS =
            Arrays.asList(
                    DateTimeFormatter.ofPattern("h:mma"),
                    DateTimeFormatter.ofPattern("h:mm a"),
                    DateTimeFormatter.ofPattern("HH:mm"),
                    DateTimeFormatter.ofPattern("HH:mm:ss")
            );

    @Override
    public FieldType canParse() {
        return FieldType.TIME;
    }

    @Override
    public LocalTime extractValue(Cell cell) {
        LocalTime result = null;
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            final Date dateVal;
            if (DateUtil.isCellDateFormatted(cell)) {
                dateVal = cell.getDateCellValue();
            } else {
                dateVal = new Date(Math.round(cell.getNumericCellValue()));
            }
            result = dateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        } else if (CellType.STRING.equals(cell.getCellType())) {
            final String strValue = cell.getStringCellValue();
            for (final DateTimeFormatter format : TIME_FORMATS) {
                try {
                    result = LocalTime.parse(strValue, format);
                    break;
                } catch (DateTimeParseException e) {
                    // did not work, try next
                }
            }
        }
        // TODO: add invalid data handling
        return result;
    }
}
