package com.jsoft.medpdfmaker.parser.impl;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ServiceRecordXlsParser implements TableFileParser<ServiceRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRecordXlsParser.class);

    private final ObjectBuilder<ServiceRecord> serviceRecordBuilder;

    private final List<String> fieldNames = new ArrayList<>();

    public ServiceRecordXlsParser(ObjectBuilder<ServiceRecord> serviceRecordBuilder) {
        this.serviceRecordBuilder = serviceRecordBuilder;
    }

    @Override
	public void parse(File srcFile, int sheetIdx, RowCallback<ServiceRecord> rowCallBack) {
        fieldNames.clear();
        try (final InputStream excelFile = new FileInputStream(srcFile);
             final Workbook workbook = new XSSFWorkbook(excelFile)) {
            if (sheetNumberIsValid(workbook, sheetIdx)) {
                final Sheet datatypeSheet = workbook.getSheetAt(sheetIdx);
                for (final Row currentRow : datatypeSheet) {
                    if (fieldNames.isEmpty()) {
                        tryToInitFieldNames(currentRow);
                    } else {
                        processRow(currentRow, rowCallBack);
                    }
                }
            } else {
                LoggerUtil.warn(LOG, String.format("Sheet number %d if out of valid range [%d, %d] for the being processed Excel book or sheet is hidden",
                        sheetIdx, 0, workbook.getNumberOfSheets()));
            }
        } catch (IOException e) {
            LOG.error("Cannot open file", e);
        }
	}

    private boolean sheetNumberIsValid(Workbook workbook, int sheetIdx) {
        return sheetIdx >= 0 && sheetIdx < workbook.getNumberOfSheets() && !workbook.isSheetHidden(sheetIdx);
    }

    private void tryToInitFieldNames(final Row currentRow) {
        short minColIx = currentRow.getFirstCellNum();
        short maxColIx = currentRow.getLastCellNum();
        for(short colIx=minColIx; colIx<maxColIx; colIx++) {
            final Cell cell = currentRow.getCell(colIx);
            fieldNames.add((cell == null) ? null : cell.getStringCellValue().toUpperCase());    
        }
    }

    private void processRow(Row currentRow, RowCallback<ServiceRecord> rowCallBack) {
        short colIx = currentRow.getFirstCellNum();
        final short maxColIx = currentRow.getLastCellNum();
        for (String fieldName : fieldNames) {
            if (colIx <= maxColIx) {
                Cell curCell = currentRow.getCell(colIx++);
                if (curCell != null) {
                    serviceRecordBuilder.setAttributeValue(fieldName, curCell);
                }
            }
        }
        if (serviceRecordBuilder.canBeBuilt()) {
            rowCallBack.onRow(serviceRecordBuilder.build());
        }
    }
}