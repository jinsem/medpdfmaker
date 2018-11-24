package com.jsoft.medpdfmaker.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation that is used to define meta data for the domain objects that can loaded from external sources.
 * Annotation is processed by application to build meta data for annotated proeprties and simplify setting values of them.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExternalField {

    /**
     * Name of the property in the imported data source.
     * This value is used to link annotated attributes and its value in the loaded data source.
     */
    String value() default "";

    /**
     * Mark attribute is required. If required attribute is not presented in the loaded data source or its value
     * is null, data loading process of the entity where empty proprety is defined must be rejected.
     */
    boolean required() default false;

    /**
     * Type of the field value.
     */
    FieldType fieldType() default FieldType.STRING;
}