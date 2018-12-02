package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.exception.ParametersParsingException;
import com.jsoft.medpdfmaker.exception.ParseException;
import com.jsoft.medpdfmaker.parser.Result;
import com.jsoft.medpdfmaker.parser.TableFileParser;
import com.jsoft.medpdfmaker.parser.ValueExtractor;
import com.jsoft.medpdfmaker.parser.impl.ServiceRecordBuilder;
import com.jsoft.medpdfmaker.parser.impl.ServiceRecordXlsParser;
import com.jsoft.medpdfmaker.pdf.MemberPdfGenerator;
import com.jsoft.medpdfmaker.pdf.PdfFileGenerator;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import com.jsoft.medpdfmaker.util.LoggerUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.jsoft.medpdfmaker.Constants.PDF_EXT;
import static com.jsoft.medpdfmaker.util.AppUtil.curDateTimeAsString;
import static com.jsoft.medpdfmaker.util.FileUtil.toOutName;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private AppProperties appProperties;
    private AppParametersParser appParametersParser;
    private List<ValueExtractor> extractors;

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Autowired
    public void setAppParametersParser(AppParametersParser appParametersParser) {
        this.appParametersParser = appParametersParser;
    }

    @Autowired
    public void setExtractors(List<ValueExtractor> extractors) {
        this.extractors = extractors;
    }

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            final AppParameters appParameters = appParametersParser.parse(args);
            if (appParameters.isHelpRequested()) {
                printHelpAndExis();
            }
            generatePdf(appParameters);
            LoggerUtil.info(LOG, "Data processing completed successfully!");
        } catch (ParametersParsingException e) {
            LoggerUtil.info(LOG, String.format("Value of one or more application parameters was invalid: %s. " +
                    "Please try again by providing correct parameters values.", e.getMessage()));
            printHelpAndExis();
        } catch (Exception e) {
            LoggerUtil.info(LOG, String.format("Data processing failed: %s. " +
                    "Please look into application log file for details.", e.getMessage()));
            throw e;
        }
    }

    private void printHelpAndExis() {
        appParametersParser.printHelp();
        System.exit(0);
    }

    private void generatePdf(AppParameters appParameters) throws IOException {
        final TableFileParser<ServiceRecord> parser = new ServiceRecordXlsParser(new ServiceRecordBuilder(extractors));
        final ServiceRecordRepository repository = new ServiceRecordRepository();
        final MemberPdfGenerator memberPdfGenerator = new MemberPdfGenerator(appProperties);
        final PdfFileGenerator pdfFileGenerator = new PdfFileGenerator(memberPdfGenerator);
        LoggerUtil.info(LOG, "Start parsing input file " + appParameters.getInputFileName());
        for (final int sheetIdx : appParameters.getSheetNumbers()) {
            LoggerUtil.info(LOG, String.format("Processing sheet #%d", sheetIdx));
            try {
                final Result result = parser.parse(appParameters.getInputFile().toFile(), sheetIdx, rowObj -> repository.put(rowObj.getMemberId(), rowObj));
                switch (result) {
                    case WARNING:
                        LoggerUtil.info(LOG, String.format("Data from sheet %d was processed without errors, but some warnings was reported", sheetIdx));
                        generatePdf(repository, pdfFileGenerator, appParameters, sheetIdx);
                        break;
                    case OK:
                        generatePdf(repository, pdfFileGenerator, appParameters, sheetIdx);
                        break;
                    default:
                        throw new ParseException();
                }
            } finally {
                repository.clean();
            }
        }
    }

    private void generatePdf(ServiceRecordRepository repository, PdfFileGenerator pdfFileGenerator,
                             AppParameters appParameters, int sheetIdx) throws IOException {
        final String curDateStr = curDateTimeAsString();
        final String outFileName = makeOutFileName(appParameters, sheetIdx, curDateStr);
        LoggerUtil.info(LOG, String.format("Writing data to PDF file %s", outFileName));
        final Path workFolder = createWorkFolder(appParameters, sheetIdx, curDateStr);
        pdfFileGenerator.generate(workFolder, outFileName, repository);
    }

    private String makeOutFileName(AppParameters appParameters, int sheetIdx, String curDateStr) {
        final String baseName = FilenameUtils.getBaseName(appParameters.getInputFile().toString());
        Path result = Paths.get(appParameters.getOutputFolder().toString(), toOutName(baseName, sheetIdx, curDateStr, PDF_EXT));
        return result.toString();
    }

    private Path createWorkFolder(AppParameters appParameters, int sheetIdx, String curDateStr) throws IOException {
        final String baseName = FilenameUtils.getBaseName(appParameters.getInputFile().toString());
        final Path workDirectory = Paths.get(appParameters.getOutputFolder().toString(), toOutName(baseName, sheetIdx, curDateStr));
        return Files.createDirectory(workDirectory);
    }
}