package com.jsoft.medpdfmaker.domain;

/**
 * Possible data types of the fields that can be provided in the incoming files.
 */
public enum FieldType {

    /**
     * Boolean value type
     */
    BOOLEAN,

    /**
     * Free form string value type
     */
    STRING,

    /**
     * Date type that does not include time part
     */
    DATE,

    /**
     * Time type that does not include date part
     */
    TIME,

    /**
     * Integer number
     */
    INTEGER
}