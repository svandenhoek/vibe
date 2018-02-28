package org.molgenis.vibe.options_digestion;

/**
 * Describes what the application should do.
 */
public enum RunMode {
    NONE("none"), GET_GENES_USING_SINGLE_PHENOTYPE("get genes matching a single phenotype");

    private String description;

    public String getDescription() {
        return description;
    }

    RunMode(String description) {
        this.description = description;
    }

    /**
     * Retrieves the {@link RunMode} based on a {@link String} existing out of ONLY numbers.
     * @param i a {@link String} that contains a number (and nothing else).
     * @return the {@link RunMode} belonging to the given number {@code i}
     * @throws NumberFormatException see {@link Integer#parseInt(String)}
     */
    public static RunMode retrieve(String i) throws NumberFormatException {
        return retrieve(Integer.parseInt(i));
    }

    /**
     * Retrieves the {@link RunMode} based on a {@code int}.
     * @param i an {@code int} defining the {@link RunMode}
     * @return the {@link RunMode} belonging to the given number {@code i}
     */
    public static RunMode retrieve(int i) {
        switch (i) {
            case 1:
                return GET_GENES_USING_SINGLE_PHENOTYPE;
            default:
                return NONE;
        }
    }
}
