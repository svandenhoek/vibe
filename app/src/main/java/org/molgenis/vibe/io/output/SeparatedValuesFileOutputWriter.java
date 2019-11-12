package org.molgenis.vibe.io.output;

import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

/**
 * Abstract class for files where fields are separated by a specific character.
 */
public abstract class SeparatedValuesFileOutputWriter extends FileOutputWriter {
    /**
     * Quotes can be used to that comma's within a field aren't seen as a field-separator.
     */
    protected static final String QUOTE_MARK = "\"";

    /**
     * The separator to be used for dividing values.
     */
    private final ValuesSeparator separator;

    public ValuesSeparator getSeparator() {
        return separator;
    }

    public SeparatedValuesFileOutputWriter(Path path, ValuesSeparator separator) {
        super(path);
        this.separator = requireNonNull(separator);
    }
}
