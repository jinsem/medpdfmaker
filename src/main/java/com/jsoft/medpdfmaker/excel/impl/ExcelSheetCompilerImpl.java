package com.jsoft.medpdfmaker.excel.impl;

import com.jsoft.medpdfmaker.excel.CellPosition;
import com.jsoft.medpdfmaker.excel.ExcelSheetCompiler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * "Compilation" means here searching variable names inside the given sheet and storing the association between
 * variable names and position of the variable name on the sheet.
 * Variables are distinguished from the usual text strings by prefix and/or suffix. Both prefix and suffix values
 * cannot be empty.
 */
public class ExcelSheetCompilerImpl implements ExcelSheetCompiler {

    /**
     * Prefix of the variables.
     */
    private final String varPrefix;

    /**
     * Suffix of the variables.
     */
    private final String varSuffix;

    /**
     * Flag means that prefix value was provided.
     */
    private final boolean prefixSet;

    /**
     * Flag means that suffix value was provided.
     */
    private final boolean suffixSet;

    /**
     * Map keeps the association between variable names and position of the variables on the sheet.
     */
    private final Map<String, CellPosition> positions = new HashMap<>();

    /**
     * Constructor that inits prefix value for compiler.
     * @param varPrefix variable prefix.
     */
    public ExcelSheetCompilerImpl(String varPrefix) {
        this(varPrefix, null);
    }

    /**
     * Constructor that inits both prefix and suffix values for compiler.
     * @param varPrefix variable prefix.
     * @param varSuffix variable suffix.
     */
    public ExcelSheetCompilerImpl(String varPrefix, String varSuffix) {
        boolean tmpPrefixSet = isNotEmpty(varPrefix);
        boolean tmpSuffixSet = isNotEmpty(varSuffix);
        Validate.isTrue(tmpPrefixSet && tmpSuffixSet, "Prefix and suffix cannot be both empty. At least one value must be set.");
        this.varPrefix = varPrefix;
        this.varSuffix = varSuffix;
        this.prefixSet = tmpPrefixSet;
        this.suffixSet = tmpSuffixSet;
    }

    @Override
    public void replaceAndCompile(final Sheet sheet, Map<String, String> replaceSource) {
        Validate.notNull(sheet);
        Validate.notNull(replaceSource);
        innerCompile(sheet, replaceSource);
    }

    @Override
    public void compile(final Sheet sheet) {
        Validate.notNull(sheet);
        final Map<String, String> emptyMap = Collections.emptyMap();
        innerCompile(sheet, emptyMap);
    }

    private void innerCompile(final Sheet sheet, Map<String, String> replaceSource) {
        positions.clear();
        final int firstRowNum = sheet.getFirstRowNum();
        final int lastRowNum = sheet.getLastRowNum();
        for (int rn = firstRowNum; rn <= lastRowNum; rn++) {
            final Row row = sheet.getRow(rn);
            if (row == null) {
                continue;
            }
            final int firstColNum = row.getFirstCellNum();
            final int lastColNum = row.getLastCellNum();
            if ((firstColNum >= 0) && (lastColNum >= 0)) {
                for (int cn = firstColNum; cn <= lastColNum; cn++) {
                    final Cell cell = row.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if ((cell == null) || (cell.getCellType() == CellType.FORMULA) || (cell.getStringCellValue() == null)) {
                        continue;
                    }
                    if (cell.getCellType() != CellType.FORMULA) {
                        String cellContent = cell.getStringCellValue().trim();
                        final String processedContent = tryToReplace(cellContent, replaceSource);
                        if (!cellContent.equals(processedContent)) {
                            cell.setCellValue(processedContent);
                            cellContent = processedContent;
                        }
                        if (prefixIsValid(cellContent) && suffixIsValid(cellContent)) {
                            final String varName = cleanVariable(cellContent);
                            positions.put(varName, new CellPosition(cn, rn));
                            cell.setCellValue("");
                        }
                    }
                }
            }
        }
    }

    private String tryToReplace(String cellContent, Map<String, String> replaceSource) {
        final StringBuilder result = new StringBuilder(cellContent);
        for (final Map.Entry<String, String> entry : replaceSource.entrySet()) {
            final int pos = result.indexOf(entry.getKey());
            if (pos > -1) {
                result.replace(pos, pos + entry.getKey().length(), entry.getValue());
            }
        }
        return result.toString();
    }

    private String cleanVariable(String cellContent) {
        final int startPos = prefixSet ? varPrefix.length() : 0;
        final int endPos = suffixSet ?  (cellContent.length() - varSuffix.length()) : cellContent.length();
        return cellContent.substring(startPos, endPos);
    }

    private boolean prefixIsValid(String cellContent) {
        return !prefixSet || cellContent.startsWith(varPrefix);
    }

    private boolean suffixIsValid(String cellContent) {
        return !suffixSet || cellContent.endsWith(varSuffix);
    }

    @Override
    public CellPosition getVarPosition(String varName) {
        return positions.get(varName);
    }

    @Override
    public boolean variablesExist(String... variableNames) {
        if (variableNames == null) {
            return true;
        }
        for (String variableName : variableNames) {
            if (!positions.containsKey(variableName)) {
                return false;
            }
        }
        return true;
    }
}
