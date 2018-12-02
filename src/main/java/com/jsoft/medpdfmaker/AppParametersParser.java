package com.jsoft.medpdfmaker;

import com.jsoft.medpdfmaker.exception.ParametersParsingException;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class AppParametersParser {

    private static final String HELP_OPTION = "h";
    public static final String HELP_OPTION_FULL = "help";

    private static final String OUTPUT_FOLDER_OPTION = "o";
    public static final String OUTPUT_FOLDER_OPTION_FULL = "output-folder";

    private static final String INPUT_FILE_SHEETS_TO_PROCESS = "s";
    public static final String INPUT_FILE_SHEETS_TO_PROCESS_FULL = "sheet-numbers";

    private final Options cliOptions = buildOptions();

    private Options buildOptions() {
        final Options result = new Options();
        result.addOption(HELP_OPTION, HELP_OPTION_FULL, false,
                "Print application usage help");
        result.addOption(OUTPUT_FOLDER_OPTION, OUTPUT_FOLDER_OPTION_FULL, true,
                "Path to folder were the generated PDF file(s) should be placed. If not set, PDF file(s) will be placed in the folder where input file is located." +
                           "If processing of multiply sheets is requested, application will create separate PDF file for each processed sheet.");
        result.addOption(INPUT_FILE_SHEETS_TO_PROCESS, INPUT_FILE_SHEETS_TO_PROCESS_FULL, true,
                "Comma separated list of sheet numbers that must be processed by the application. If paarmeters is not set, only the information from the 1st sheet in the input Excel book will be processed. " +
                           "Numbers are zero bases, so first sheet has index 0, second sheet has index 1 and so on.");
        return result;
    }

    public AppParameters parse(String... args) {
        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine cmd = parser.parse(cliOptions, args);
            if (cmd.hasOption(HELP_OPTION)) {
                return buildHelpAppParameters();
            } else {
                return buildAppParameters(cmd);
            }
        } catch (ParseException e) {
            throw new ParametersParsingException("Cannot parse application paarmeters", e);
        }
    }

    private AppParameters buildHelpAppParameters() {
        final AppParameters.Builder builder = new AppParameters.Builder();
        builder.setHelpRequested(true);
        return builder.build();
    }

    private AppParameters buildAppParameters(CommandLine cmd) {
        final AppParameters.Builder resultBuilder = new AppParameters.Builder();
        final String inputFileName = getInputFileNameParameter(cmd);
        setInputFileName(inputFileName, resultBuilder);
        setOutputFolderName(inputFileName, cmd.getOptionValue(OUTPUT_FOLDER_OPTION), resultBuilder);
        setSheetNumbers(cmd.getOptionValue(INPUT_FILE_SHEETS_TO_PROCESS), resultBuilder);
        return resultBuilder.build();
    }

    private String getInputFileNameParameter(CommandLine cmd) {
        final String[] parameters = cmd.getArgs();
        if (parameters.length == 0) {
            throw new ParametersParsingException("Input file path is required and cannot be empty");
        }
        if (parameters.length > 1) {
            throw new ParametersParsingException("Multiply input file paths are provided, or input file path is not taken into quotes");
        }
        return parameters[0];
    }

    private void setInputFileName(String inputFileName, AppParameters.Builder resultBuilder) {
        final Path fileToVerify = Paths.get(inputFileName);
        if (!fileToVerify.toFile().exists()) {
            throw new ParametersParsingException(inputFileName + " cannot be found. Please make sure that file name is set correctly");
        }
        if (!fileToVerify.toFile().canRead()) {
            throw new ParametersParsingException(inputFileName + " cannot be read. Please make sure current system user has permissions to read this file");
        }
        resultBuilder.setInputFile(fileToVerify);
    }

    private void setOutputFolderName(String inputFileName, String outputFolderName, AppParameters.Builder resultBuilder) {
        final Path outputFolder;
        if (StringUtils.isBlank(outputFolderName)) {
            // it is OK. just use folder that contains input file
            outputFolder = Paths.get(inputFileName).getParent();
        } else {
            outputFolder = Paths.get(outputFolderName);
        }
        if (!outputFolder.toFile().exists()) {
            throw new ParametersParsingException(outputFolder + " cannot be found. " +
                    "Please make sure that output folder name is set correctly");
        }
        if (!outputFolder.toFile().isDirectory()) {
            throw new ParametersParsingException(outputFolder + " is not a directory. " +
                    "Please make sure that this parameters contains valid path to a directory, not to a file");
        }
        if (!outputFolder.toFile().canWrite()) {
            throw new ParametersParsingException("The application cannot create files in the " + outputFolder + " directory. " +
                    "Please make sure that current system user has write access to this directory");
        }
        resultBuilder.setOutputFolder(outputFolder);
    }

    private void setSheetNumbers(String optionValue, AppParameters.Builder resultBuilder) {
        final List<Integer> sheetNumbers = new LinkedList<>();
        if (StringUtils.isBlank(optionValue)) {
            // it is OK. Just parse the 1st sheet
            sheetNumbers.add(0);
        } else {
            final String[] strNumbers = optionValue.split(",");
            for (final String strNumber : strNumbers) {
                final String tmpStr = StringUtils.trim(strNumber);
                if (StringUtils.isEmpty(tmpStr)) {
                    throw new ParametersParsingException("All sheet numbers must be set and cannot be empty");
                }
                int intNumber;
                try {
                    intNumber = Integer.parseInt(strNumber);
                } catch (Exception e) {
                    throw new ParametersParsingException("All sheet numbers must be numbers. Incorrect value: " + strNumber);
                }
                if (intNumber < 0) {
                    throw new ParametersParsingException("All sheet numbers must be greater of equal to 0. Incorrect value: " + intNumber);
                }
                sheetNumbers.add(intNumber);
            }
        }
        resultBuilder.setSheetNumbers(sheetNumbers);
    }

    public void printHelp() {
        new HelpFormatter().printHelp("medpdfmaker <input-file-name>", cliOptions, true);
    }
}
