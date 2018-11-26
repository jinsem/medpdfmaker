package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.ExternalField;
import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.exception.AppException;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Builder implementation for ServiceRecord.
 */
public class ServiceRecordBuilder implements ObjectBuilder<ServiceRecord> {

    private static final Set<String> REQUIRED_FIELDS = new HashSet<>();
    private static final Map<String, FieldMetaData> METADATA = buildMetaData();

    private ServiceRecord resultRecord = new ServiceRecord();
    private Set<String> requiredFieldsWithValues = new HashSet<>();

    private final Set<String> possibleTrues = new HashSet<>(
            Arrays.asList("Y", "YES", "TRUE", "X")
    );
    private static final List<DateTimeFormatter> TIME_FORMATS =
            Arrays.asList(
                    DateTimeFormatter.ofPattern("h:mma"),
                    DateTimeFormatter.ofPattern("h:mm a"),
                    DateTimeFormatter.ofPattern("HH:mm"),
                    DateTimeFormatter.ofPattern("HH:mm:ss")
            );

    private static Map<String, FieldMetaData> buildMetaData() {
        Map<String, FieldMetaData> result = new HashMap<>();
        for (Method method: ServiceRecord.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExternalField.class)) {
                final ExternalField ann = method.getAnnotation(ExternalField.class);
                final String fieldName = ann.value();
                final FieldType fieldType = ann.fieldType();
                if (ann.required()) {
                    REQUIRED_FIELDS.add(fieldName);
                }
                final FieldMetaData newMd = new FieldMetaData(method, fieldType);
                result.put(fieldName, newMd);
            }
        }        
        return result;
    }

    @Override
    public void setAttributeValue(String attrName, Cell valueCell) {
        final FieldMetaData fieldMetaData = METADATA.get(attrName);
        if (fieldMetaData == null) {
            return;
        }
        if (REQUIRED_FIELDS.contains(attrName) && cellHasValue(valueCell)) {
            requiredFieldsWithValues.add(attrName);
        }
        Method methodToCall = fieldMetaData.method;
        try {
            if (FieldType.BOOLEAN.equals(fieldMetaData.fieldType)) {
                final Boolean boolVal = extractBooleanValue(valueCell);
                methodToCall.invoke(resultRecord, boolVal);
            } else if (FieldType.DATE.equals(fieldMetaData.fieldType)) {
                final LocalDate dateVal = extractDateValue(valueCell);
                methodToCall.invoke(resultRecord, dateVal);
            } else if (FieldType.TIME.equals(fieldMetaData.fieldType)) {
                final LocalTime timeVal = extractTimeValue(valueCell);
                methodToCall.invoke(resultRecord, timeVal);
            } else if (FieldType.STRING.equals(fieldMetaData.fieldType)) {
                final String stringVal = extractStringValue(valueCell);
                methodToCall.invoke(resultRecord, stringVal);
            } else if (FieldType.INTEGER.equals(fieldMetaData.fieldType)) {
                final Integer stringVal = extractIntegerValue(valueCell);
                methodToCall.invoke(resultRecord, stringVal);
            } else {
                throw new IllegalStateException(fieldMetaData.fieldType + " type is unknown");
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // This is unlikely situation. Annotation is used only for public fields, so no IllegalAccessException possible
            // IllegalArgumentException is possible during application testing, but not very possible in production
            // when new building logic is debugged and tested.
            // InvocationTargetException is also not possible because all the methods that are called here are simple
            // setters and do not have any complex logic that can throw an exception.
            throw new AppException("Error setting value for attribute " + attrName, e);
        }
    }

    private boolean cellHasValue(Cell valueCell) {
        return valueCell != null && !CellType.BLANK.equals(valueCell.getCellType());
    }

    @Override
    public boolean canBeBuilt() {
        return REQUIRED_FIELDS.equals(requiredFieldsWithValues);
    }

    private Boolean extractBooleanValue(Cell cell) {
        boolean result;
        if (CellType.BLANK.equals(cell.getCellType())) {
            result = false;
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            result = cell.getBooleanCellValue();
        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            final double numVal = cell.getNumericCellValue();
            result = Math.abs(0 - numVal) > 0.01;
        } else if (CellType.STRING.equals(cell.getCellType())) {
            final String strVal = StringUtils.upperCase(StringUtils.trim(cell.getStringCellValue()));
            result = strVal != null && possibleTrues.contains(strVal);
        } else {
            result = false;
        }
        return result;
    }     

    private Integer extractIntegerValue(Cell cell) {
        Integer result = null;
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

    private String extractStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String result;
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            result = String.valueOf(cell.getNumericCellValue());    
        } else {
            result = cell.getStringCellValue();
        }   
        return result;
    }

    private LocalDate extractDateValue(Cell cell) {
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

    private LocalTime extractTimeValue(Cell cell) {
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

    @Override
    public ServiceRecord build() {
        final ServiceRecord result = resultRecord;
        resultRecord = new ServiceRecord();
        requiredFieldsWithValues = new HashSet<>();
        return result;
	}

    private static class FieldMetaData {

        private final Method method;
        private final FieldType fieldType;

        FieldMetaData(Method method, FieldType fieldType) {
            this.method = method;
            this.fieldType = fieldType;
        }
    }
}