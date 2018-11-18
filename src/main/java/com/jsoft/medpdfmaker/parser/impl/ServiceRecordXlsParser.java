package com.jsoft.medpdfmaker.parser.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
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

    private final List<String> fieldNames = new ArrayList<>();

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
                    processRow(currentRow);
                }
            }    
        } catch (FileNotFoundException e) {
            LOG.error("Cannot open file", e);
        } catch (IOException e) {
            LOG.error("Cannot open file", e);
        }
	}

    private void tryToInitFieldNames(Row currentRow) {
        Iterator<Cell> cellIterator = currentRow.iterator();
        while (cellIterator.hasNext()) {
            Cell currentCell = cellIterator.next();
            fieldNames.add(currentCell.getStringCellValue());    
        }    
    }

    private Map<String, Cell> processRow(Row currentRow) {
        final Map<String, Cell> result = new LinkedHashMap<>();
        Iterator<Cell> cellIterator = currentRow.iterator();
        Iterator<String> fieldNamesIterator = fieldNames.iterator();
        while (fieldNamesIterator.hasNext()) {
            String fieldName = fieldNamesIterator.next();
            if (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                result.put(fieldName, currentCell);
            }
        }    
        return result;
    }
}