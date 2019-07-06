package com.jsoft.medpdfmaker.pdf;

import com.jsoft.medpdfmaker.domain.ServiceRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface PageGenerator {

    void generate(final Path workFolder, final List<ServiceRecord> memberServiceRecords, PageHandler pageHandler) throws IOException;
}
