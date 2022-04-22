package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.ExternalField;
import com.jsoft.medpdfmaker.domain.FieldType;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.exception.AppException;
import com.jsoft.medpdfmaker.exception.UnknownAttributeException;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

/**
 * Builder implementation for ServiceRecord.
 */
public class ServiceRecordBuilder implements ObjectBuilder<ServiceRecord> {

    private static final Set<String> REQUIRED_FIELDS = new TreeSet<>();

    private static final Map<String, FieldMetaData> METADATA = buildMetaData();

    private ServiceRecord resultRecord = new ServiceRecord();

    private final Map<FieldType, ValueExtractor> valueExtractors;

    private final BigDecimal defaultTripPrice;

    private final String defaultProcedureCode;

    public ServiceRecordBuilder(List<ValueExtractor> extractors, BigDecimal defaultTripPrice,
                                String defaultProcedureCode) {
        Validate.notNull(defaultTripPrice, "defaultTripPrice cannot be null");
        valueExtractors = new EnumMap<>(FieldType.class);
        for (ValueExtractor extractor : extractors) {
            valueExtractors.put(extractor.canParse(), extractor);
        }
        this.defaultTripPrice = defaultTripPrice;
        this.defaultProcedureCode = defaultProcedureCode;
    }

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
                result.put(fieldName, new FieldMetaData(method, fieldType));
            }
        }        
        return result;
    }

    @Override
    public boolean attributeIsKnown(String attrName) {
        return attrName != null && METADATA.containsKey(attrName);
    }

    @Override
    public void setAttributeValue(String attrName, Cell valueCell) {
        final FieldMetaData fieldMetaData = METADATA.get(attrName);
        if (fieldMetaData == null) {
            throw new UnknownAttributeException(String.format("Attribute %s is unknown", attrName));
        }
        final Method methodToCall = fieldMetaData.method;
        try {
            if (fieldMetaData.fieldType == null || !valueExtractors.containsKey(fieldMetaData.fieldType)) {
                throw new IllegalStateException(fieldMetaData.fieldType + " fieldType is not defined or unknown");
            }
            final ValueExtractor valueExtractor = valueExtractors.get(fieldMetaData.fieldType);
            methodToCall.invoke(resultRecord, valueExtractor.extractValue(valueCell));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // This is unlikely situation. Annotation is used only for public fields, so no IllegalAccessException possible
            // IllegalArgumentException is possible during application testing, but not very possible in production
            // when new building logic is debugged and tested.
            // InvocationTargetException is also not possible because all the methods that are called here are simple
            // setters and do not have any complex logic that can throw an exception.
            throw new AppException("Error setting value for attribute " + attrName, e);
        }
    }

    @Override
    public boolean entityIsEmpty() {
        return resultRecord.allFieldsAreEmpty();
    }

    @Override
    public boolean entityKeyIsEmpty() {
        return resultRecord.requiredFieldsAreEmpty();
    }

    @Override
    public String getRequiredAttributesNames() {
        return String.join(",", REQUIRED_FIELDS);
    }

    @Override
    public ServiceRecord build() {
        final ServiceRecord result = resultRecord;
        if (result.getTripPrice() == null) {
            result.setTripPrice(defaultTripPrice);
        }
        if (Strings.isBlank(result.getProcedureCode())) {
            result.setProcedureCode(defaultProcedureCode);
        }
        resultRecord = new ServiceRecord();
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