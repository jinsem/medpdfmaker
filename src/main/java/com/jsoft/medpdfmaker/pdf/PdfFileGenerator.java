package com.jsoft.medpdfmaker.pdf;

import com.jsoft.medpdfmaker.Constants;
import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.apache.pdfbox.io.MemoryUsageSetting.setupTempFileOnly;

public class PdfFileGenerator {

    private MemberPdfGenerator memberPdfGenerator;

    public PdfFileGenerator(final MemberPdfGenerator memberPdfGenerator) {
        this.memberPdfGenerator = memberPdfGenerator;
    }

    public void generate(String outFileName, ServiceRecordRepository repository) throws IOException {
        if (repository.isEmpty()) {
            return;
        }
        Path workFolder = null;
        try {
            final PDFMergerUtility pdfMerger = new PDFMergerUtility();
            pdfMerger.setDestinationFileName(outFileName);
            workFolder = initWorkFolder();
            for(final String key : repository.getKeys()) {
                final List<ServiceRecord> serviceRecords = repository.getGroupByKey(key);
                List<Path> pages = memberPdfGenerator.generate(workFolder, serviceRecords);
                for (final Path page : pages) {
                    pdfMerger.addSource(page.toFile());
                }
            }
            pdfMerger.mergeDocuments(setupTempFileOnly());
        } finally {
            if (workFolder != null) {
                FileUtils.cleanDirectory(workFolder.toFile());
                Files.delete(workFolder);

            }
        }
    }

    private Path initWorkFolder() throws IOException {
        return Files.createTempDirectory(Constants.TMP_FOLDER_PATH, Constants.WORK_FOLDER_PREF);
    }
}