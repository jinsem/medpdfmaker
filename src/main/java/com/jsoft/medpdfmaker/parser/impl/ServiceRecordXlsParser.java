package com.jsoft.medpdfmaker.parser.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import com.jsoft.medpdfmaker.parser.RowCallback;
import com.jsoft.medpdfmaker.parser.TableFileParser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRecordXlsParser implements TableFileParser<ServiceRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRecordXlsParser.class);

    final ObjectBuilder<ServiceRecord> serviceRecordBuilder;

    private final List<String> fieldNames = new ArrayList<>();

    public ServiceRecordXlsParser(ObjectBuilder<ServiceRecord> serviceRecordBuilder) {
        this.serviceRecordBuilder = serviceRecordBuilder;
    }

    @Override
	public void parse(File srcFile, RowCallback<ServiceRecord> rowCallBack) {
        try (final InputStream excelFile = new FileInputStream(srcFile);
             final Workbook workbook = new XSSFWorkbook(excelFile)) {
            final Sheet datatypeSheet = workbook.getSheetAt(0);
            final Iterator<Row> iterator = datatypeSheet.iterator();
            while (iterator.hasNext()) {
                final Row currentRow = iterator.next();
                if (fieldNames.isEmpty()) {
                    tryToInitFieldNames(currentRow);
                } else {
                    processRow(currentRow, rowCallBack);
                }
            }    
        } catch (FileNotFoundException e) {
            LOG.error("Cannot open file", e);
        } catch (IOException e) {
            LOG.error("Cannot open file", e);
        }
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
                serviceRecordBuilder.setAttributeValue(fieldName, curCell);
            }
        } 
        rowCallBack.onRow(serviceRecordBuilder.build());   
    }
}