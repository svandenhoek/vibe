package org.molgenis.vibe.cli.output;

/**
 * Defines possible {@code separators} to be used.
 */
public enum ValuesSeparator {
    COMMA(","),
    TAB("\t"),
    COLON(":"),
    SEMICOLON(";"),
    HYPHEN_MINUS("-"),
    VERTICAL_LINE("|"),
    EQUALS("=");

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
