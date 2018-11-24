package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;
import com.jsoft.medpdfmaker.parser.TableFileParser;
import com.jsoft.medpdfmaker.parser.impl.ServiceRecordBuilder;
import com.jsoft.medpdfmaker.parser.impl.ServiceRecordXlsParser;
import com.jsoft.medpdfmaker.pdf.MemberPdfGenerator;
import com.jsoft.medpdfmaker.pdf.PdfFileGenerator;
import com.jsoft.medpdfmaker.repository.impl.ServiceRecordRepository;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jsoft.medpdfmaker.util.FileUtil.stripLastSlashIfNeeded;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final String HELP_OPTION = "h";
    private static final String INPUT_FILE_OPTION = "i";
    private static final String OUTPUT_FOLDER_OPTION = "o";

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    private static Options cliOptions = buildOptions();

    private AppProperties appProperties;

    @Autowired
    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    private static Options buildOptions() {
        final Options result = new Options();
        result.addOption(HELP_OPTION, "help", false, "Print usage help");
        result.addRequiredOption(INPUT_FILE_OPTION, "input-file", true, "Defines path to input file that needs to be processed");
        result.addOption(OUTPUT_FOLDER_OPTION, "output-folder", true,
                "(Optional) Defines path to folder were the generated PDF file should be placed. If not set, PDF file will be placed in the folder where input file is located");
        return result;
    }

    @Override
    public void run(String... args) throws Exception {
        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine cmd = parser.parse(cliOptions, args);
            verifyCommandLine(cmd);
            generatePdf(cmd);
        } catch (ParseException e) {
           LOG.error("Failed to parse command line properties", e);
           printHelpAndExit();
        }
    }

    private void verifyCommandLine(CommandLine cmd) {
		if (cmd.hasOption(HELP_OPTION)) {
		    printHelpAndExit();
		}
		if (!cmd.hasOption(INPUT_FILE_OPTION)) {
		    logMissingOption(INPUT_FILE_OPTION);
		    printHelpAndExit();
		}
    }

    private void logMissingOption(String name) {
        LOG.error("Missing {} option", name);
    }

    private void printHelpAndExit() {
        final HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", cliOptions);
        System.exit(0);
    }

	private void generatePdf(CommandLine cmd) throws IOException {
        //TODO refactor this method!
        final String inputFileName = cmd.getOptionValue(INPUT_FILE_OPTION);
        final String outputFolderName = getOutputFolder(cmd);
        final ObjectBuilder<ServiceRecord> builder = new ServiceRecordBuilder();
        final TableFileParser<ServiceRecord> parser = new ServiceRecordXlsParser(builder);
        File inputFile = new File(inputFileName);
        final ServiceRecordRepository repository = new ServiceRecordRepository();
        parser.parse(inputFile, rowObj -> repository.put(rowObj.getMemberId(), rowObj));
        MemberPdfGenerator memberPdfGenerator = new MemberPdfGenerator(appProperties);
        PdfFileGenerator pdfFileGenerator = new PdfFileGenerator(memberPdfGenerator);
        final String outFileName = makeOutFileName(inputFileName, outputFolderName);
        pdfFileGenerator.generate(outFileName, repository);
    }

    private String getOutputFolder(CommandLine cmd) {
        if (cmd.hasOption(OUTPUT_FOLDER_OPTION)) {
            return stripLastSlashIfNeeded(cmd.getOptionValue(OUTPUT_FOLDER_OPTION));
        }
        final String inputFile = cmd.getOptionValue(INPUT_FILE_OPTION);
        return new File(inputFile).getParent();
    }

    private String makeOutFileName(String inputFileName, String outputFolderName) {
        final String baseName = FilenameUtils.getBaseName(inputFileName);
        return outputFolderName + File.separator + baseName + "-" + makeCurrentDateString() + ".pdf";
    }

    private String makeCurrentDateString() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date());
    }
}