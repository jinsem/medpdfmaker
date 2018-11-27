package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.poi.ss.usermodel.DateUtil.getJavaDate;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

public class LocalTimeValueExtractor implements ValueExtractor<LocalTime> {

    private static final List<DateTimeFormatter> TIME_FORMATS =
            Arrays.asList(
                    DateTimeFormatter.ofPattern("h:ma"),
                    DateTimeFormatter.ofPattern("h:m:sa"),
                    DateTimeFormatter.ofPattern("H:m"),
                    DateTimeFormatter.ofPattern("H:m:s")
            );

    @Override
    public FieldType canParse() {
        return FieldType.TIME;
    }

    @Override
    public LocalTime extractValue(Cell cell) {
        final LocalTime result;
        switch (cell.getCellType()) {
            case NUMERIC:
                final Date tmpD = (isCellDateFormatted(cell)) ? cell.getDateCellValue() : getJavaDate(cell.getNumericCellValue());
                result = tmpD.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                break;
            case BLANK:
                result = null;
                break;
            case STRING:
                result = parseTimeFromString(cell);
                break;
            default:
                throw new ValueExtractException("Cannot extract time value from the cell", cell);
        }
        return result;
    }

    private LocalTime parseTimeFromString(Cell cell) {
        final String strValue = StringUtils.remove(trim(upperCase(cell.getStringCellValue())), " ");
        if (StringUtils.isBlank(strValue)) {
            return null;
        }
        for (final DateTimeFormatter format : TIME_FORMATS) {
            try {
                return LocalTime.parse(strValue, format);
            } catch (DateTimeParseException e) {
                // did not work, try next
            }
        }
        throw new ValueExtractException("Cannot extract time value from the cell", cell);
    }
}
