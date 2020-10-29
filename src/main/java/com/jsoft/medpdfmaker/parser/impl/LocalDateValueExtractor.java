package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.poi.ss.usermodel.DateUtil.getJavaDate;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

public class LocalDateValueExtractor implements ValueExtractor<LocalDate> {

    private static final List<DateTimeFormatter> DATE_FORMATS =
            Arrays.asList(
                    DateTimeFormatter.ofPattern("dd-MMM-yy"),
                    DateTimeFormatter.ofPattern("dd-MMM-yyyy"),
                    DateTimeFormatter.ofPattern("MM/dd/yy"),
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                    DateTimeFormatter.ofPattern("M/d/yyyy"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd")
            );

    @Override
    public FieldType canParse() {
        return FieldType.DATE;
    }

    @Override
    public LocalDate extractValue(Cell cell) {
        final LocalDate result;
        switch (cell.getCellType()) {
            case NUMERIC:
                final Date tmpD = (isCellDateFormatted(cell)) ? cell.getDateCellValue() : getJavaDate(cell.getNumericCellValue());
                result = tmpD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                break;
            case BLANK:
                result = null;
                break;
            case STRING:
                result = parseDateFromString(cell);
                break;
            default:
                throw new ValueExtractException("Cannot extract date value from the cell", cell);
        }
        return result;
    }

    private LocalDate parseDateFromString(Cell cell) {
        final String strValue = trim(cell.getStringCellValue());
        if (StringUtils.isBlank(strValue)) {
            return null;
        }
        for (final DateTimeFormatter format : DATE_FORMATS) {
            try {
                return LocalDate.parse(strValue, format);
            } catch (DateTimeParseException e) {
                // did not work, try next
            }
        }
        throw new ValueExtractException("Cannot extract date value from the cell", cell);
    }
}
