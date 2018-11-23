package com.jsoft.medpdfmaker.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExternalField {
    public String value() default "";
    public boolean required() default false;
    public FieldType fieldType() default FieldType.STRING;
}