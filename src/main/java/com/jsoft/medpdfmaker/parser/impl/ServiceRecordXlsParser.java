package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import com.jsoft.medpdfmaker.parser.ParsingResult;
import com.jsoft.medpdfmaker.parser.RowCallback;
import com.jsoft.medpdfmaker.parser.TableFileParser;
import com.jsoft.medpdfmaker.util.LoggerUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.jsoft.medpdfmaker.parser.ParsingResult.*;

public class ServiceRecordXlsParser implements TableFileParser<ServiceRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRecordXlsParser.class);

    private final ObjectBuilder<ServiceRecord> serviceRecordBuilder;

    public ServiceRecordXlsParser(ObjectBuilder<ServiceRecord> serviceRecordBuilder) {
        this.serviceRecordBuilder = serviceRecordBuilder;
    }

    @Override
	public ParsingResult parse(File srcFile, int sheetIdx, RowCallback<ServiceRecord> rowCallBack) throws IOException {
        ParsingResult result = OK;
        final List<String> fieldNames = new ArrayList<>();
        try (final InputStream excelFile = new FileInputStream(srcFile);
             final Workbook workbook = new XSSFWorkbook(excelFile)) {
            if (sheetNumberIsValid(workbook, sheetIdx)) {
                final Sheet datatypeSheet = workbook.getSheetAt(sheetIdx);
                for (final Row currentRow : datatypeSheet) {
                    if (fieldNames.isEmpty()) {
                        tryToInitFieldNames(fieldNames, currentRow);
                    } else {
                        final ParsingResult rowResult = processRow(fieldNames, currentRow, rowCallBack);
                        result = moreImportant(result, rowResult);
                    }
                }
                if (fieldNames.isEmpty()) {
                    result = moreImportant(result, WARNING);
                    LoggerUtil.warn(LOG, String.format("Sheet number %d does not contain any data", sheetIdx));
                }
            } else {
                result = moreImportant(result, WARNING);
                LoggerUtil.warn(LOG, String.format("Sheet number %d if out of valid range [%d, %d] for the being processed Excel book or sheet is hidden",
                        sheetIdx, 0, workbook.getNumberOfSheets()));
            }
        }
        return result;
	}

    private boolean sheetNumberIsValid(Workbook workbook, int sheetIdx) {
        return sheetIdx >= 0 && sheetIdx < workbook.getNumberOfSheets() && !workbook.isSheetHidden(sheetIdx);
    }

    private void tryToInitFieldNames(List<String> fieldNames, final Row currentRow) {
        short minColIx = currentRow.getFirstCellNum();
        short maxColIx = currentRow.getLastCellNum();
        for(short colIx=minColIx; colIx<maxColIx; colIx++) {
            final Cell cell = currentRow.getCell(colIx);
            fieldNames.add((cell == null) ? null : cell.getStringCellValue().toUpperCase());    
        }
    }

    private ParsingResult processRow(List<String> fieldNames, Row currentRow, RowCallback<ServiceRecord> rowCallBack) {
        final ParsingResult result;
        short colIx = currentRow.getFirstCellNum();
        final short maxColIx = currentRow.getLastCellNum();
        for (String fieldName : fieldNames) {
            if (colIx <= maxColIx) {
                final Cell curCell = currentRow.getCell(colIx++);
                if (curCell != null) {
                    serviceRecordBuilder.setAttributeValue(fieldName, curCell);
                }
            }
        }
        if (serviceRecordBuilder.entityIsEmpty()) {
            LoggerUtil.logRowParsingWarning(LOG, "Empty row processing was skipped", currentRow);
            result = WARNING;
        } else if (serviceRecordBuilder.entityKeyIsEmpty()) {
            LoggerUtil.logRowParsingError(LOG, String.format("One or more required values %s are not set", serviceRecordBuilder.getRequiredAttributesNames()), currentRow);
            result = ERROR;
        } else {
            rowCallBack.onRow(serviceRecordBuilder.build());
            result = OK;
        }
        return result;
    }
}