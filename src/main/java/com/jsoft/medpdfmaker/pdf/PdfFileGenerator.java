package com.jsoft.medpdfmaker.pdf;

import com.jsoft.medpdfmaker.AppProperties;
import com.jsoft.medpdfmaker.Constants;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.domain.ServiceRecordKey;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import com.jsoft.medpdfmaker.util.LoggerUtil;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.io.MemoryUsageSetting.setupTempFileOnly;

public class PdfFileGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(PdfFileGenerator.class);

    private final MemberPdfGenerator memberPdfGenerator;
    private final AppProperties appProperties;

    public PdfFileGenerator(AppProperties appProperties, final MemberPdfGenerator memberPdfGenerator) {
        this.appProperties = appProperties;
        this.memberPdfGenerator = memberPdfGenerator;
    }

    public void generate(final Path workFolder, final String outFileName,
                         final ServiceRecordRepository repository) throws IOException {
        if (repository.isEmpty()) {
            LoggerUtil.info(LOG, "No data was provided for PDF generation");
            return;
        }
        final List<Path> buffer = new ArrayList<>();
        int mergeCount = 1;
        for (final ServiceRecordKey key : repository.getKeys()) {
            final List<ServiceRecord> serviceRecords = repository.getGroupByKey(key);
            final List<Path> pages = memberPdfGenerator.generate(workFolder, serviceRecords);
            if (appProperties.isCompositePdfEnabled()) {
                for (final Path page : pages) {
                    buffer.add(page);
                    if (buffer.size() >= appProperties.getMaxPagesInPdfFile()) {
                        mergeBatchIfNeeded(buffer, outFileName, mergeCount);
                        buffer.clear();
                        mergeCount++;
                    }
                }
            }
        }
        if (appProperties.isCompositePdfEnabled()) {
            mergeBatchIfNeeded(buffer, outFileName, mergeCount);
        }
    }

    private void mergeBatchIfNeeded(List<Path> pathsForMerge, String outFileName, int mergeCount) throws IOException {
        if (pathsForMerge.size() <= 0) {
            return;
        }
        final PDFMergerUtility pdfMerger = new PDFMergerUtility();
        final String fullOutFileName = String.format("%s_%03d%s", outFileName, mergeCount, Constants.PDF_EXT);
        pdfMerger.setDestinationFileName(fullOutFileName);
        for (final Path pathToMerge : pathsForMerge) {
            pdfMerger.addSource(pathToMerge.toFile());
        }
        pdfMerger.mergeDocuments(setupTempFileOnly());
    }
}