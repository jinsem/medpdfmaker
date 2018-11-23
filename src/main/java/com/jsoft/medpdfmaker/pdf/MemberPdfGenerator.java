package com.jsoft.medpdfmaker.pdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.jsoft.medpdfmaker.Constants;
import com.jsoft.medpdfmaker.PropertyName;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.core.env.Environment;

public class MemberPdfGenerator {

    private static final int ROWS_COUNT = 6;

    private Environment environment;

    public MemberPdfGenerator(final Environment environment) {
        this.environment = environment;
    }

    public List<Path> generate(final Path workFolder, final List<ServiceRecord> memberServiceRecords) throws IOException {

        if (CollectionUtils.isEmpty(memberServiceRecords)) {
            return Collections.emptyList();
        }
        final List<Path> result = new LinkedList<>();
        int pageNum = 1;
        final ServiceRecord headerRecord = memberServiceRecords.get(0);
        List<ServiceRecord> pageRecords = new LinkedList<>();
        for (final ServiceRecord memberServiceRecord : memberServiceRecords) {
            pageRecords.add(memberServiceRecord);
            if (pageRecords.size() == ROWS_COUNT) {
                result.add(generatePage(pageNum, headerRecord, pageRecords, workFolder));
                pageRecords = new LinkedList<>();
                pageNum++;
            }
        }
        if (!pageRecords.isEmpty()) {
            result.add(generatePage(pageNum, headerRecord, pageRecords, workFolder));
        }
        return result;
    }

    private Path generatePage(int pageNum, ServiceRecord headerRecord, List<ServiceRecord> pageRecords,
                              Path workFolder) throws IOException {
        final String pageFileName = makePageFileName(headerRecord, pageNum, workFolder);
        try (InputStream templateStream = getTemplateStream()) {
            final PDDocument pdDocument = PDDocument.load(templateStream);
            final String placeOfServiceValue = environment.getProperty(PropertyName.PLACE_OF_SERVICE);
            final String proceduresValue = environment.getProperty(PropertyName.PROCEDURES);
            final String chargesValue = environment.getProperty(PropertyName.CHARGES);
            final String reservedText = environment.getProperty(PropertyName.RESERVED);
            final String federalTaxIDValue = environment.getProperty(PropertyName.FEDERAL_TAX_ID);
            final String providerValue = environment.getProperty(PropertyName.PROVIDER);

            pdDocument.save(pageFileName);
        }
        return Paths.get(pageFileName);
    }

    private String makePageFileName(ServiceRecord headerRecord, int pageNum, Path workFolder) {
        final String normalizedMemberId = headerRecord.getMemberId().replaceAll("[^a-zA-Z0-9.-]", "_");
        return FilenameUtils.getFullPathNoEndSeparator(workFolder.toFile().getAbsolutePath()) +
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

    public static void setField(final PDDocument pdDocument, final String fName, final String fValue) throws IOException {
        final PDDocumentCatalog pdDocumentCatalog = pdDocument.getDocumentCatalog();
        final PDAcroForm pdAcroForm = pdDocumentCatalog.getAcroForm();
        final PDField field = pdAcroForm.getField(fName);
        if (field == null) {
            throw new IllegalArgumentException(String.format("No field %s found in PDF documeent", fName));
        } else {
            field.setValue(fValue);
        }
    }
}
