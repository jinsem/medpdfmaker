package com.jsoft.medpdfmaker;

import org.springframework.util.CollectionUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Application command line parameters holder.
 */
@SuppressWarnings("WeakerAccess")
public class AppParameters {

    private boolean helpRequested;
    private Path inputFile;
    private Path outputFolder;
    private List<Integer> sheetNumbers;

    private AppParameters() {
        // no op
    }

    /**
     * Return true if user requested application help. It means that no other parameters need to be parsed and analysed.
     * @return true if user requested application help. It means that no other parameters need to be parsed and analysed.
     */
    public boolean isHelpRequested() {
        return helpRequested;
    }

    /**
     * Return full path to the input file name that needs to be processed.
     * Current version of the application supports only processing of Excel files.
     * @return full path to the input file name that needs to be processed.
     */
    public String getInputFileName() {
        return (inputFile == null) ? null : inputFile.toString();
    }

    /**
     * Get File object that corresponds to inputFileName attribute.
     * @return File object that corresponds to inputFileName attribute or null if inputFileName attribute is null.
     */
    public Path getInputFile() {
        return inputFile;
    }

    /**
     * Return name of the folder that will be used to create output files.
     * Current version of the application supports only generating of PDF files.
     * Application will create separate PDF file for each sheet number provided to the application.
     * @return name of the file that will contain data extracted from input file.
     */
    public String getOutputFolderName() {
        return (outputFolder == null) ? null : outputFolder.toString();
    }

    /**
     * Get File object that corresponds to outputFolderName attribute.
     * @return File object that corresponds to outputFolderName attribute or null if outputFolderName attribute is null.
     */
    public Path getOutputFolder() {
        return outputFolder;
    }

    /**
     * Get list of the sheet numbers defined in the input files that need to be processed.
     * Each sheet of input file will be processed independently and separate PDF file will be crated for each processed
     * sheet. if sheet with provided number does not exist in the input Excel book, it will be ignored with warning
     * in the log file.
     * @return list of the sheet numbers defined in the input files that need to be processed.
     */
    public List<Integer> getSheetNumbers() {
        return sheetNumbers;
    }

    /**
     * Builder for AppParameters class instances.
     */
    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    public static class Builder {
        private boolean helpRequested = false;
        private Path inputFile;
        private Path outputFolder;
        private List<Integer> sheetNumbers;

        public Builder setHelpRequested(boolean helpRequested) {
            this.helpRequested = helpRequested;
            return this;
        }

        public Builder setInputFile(Path inputFile) {
            if (helpRequested) {
                this.inputFile = null;
            } else {
                validateRequiredPath(inputFile, "inputFile");
                this.inputFile = inputFile;
            }
            return this;
        }

        private void validateRequiredPath(Path value, String attrName) {
            if (value == null) {
                throw new IllegalArgumentException(attrName + " value cannot be empty or blank");
            }
        }

        public Builder setOutputFolder(Path outputFile) {
            if (helpRequested) {
                this.outputFolder = null;
            } else {
                validateRequiredPath(outputFile, "outputFolderName");
                this.outputFolder = outputFile;
            }
            return this;
        }

        public Builder setSheetNumbers(List<Integer> sheetNumbers) {
            if (helpRequested) {
                this.sheetNumbers = Collections.emptyList();
            } else {
                validateSheetNumbers(sheetNumbers);
                this.sheetNumbers = sheetNumbers;
            }
            return this;
        }

        private void validateSheetNumbers(List<Integer> numbers) {
            if (CollectionUtils.isEmpty(numbers)) {
                throw new IllegalArgumentException("Sheet numbers must contain at least 1 sheet number that needs to be processed");
            }
        }

        public AppParameters build() {
            if (!helpRequested) {
                validateSheetNumbers(sheetNumbers);
                validateRequiredPath(inputFile, "inputFileName");
                validateRequiredPath(outputFolder, "outputFolderName");
            }
            final AppParameters result = new AppParameters();
            result.helpRequested = helpRequested;
            result.inputFile = inputFile;
            result.outputFolder = outputFolder;
            result.sheetNumbers = sheetNumbers;
            return result;
        }
    }
}
