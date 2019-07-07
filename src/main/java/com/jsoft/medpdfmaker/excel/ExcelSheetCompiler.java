package com.jsoft.medpdfmaker.excel;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

/**
 * Base interface for excel sheet compiler class that are used for reading information about the variables defined
 * in report templates.
 *
 * User: jin
 * Date: 7/20/13 12:33 PM
 * Version: 1.0
 */
public interface ExcelSheetCompiler {

    /**
     * Scan given excel sheet and save position of all variables found on it.
     * In addition, this method replaces all occurrences of the replaceSource key by it's values.
     *
     * @param sheet Sheet object to search variables positions.
     * @param replaceSource text fragments, if they equal key of this map, will be replaced by values of this map.
     */
    void replaceAndCompile(Sheet sheet, Map<String, String> replaceSource);

    /**
     * Scan given excel sheet and save position of all variables found on it.
     * @param sheet Sheet object to search variables positions.
     */
    void compile(Sheet sheet);

    /**
     * Return position of the variable,
     * @param varName name of the variable which position must be returned.
     * @return CellPosition object contained position of the variable on the template sheet or null is no variable with
     * provided name has been found/
     */
    CellPosition getVarPosition(String varName);

    /**
     * Check that all variables in the provided collection has been found by compiler in a report template.
     * @param variableNames list of the variable names to check their existence.
     * @return true if variableNames is null or empty or is all variables have been found in report template, false otherwise.
     */
    boolean variablesExist(String... variableNames);
}
