package com.jsoft.medpdfmaker.parser.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
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

    private static final String REF_ID_FIELD = "REF_ID";
    private static final String CANCELLED_FIELD = "CANCELLED";
    private static final String LNAME_FIELD = "LNAME";
    private static final String FNAME_FIELD = "FNAME";
    private static final String MEMBERID_FIELD = "MEMBERID";
    private static final String DOB_FIELD = "DOB";
    private static final String PICKUP_DATE_FIELD = "PICKUP_DATE";
    private static final String PICKUP_TIME_FIELD = "PICKUP_TIME";
    private static final String APPT_TIME_FIELD = "APPT_TIME";
    private static final String ORIGIN_FIELD = "ORIGIN";
    private static final String DESTINATION_FIELD = "DESTINATION";
    private static final String WHEELCHAIR_YESNO_FIELD = "WHEELCHAIR_YESNO";
    private static final String TOTAL_PASSENGERS_FIELD = "TOTAL_PASSENGERS";
    private static final String NOTES_FIELD = "NOTES";
    private static final String TELEPHONE_FIELD = "TELEPHONE";
    private static final String COORDINATOR_INITIALS_FIELD = "COORDINATOR_INITIALS";
    private static final String CITY_FIELD = "CITY";
    private static final String STATE_FIELD = "STATE";
    private static final String ZIPCODE_FIELD = "ZIPCODE";
    private static final String AREACODE_FIELD = "AREACODE";
    private static final String PHONE_FIELD = "PHONE";

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

    private Map<String, Object> processRow(Row currentRow) {
        return null;
    }
}