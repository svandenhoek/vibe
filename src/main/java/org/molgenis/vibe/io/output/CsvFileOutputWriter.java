package org.molgenis.vibe.io.output;

import java.nio.file.Path;

public abstract class CsvFileOutputWriter extends FileOutputWriter {
    public CsvFileOutputWriter(Path path) {
        super(path);
    }
}
