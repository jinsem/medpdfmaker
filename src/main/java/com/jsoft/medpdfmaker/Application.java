package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.exception.ParametersParsingException;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import com.jsoft.medpdfmaker.parser.TableFileParser;
import com.jsoft.medpdfmaker.parser.impl.ServiceRecordBuilder;
import com.jsoft.medpdfmaker.parser.impl.ServiceRecordXlsParser;
import com.jsoft.medpdfmaker.pdf.MemberPdfGenerator;
import com.jsoft.medpdfmaker.pdf.PdfFileGenerator;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.jsoft.medpdfmaker.util.AppUtil.curDateTimeAsString;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private AppProperties appProperties;
    private AppParametersParser appParametersParser;

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
        } catch (ParametersParsingException e) {
            printHelpAndExis();
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
        for (final int sheetIdx : appParameters.getSheetNumbers()) {
            parser.parse(appParameters.getInputFile(), sheetIdx, rowObj -> repository.put(rowObj.getMemberId(), rowObj));
            final String outFileName = makeOutFileName(appParameters.getInputFile(), sheetIdx, appParameters.getOutputFolder());
            pdfFileGenerator.generate(outFileName, repository);
            repository.clean();
        }
    }

    private String makeOutFileName(File inputFile, int sheetIdx, File outputFolder) {
        final String baseName = FilenameUtils.getBaseName(inputFile.getAbsolutePath());
        return outputFolder.getAbsolutePath() + File.separator + baseName + "-" + sheetIdx + "-" + curDateTimeAsString() + ".pdf";
    }
}