package com.jsoft.medpdfmaker;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final String HELP_OPTION = "h";
    private static final String INPUT_FILE_OPTION = "i";
    private static final String OUTPUT_FOLDER_OPTION = "o";

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    private static Options cliOptions = buildOptions();

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    private static Options buildOptions() {
        final Options result = new Options();
        result.addOption(HELP_OPTION, "help", false, "Print usage help");
        result.addRequiredOption(INPUT_FILE_OPTION, "input-file", true, "Defines path to input file that needs to be processed");
        result.addRequiredOption(OUTPUT_FOLDER_OPTION, "output-folder", true, "Defines path to folder that will contain generated PDF files");
        return result;
    }

    @Override
    public void run(String... args) throws Exception {
        final CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(cliOptions, args);
            if (cmd.hasOption(HELP_OPTION)) {
                printHelpAndExit();
            }
            if (!cmd.hasOption(INPUT_FILE_OPTION)) {
                logMissingOption(INPUT_FILE_OPTION);
                printHelpAndExit();
            }
            if (!cmd.hasOption(OUTPUT_FOLDER_OPTION)) {
                logMissingOption(OUTPUT_FOLDER_OPTION);
                printHelpAndExit();
            }
        } catch (ParseException e) {
           LOG.error("Failed to parse comand line properties", e);
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
}