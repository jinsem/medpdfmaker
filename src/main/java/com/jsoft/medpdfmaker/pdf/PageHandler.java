package com.jsoft.medpdfmaker.pdf;

import java.io.IOException;
import java.nio.file.Path;

public interface PageHandler {

    void onPage(Path pagePath) throws IOException;
}
