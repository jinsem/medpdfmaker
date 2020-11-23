package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.exception.ValueExtractException;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import com.jsoft.medpdfmaker.parser.Result;
import com.jsoft.medpdfmaker.parser.TableFileParser;
import com.jsoft.medpdfmaker.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.jsoft.medpdfmaker.parser.Result.ERROR;
import static com.jsoft.medpdfmaker.parser.Result.OK;
import static com.jsoft.medpdfmaker.parser.Result.WARNING;
import static com.jsoft.medpdfmaker.parser.Result.moreImportant;

public class ServiceRecordXlsParser implements TableFileParser<ServiceRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRecordXlsParser.class);

    private final ObjectBuilder<ServiceRecord> serviceRecordBuilder;

    public ServiceRecordXlsParser(final ObjectBuilder<ServiceRecord> serviceRecordBuilder) {
        this.serviceRecordBuilder = serviceRecordBuilder;
    }

    @Override
	public Result parse(final File srcFile, final int sheetIdx, final Consumer<ServiceRecord> rowCallBack)
            throws IOException {
        Result result = OK;
        final List<String> fieldNames = new ArrayList<>();
        try (final InputStream excelFile = new FileInputStream(srcFile);
             final Workbook workbook = new XSSFWorkbook(excelFile)) {
            if (sheetNumberIsValid(workbook, sheetIdx)) {
                for (final Row currentRow : workbook.getSheetAt(sheetIdx)) {
                    if (rowIsVisible(currentRow)) {
                        if (fieldNames.isEmpty()) {
                            result = moreImportant(result, tryToInitFieldNames(fieldNames, currentRow));
                        } else {
                            result = moreImportant(result, processRow(fieldNames, currentRow, rowCallBack));
                        }
                    }
                }
                if (fieldNames.isEmpty()) {
                    result = moreImportant(result, WARNING);
                    LoggerUtil.warn(LOG, String.format("Sheet number %d does not contain any data", sheetIdx));
                }
            } else {
                result = moreImportant(result, WARNING);
                LoggerUtil.warn(LOG, String.format("Sheet number %d is hidden or out of valid range [%d, %d] for the being processed Excel book",
                        sheetIdx, 0, workbook.getNumberOfSheets()));
            }
        }
        return result;
	}

    private boolean rowIsVisible(Row currentRow) {
        if (!currentRow.isFormatted() || currentRow.getRowStyle() == null) {
            return true;
        }
        return !currentRow.getRowStyle().getHidden();
    }

    private boolean sheetNumberIsValid(Workbook workbook, int sheetIdx) {
        return sheetIdx >= 0 && sheetIdx < workbook.getNumberOfSheets() && !workbook.isSheetHidden(sheetIdx);
    }

    private Result tryToInitFieldNames(List<String> fieldNames, final Row currentRow) {
        Result result = OK;
        final DataFormatter formatter = new DataFormatter();
        short minColIx = currentRow.getFirstCellNum();
        short maxColIx = currentRow.getLastCellNum();
        for(short colIx=minColIx; colIx<maxColIx; colIx++) {
            final Cell cell = currentRow.getCell(colIx);
            final String fieldName = (cell == null) ? "" : StringUtils.upperCase(formatter.formatCellValue(cell)).trim();
            if (!serviceRecordBuilder.attributeIsKnown(fieldName)) {
                LoggerUtil.warn(LOG, String.format("Attribute %s is unknown. Value of this attribute will be ignored", fieldName));
                result = WARNING;
            }
            // We have to store both known and unknown attributes to simplify synchronizing header and actual values
            fieldNames.add(fieldName);
        }
        return result;
    }

    private Result processRow(List<String> fieldNames, Row currentRow, Consumer<ServiceRecord> rowCallBack) {
        Result result = OK;
        short colIx = currentRow.getFirstCellNum();
        if (colIx >= 0) {
            final short maxColIx = currentRow.getLastCellNum();
            for (String fieldName : fieldNames) {
                if (colIx <= maxColIx) {
                    final Cell curCell = currentRow.getCell(colIx++);
                    if (curCell != null) {
                        result = getDataFromCell(result, fieldName, curCell);
                    }
                }
            }
            if (result == OK && !serviceRecordBuilder.entityIsEmpty()) {
                if (serviceRecordBuilder.entityKeyIsEmpty()) {
                    LoggerUtil.logRowParsingError(LOG, String.format("One or more required values %s are not set", serviceRecordBuilder.getRequiredAttributesNames()), currentRow);
                    result = ERROR;
                } else {
                    rowCallBack.accept(serviceRecordBuilder.build());
                }
            }
        }
        return result;
    }

    private Result getDataFromCell(Result result, String fieldName, Cell curCell) {
        try {
            if (serviceRecordBuilder.attributeIsKnown(fieldName)) {
                serviceRecordBuilder.setAttributeValue(fieldName, curCell);
            }
        } catch (final ValueExtractException e) {
            LoggerUtil.logCellParsingError(LOG, String.format("Cell value cannot be processed: %s", e.getMessage()), curCell);
            result = ERROR;
        }
        return result;
    }
}
