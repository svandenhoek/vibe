package org.molgenis.vibe.io.output;

/**
 * Defines the possible {@code separators} for {@link SeparatedValuesFileOutputWriter}{@code s}.
 */
public enum ValuesSeparator {
    COMMA(","),
    TAB("\t"),
    SEMICOLON(";");

    /**
     * The separator character.
     */
    private String separator;

    public String getSeparator() {
        return separator;
    }

    ValuesSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return getSeparator();
    }
}
