package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.exception.ParametersParsingException;
import com.jsoft.medpdfmaker.parser.TableFileParser;
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

import java.io.File;
import java.io.IOException;

import static com.jsoft.medpdfmaker.util.AppUtil.curDateTimeAsString;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private AppProperties appProperties;
    private AppParametersParser appParametersParser;

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Autowired
    public void setAppParametersParser(AppParametersParser appParametersParser) {
        this.appParametersParser = appParametersParser;
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
        final TableFileParser<ServiceRecord> parser = new ServiceRecordXlsParser(new ServiceRecordBuilder());
        final ServiceRecordRepository repository = new ServiceRecordRepository();
        final MemberPdfGenerator memberPdfGenerator = new MemberPdfGenerator(appProperties);
        final PdfFileGenerator pdfFileGenerator = new PdfFileGenerator(memberPdfGenerator);
        LoggerUtil.info(LOG, "Start parsing input file " + appParameters.getInputFileName());
        for (final int sheetIdx : appParameters.getSheetNumbers()) {
            LoggerUtil.info(LOG, String.format("Processing sheet #%d", sheetIdx));
            final String outFileName = makeOutFileName(appParameters, sheetIdx);
            parser.parse(appParameters.getInputFile(), sheetIdx, rowObj -> repository.put(rowObj.getMemberId(), rowObj));
            LoggerUtil.info(LOG, String.format("Writing data to PDF file %s", outFileName));
            pdfFileGenerator.generate(outFileName, repository);
            repository.clean();
        }
    }

    private String makeOutFileName(AppParameters appParameters, int sheetIdx) {
        final String baseName = FilenameUtils.getBaseName(appParameters.getInputFile().getAbsolutePath());
        return appParameters.getOutputFolder().getAbsolutePath() + File.separator +
                baseName + "[" + sheetIdx + "]" + curDateTimeAsString() + ".pdf";
    }
}