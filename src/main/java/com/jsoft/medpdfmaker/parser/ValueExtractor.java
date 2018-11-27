package com.jsoft.medpdfmaker.parser;

import com.jsoft.medpdfmaker.domain.FieldType;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Interface for value extractors from POI Cell object.
 * @param <T>
 */
public interface ValueExtractor<T> {

    /**
     * Type that can be parsed.
     * @return type that can be parsed.
     */
    FieldType canParse();

    /**
     * Extract value of required type from Excel Cell.
     * @param cell Cell object tht holds value to extract.
     * @return Extract value or null if cell is null.
     */
    T extractValue(Cell cell);
}
