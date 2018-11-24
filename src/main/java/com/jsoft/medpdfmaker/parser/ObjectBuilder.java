package com.jsoft.medpdfmaker.parser;

import com.jsoft.medpdfmaker.domain.DomainEntity;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Implementations of this interface are responsible for building objects of type T using the data loaded from excel
 * tables.
 * @param <T> type of the objects that can be built by a specific builder.
 */
public interface ObjectBuilder<T extends DomainEntity> {

    /**
     * Set new object's attribute value.
     * @param attrName name of the attribute that needs to be set. Name must correspond to a value of ExternalField's
     * annotation that marks members of the being built class.
     * @param value poi Cell object that contains actual value of the attribute that needs to be set.
     */
    void setAttributeValue(String attrName, Cell value);

    /**
     * Check if object can be built after setting all the values. The most possible reason of impossibility to build
     * object is that some of the required fields or all of them were not set.
     * @return true if object can be built, false otherwise.
     */
    boolean canBeBuilt();

    /**
     * Build object and return it.
     * @return brand new object of class T.
     */
    T build();
}