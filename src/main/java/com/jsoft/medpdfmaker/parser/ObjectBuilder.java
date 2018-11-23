package com.jsoft.medpdfmaker.parser;

import org.apache.poi.ss.usermodel.Cell;

public interface ObjectBuilder<T> {

    void setAttributeValue(String attrName, Cell value);

    boolean canBeBuilt();

    T build();
}