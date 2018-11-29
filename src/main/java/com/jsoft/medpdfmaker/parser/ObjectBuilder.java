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
     * Return true if attribute can be processed by the builder.
     * @param attrName attribute name to check if it can be processed or not.
     * @return true if attribute can be processed by the builder; false otherwise.
     */
    boolean attributeIsKnown(String attrName);

    /**
     * Set new object's attribute value.
     * @param attrName name of the attribute that needs to be set. Name must correspond to a value of ExternalField's
     * annotation that marks members of the being built class.
     * @param value poi Cell object that contains actual value of the attribute that needs to be set.
     */
    void setAttributeValue(String attrName, Cell value);

    /**
     * Check if all the attributes of entity are empty. It is kind of representation of the case when empty row
     * is added in the Excel book. Row phicially exists, but there meaningful data in the row.
     * This method helps builder users to understand if object that is about to be built contains any data.
     * @return true if object that is about to be built is empty (values of all attributes are null or empty strings).
     */
    boolean entityIsEmpty();

    /**
     * Check if key attributes used for entity identification are empty.
     * @return true if key attributes used for entity identification are empty.
     */
    boolean entityKeyIsEmpty();

    String getRequiredAttributesNames();

    /**
     * Build object and return it.
     * @return brand new object of class T.
     */
    T build();
}