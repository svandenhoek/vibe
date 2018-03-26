package org.molgenis.vibe.io.output;

import java.nio.file.Path;

/**
 * Abstract class for CSV formatted files.
 */
public abstract class CsvFileOutputWriter extends FileOutputWriter {
    /**
     * Separator to be used in CSV files (comma-separated).
     */
    protected static final String SEPARATOR = ",";
    /**
     * Quotes can be used to that comma's within a field aren't seen as a field-separator.
     */
    protected static final String QUOTE_MARK = "\"";

    public CsvFileOutputWriter(Path path) {
        super(path);
    }
}
