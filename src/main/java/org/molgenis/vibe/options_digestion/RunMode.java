package org.molgenis.vibe.options_digestion;

public enum RunMode {
    NONE, GET_GENES_WITH_SINGLE_HPO;

    /**
     *
     * @param i
     * @return
     * @throws NumberFormatException see {@link Integer#parseInt(String)}
     */
    public static RunMode getMode(String i) throws NumberFormatException {
        return getMode(Integer.parseInt(i));
    }

    public static RunMode getMode(int i) {
        switch (i) {
            case 1:
                return GET_GENES_WITH_SINGLE_HPO;
            default:
                return NONE;
        }
    }
}
