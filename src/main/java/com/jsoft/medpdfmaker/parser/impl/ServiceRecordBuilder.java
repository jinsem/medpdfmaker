package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.ExternalField;
import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
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
import java.util.*;

public class ServiceRecordBuilder implements ObjectBuilder<ServiceRecord> {

    private static final Set<String> REQUIRED_FIELDS = new HashSet<>();
    private static final Map<String, FieldMetaData> METADATA = buildMetaData();

    private ServiceRecord resultRecord = new ServiceRecord();
    private Set<String> requiredFieldsWithValues = new HashSet<>();

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
            if (fieldMetaData.fieldType.equals(FieldType.BOOLEAN)) {
                Boolean boolVal = extractBooleanValue(valueCell);
                methodToCall.invoke(resultRecord, boolVal);
            } else if (fieldMetaData.fieldType.equals(FieldType.DATE)) {
                LocalDate dateVal = extractDateValue(valueCell);
                methodToCall.invoke(resultRecord, dateVal);
            } else if (fieldMetaData.fieldType.equals(FieldType.TIME)) {
                LocalTime timeVal = extractTimeValue(valueCell);
                methodToCall.invoke(resultRecord, timeVal);
            } else if (fieldMetaData.fieldType.equals(FieldType.STRING)) {
                String stringVal = extractStringValue(valueCell);
                methodToCall.invoke(resultRecord, stringVal);
            } else if (fieldMetaData.fieldType.equals(FieldType.INTEGER)) {
                Integer stringVal = extractIntegerValue(valueCell);
                methodToCall.invoke(resultRecord, stringVal);
            } else {
                // TODO: exception
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            //TODO implement error processing
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
        Boolean result = false;
        if (CellType.BOOLEAN.equals(cell.getCellType())) {
            result = cell.getBooleanCellValue();
        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            double numVal = cell.getNumericCellValue();
            result = Math.abs(0 - numVal) > 0.01;
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String strVal = StringUtils.upperCase(StringUtils.trim(cell.getStringCellValue()));
            Set<String> possibleTrues = new HashSet<>();
            possibleTrues.add("Y");
            possibleTrues.add("YES");
            possibleTrues.add("TRUE");
            possibleTrues.add("X");
            result = possibleTrues.contains(strVal);
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
        return null; // TODO implement
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