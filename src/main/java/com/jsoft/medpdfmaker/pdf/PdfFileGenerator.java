package com.jsoft.medpdfmaker.pdf;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import com.jsoft.medpdfmaker.util.AppUtil;
import com.jsoft.medpdfmaker.util.FileUtil;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.jsoft.medpdfmaker.Constants.WORK_FOLDER_PREF;
import static org.apache.pdfbox.io.MemoryUsageSetting.setupTempFileOnly;

public class PdfFileGenerator {

    private final MemberPdfGenerator memberPdfGenerator;

    public PdfFileGenerator(final MemberPdfGenerator memberPdfGenerator) {
        this.memberPdfGenerator = memberPdfGenerator;
    }

    public void generate(File outFolder, String outFileName, ServiceRecordRepository repository) throws IOException {
        if (repository.isEmpty()) {
            return;
        }
        final Path workFolder = initWorkFolder(outFolder);
        final PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(outFileName);
        for (final String key : repository.getKeys()) {
            final List<ServiceRecord> serviceRecords = repository.getGroupByKey(key);
            final List<Path> pages = memberPdfGenerator.generate(workFolder, serviceRecords);
            for (final Path page : pages) {
                pdfMerger.addSource(page.toFile());
            }
        }
        pdfMerger.mergeDocuments(setupTempFileOnly());
    }

    private Path initWorkFolder(final File outFolder) throws IOException {
        final String workFolderName = outFolder.getAbsolutePath() + File.separator + WORK_FOLDER_PREF + AppUtil.curDateTimeAsString();
        return Files.createDirectory(Paths.get(workFolderName));
    }
}