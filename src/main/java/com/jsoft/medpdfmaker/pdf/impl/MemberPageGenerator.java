package com.jsoft.medpdfmaker.pdf.impl;

import com.jsoft.medpdfmaker.AppProperties;
import com.jsoft.medpdfmaker.Constants;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.pdf.PageGenerator;
import com.jsoft.medpdfmaker.pdf.PageHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Provider;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static com.jsoft.medpdfmaker.Constants.INSURANCE_TYPE;
import static com.jsoft.medpdfmaker.Constants.RENDER_PROVIDER;
import static com.jsoft.medpdfmaker.Constants.SIGNATURE_ON_FILE;
import static com.jsoft.medpdfmaker.util.FileUtil.stripLastSlashIfNeeded;

public class MemberPageGenerator implements PageGenerator {


    private static final int ROWS_COUNT = 5;
    private static final String N_A = "";

    private final AppProperties appProperties;

    private final DateTimeFormatter formatYearCentury = DateTimeFormatter.ofPattern("yy");
    private final DateTimeFormatter formatDay = DateTimeFormatter.ofPattern("dd");
    private final DateTimeFormatter formatMonth = DateTimeFormatter.ofPattern("MM");
    private final DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final DecimalFormat formatMoney = new DecimalFormat("0.00");

    public MemberPageGenerator(final AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void generate(Path workFolder, List<ServiceRecord> memberServiceRecords, PageHandler pageHandler) throws IOException {
        final ServiceRecord headerRecord = memberServiceRecords.get(0);
        List<ServiceRecord> pageRecords = new LinkedList<>();
        final PageInfo pageInfo = new PageInfo(memberServiceRecords.size());
        for (final ServiceRecord memberServiceRecord : memberServiceRecords) {
            pageInfo.incPageRecordsCount();
            pageRecords.add(memberServiceRecord);
            if (pageRecords.size() == ROWS_COUNT) {
                pageHandler.onPage(generatePage(pageInfo, headerRecord, pageRecords, workFolder));
                pageRecords = new LinkedList<>();
                pageInfo.incPageNum();
                pageInfo.resetPageRecordsCount();
            }
        }
        if (!pageRecords.isEmpty()) {
            pageHandler.onPage(generatePage(pageInfo, headerRecord, pageRecords, workFolder));
        }
    }

    private Path generatePage(PageInfo pageInfo, ServiceRecord headerRecord,
                              List<ServiceRecord> pageRecords, Path workFolder) throws IOException {
        final String pageFileName = makePageFileName(headerRecord, pageInfo.pageNum, workFolder);
        try (InputStream templateStream = getTemplateStream();
             PDDocument pdDocument = PDDocument.load(templateStream);) {
            fillPageHeader(pdDocument, headerRecord, pageRecords);
            fillPageTable(pdDocument, pageRecords);
            fillPageFooter(pdDocument, pageInfo, headerRecord);
            pdDocument.save(pageFileName);
        }
        return Paths.get(pageFileName);
    }

    private void fillPageHeader(PDDocument pdDocument, ServiceRecord headerRecord, List<ServiceRecord> pageRecords) throws IOException {
        String memberIdPage = headerRecord.getMemberId();
        setField(pdDocument, "Text1", memberIdPage);
        setField(pdDocument, "Text2", headerRecord.getFAndLNameReversed());
        String origin = headerRecord.getOrigin();
        int originSlashPos = (origin == null) ? -1 : origin.indexOf('/');
        if (originSlashPos > -1) {
            setField(pdDocument,"Text3", origin.substring(0, originSlashPos));
        } else {
            setField(pdDocument,"Text3", origin);
        }
        if (StringUtils.isNotEmpty(headerRecord.getCity())) {
            setField(pdDocument,"Text4", headerRecord.getCity());
        }
        if (StringUtils.isNotEmpty(headerRecord.getState())) {
            setField(pdDocument,"Text5", headerRecord.getState());
        }
        if (StringUtils.isNotEmpty(headerRecord.getZipCode())) {
            setField(pdDocument,"Text6", headerRecord.getZipCode());
        }
        if (StringUtils.isNotEmpty(headerRecord.getAreaCode())) {
            setField(pdDocument,"Text7", headerRecord.getAreaCode());
        }
        if (StringUtils.isNotEmpty(headerRecord.getPhone())) {
            setField(pdDocument,"Text8", headerRecord.getPhone());
        }
        fillDob(pdDocument, headerRecord);
        fillSex(pdDocument, headerRecord);
        setField(pdDocument,"Text54", appProperties.getFederalTaxID());
        setField(pdDocument,"Text57", appProperties.getProvider());
        setField(pdDocument,"PATIENT_RELATIONSHIP_SELF", "X");
        setField(pdDocument, INSURANCE_TYPE, "X");
        setField(pdDocument, "PATIENT_SIGNATURE", SIGNATURE_ON_FILE);
        setField(pdDocument, "PATIENT_SIGNATURE_13", SIGNATURE_ON_FILE);
        LocalDate signatureDate = headerRecord.getPickupDate().withDayOfMonth(headerRecord.getPickupDate().lengthOfMonth());
        setField(pdDocument, "PATIENT_SIGNATURE_DATE", formatDate.format(signatureDate));
        setField(pdDocument,"EIN", "X");
        setField(pdDocument,"ACCEPT_ASSIGNMENT_YES", "X");
        setField(pdDocument,"SIGNATURE_DATE", " " + formatDate.format(signatureDate));
        setField(pdDocument,"SERVICE_LOCATION", generateServiceLocation(pageRecords));
        setField(pdDocument,"NPI_32A", RENDER_PROVIDER);
        setField(pdDocument,"NPI_33A", RENDER_PROVIDER);
        setField(pdDocument,"SIGNATURE_OF_SUPPLIER", SIGNATURE_ON_FILE);
        setField(pdDocument,"INSURED_ADDRESS", "SAME");
    }

    private void fillSex(PDDocument pdDocument, ServiceRecord headerRecord) throws IOException {
        if ("M".equals(headerRecord.getGender())) {
            setField(pdDocument,"SEX_M", "X");
            setField(pdDocument,"SEX_M_2", "X");
        } else if ("F".equals(headerRecord.getGender())) {
            setField(pdDocument,"SEX_F", "X");
            setField(pdDocument,"SEX_F_2", "X");
        }
    }

    private void fillDob(PDDocument pdDocument, ServiceRecord headerRecord) throws IOException {
        LocalDate dob = headerRecord.getDayOfBirth();
        if (dob == null) {
            setField(pdDocument,"Text9", N_A);
            setField(pdDocument,"Text10", N_A);
            setField(pdDocument,"Text11", N_A);
            setField(pdDocument,"DOB_3_MM", N_A);
            setField(pdDocument,"DOB_3_DD", N_A);
            setField(pdDocument,"DOB_3_YYYY", N_A);
        } else {
            setField(pdDocument,"Text9", formatMonth.format(dob));
            setField(pdDocument,"Text10", formatDay.format(dob));
            setField(pdDocument,"Text11", formatYearCentury.format(dob));
            setField(pdDocument,"DOB_3_MM", formatMonth.format(dob));
            setField(pdDocument,"DOB_3_DD", formatDay.format(dob));
            setField(pdDocument,"DOB_3_YYYY", formatYearCentury.format(dob));
        }
    }

    private String generateServiceLocation(List<ServiceRecord> pageRecords) {
        final StringBuilder result = new StringBuilder();
        boolean needNewLine = false;
        for (ServiceRecord pageRecord : pageRecords) {
            if (needNewLine) {
                result.append("\n");
            } else {
                needNewLine = true;
            }
            result.append(pageRecord.getOrigin()).append(" - ").append(pageRecord.getDestination());
        }
        return result.toString();
    }

    private void fillPageTable(PDDocument pdDocument, List<ServiceRecord> pageRecords) throws IOException {
        int recNum = 0;
        for (final ServiceRecord pageRecord : pageRecords) {
            int fieldIdxShift = recNum * 7;
            LocalDate pickUpDate = pageRecord.getPickupDate();
            setField(pdDocument,"Text" + (12 + fieldIdxShift), formatMonth.format(pickUpDate));
            setField(pdDocument,"Text" + (13 + fieldIdxShift), formatDay.format(pickUpDate));
            setField(pdDocument,"Text" + (14 + fieldIdxShift), formatYearCentury.format(pickUpDate));
            setField(pdDocument,"Text" + (15 + fieldIdxShift), appProperties.getPlaceOfService());
            setField(pdDocument,"Text" + (16 + fieldIdxShift), appProperties.getProcedures());
            setField(pdDocument,"Text" + (17 + fieldIdxShift), formatMoney.format(pageRecord.getTripPrice()));
            setField(pdDocument,"Text" + (18 + fieldIdxShift), pageRecord.getRefId());
            setField(pdDocument,"G_DAYS_" + (recNum + 1), "1");
            setField(pdDocument,"RENDER_PROVIDER_" + (recNum + 1), RENDER_PROVIDER);
            recNum++;
        }
    }

    private void fillPageFooter(PDDocument pdDocument, PageInfo pageInfo, ServiceRecord headerRecord) throws IOException {
        setField(pdDocument,"Text56", formatMoney.format(headerRecord.getTripPrice().multiply(BigDecimal.valueOf(pageInfo.pageRecordsCount))));
    }

    private String makePageFileName(ServiceRecord headerRecord, int pageNum, Path workFolder) {
        final String normalizedMemberId = String.format("%s_(%s)", headerRecord.getMemberId(), headerRecord.getTripPrice().toString())
                .replaceAll("[^a-zA-Z0-9.-]", "_");
        return stripLastSlashIfNeeded(workFolder.toFile().getAbsolutePath()) +
                File.separator +
                String.format("%s_%03d.pdf", normalizedMemberId, pageNum);
    }

    private InputStream getTemplateStream() {
        final InputStream result = this.getClass().getClassLoader().getResourceAsStream(Constants.PDF_TEMPLATE_RESOURCE_PATH);
        if (result == null) {
            throw new IllegalStateException(String.format("Template resource %s is not found", Constants.PDF_TEMPLATE_RESOURCE_PATH));
        } else {
            return result;
        }
    }

    private static void setField(final PDDocument pdDocument, final String fName, final String fValue) throws IOException {
        final PDDocumentCatalog pdDocumentCatalog = pdDocument.getDocumentCatalog();
        final PDAcroForm pdAcroForm = pdDocumentCatalog.getAcroForm();
        final PDField field = pdAcroForm.getField(fName);
        if (field == null) {
            throw new IllegalArgumentException(String.format("No field %s found in PDF documeent", fName));
        } else {
            field.setValue(fValue);
        }
    }

    private static class PageInfo {

        int pageNum;
        int pageRecordsCount;
        final int totalRecordsCount;
        final int pageCount;

        // memberServiceRecords
        PageInfo(int totalRecordsCount) {
            this.totalRecordsCount = totalRecordsCount;
            this.pageNum = 1;
            resetPageRecordsCount();
            this.pageCount = (int)Math.round(Math.ceil((double) totalRecordsCount / ROWS_COUNT));
        }

        void incPageNum() {
            pageNum++;
        }

        void incPageRecordsCount() {
            pageRecordsCount++;
        }

        void resetPageRecordsCount() {
            pageRecordsCount = 0;
        }

        boolean lastPage() {
            return pageNum == pageCount;
        }
    }
}
