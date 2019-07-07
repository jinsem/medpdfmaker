package com.jsoft.medpdfmaker.pdf.impl;

import com.jsoft.medpdfmaker.AppProperties;
import com.jsoft.medpdfmaker.Constants;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.domain.ServiceRecordGroupKey;
import com.jsoft.medpdfmaker.pdf.PageGenerator;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import com.jsoft.medpdfmaker.util.LoggerUtil;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.io.MemoryUsageSetting.setupTempFileOnly;

public class MembersBookGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MembersBookGenerator.class);

    private final PageGenerator pageGenerator;
    private final AppProperties appProperties;

    public MembersBookGenerator(AppProperties appProperties, final PageGenerator pageGenerator) {
        this.appProperties = appProperties;
        this.pageGenerator = pageGenerator;
    }

    public void generate(final Path workFolder, final String outFileName,
                         final ServiceRecordRepository repository) throws IOException {
        if (repository.isEmpty()) {
            LoggerUtil.info(LOG, "No data was provided for PDF generation");
            return;
        }
        final List<Path> buffer = new ArrayList<>();
        int[] mergeCount = new int[]{0};
        for (final ServiceRecordGroupKey key : repository.getKeys()) {
            final List<ServiceRecord> serviceRecords = repository.getGroupByKey(key);
            pageGenerator.generate(workFolder, serviceRecords, pagePath -> {
                    buffer.add(pagePath);
                    if (buffer.size() >= appProperties.getMaxPagesInPdfFile()) {
                        mergeBatchIfNeeded(buffer, outFileName, mergeCount[0]);
                        buffer.clear();
                        mergeCount[0]++;
                    }
            });
        }
        if (appProperties.isCompositePdfEnabled()) {
            mergeBatchIfNeeded(buffer, outFileName, mergeCount[0]);
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
