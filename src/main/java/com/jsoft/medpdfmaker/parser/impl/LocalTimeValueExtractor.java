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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.strip;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.poi.ss.usermodel.DateUtil.getJavaDate;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

public class LocalTimeValueExtractor implements ValueExtractor<LocalTime> {

    private static final List<DateTimeFormatter> TIME_FORMATS =
            Arrays.asList(
                    DateTimeFormatter.ofPattern("h:ma"),
                    DateTimeFormatter.ofPattern("H:m")
            );

    private static final Set<Character> ALLOWED_CHARS_IN_TIME = new HashSet<>(Arrays.asList(
            '1','2','3','4','5','6','7','8','9','0','A','P','M',':'
    ));

    private static final Set<String> EMPTY_VALUES = new HashSet<>(
            Arrays.asList("W/C", "N/A")
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
                throw new ValueExtractException(String.format("Cannot extract time value from the cell, cell type %s is not supported", cell.getCellType()), cell);
        }
        return result;
    }

    private LocalTime parseTimeFromString(Cell cell) {
        final String strValue = cleanTimeString(trim(upperCase(cell.getStringCellValue())));
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

    private String cleanTimeString(String src) {
        if (src == null) {
            return null;
        }
        String workSrc = deleteEmptyValues(src);
        final StringBuilder result = new StringBuilder();
        for (int i=0;i<workSrc.length();i++) {
            char c = workSrc.charAt(i);
            if (ALLOWED_CHARS_IN_TIME.contains(c)) {
                result.append(c);
            } else {
                if (c == ';') {
                    result.append(':');
                }
            }
        }
        return result.length() == 0 ? null : result.toString();
    }

    private String deleteEmptyValues(String src) {
        String result = src;
        for (String emptyValue : EMPTY_VALUES) {
            result = result.replaceAll(emptyValue, "");
        }
        return result;
    }

}
