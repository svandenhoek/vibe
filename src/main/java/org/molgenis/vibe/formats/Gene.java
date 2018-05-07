package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import static java.util.Objects.requireNonNull;

import java.net.URI;

/**
 * A gene. Note that equality is based on the NCBI gene id only (as multiple objects with the same id but a different
 * name/symbol should be regarded as invalid and not as multiple DIFFERENT genes).
 */
public class Gene extends BiologicalEntity {
    private static final String ID_PREFIX = "ncbigene:";
    private static final String ID_REGEX = "^(ncbigene|NCBIGENE):([0-9]+)$";
    private static final int REGEX_ID_GROUP = 2;
    private static final String URI_PREFIX = "http://identifiers.org/ncbigene/";

    /**
     * The HGNC (HUGO Gene Nomenclature Committee) name.
     */
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    @Override
    protected String getIdPrefix() {
        return ID_PREFIX;
    }

    @Override
    protected String getIdRegex() {
        return ID_REGEX;
    }

    @Override
    protected int getRegexIdGroup() {
        return REGEX_ID_GROUP;
    }

    @Override
    protected String getUriPrefix() {
        return URI_PREFIX;
    }

    public Gene(String id) {
        super(id);
    }

    public Gene(String id, String name, String symbol, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
        this.symbol = requireNonNull(symbol);
    }

    @Override
    public String toString() {
        return "Gene{" +
                "symbol='" + symbol + '\'' +
                "} " + super.toString();
    }
}
