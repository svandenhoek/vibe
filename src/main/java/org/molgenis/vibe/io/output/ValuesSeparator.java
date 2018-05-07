package org.molgenis.vibe.io.output;

/**
 * Defines the possible {@code separators} for {@link SeparatedValuesFileOutputWriter}{@code s}.
 */
public enum ValuesSeparator {
    COMMA(","),
    TAB("\t"),
    COLON(":"),
    SEMICOLON(";"),
    HYPHEN_MINUS("-"),
    VERTICAL_LINE("|");

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
