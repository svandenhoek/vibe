package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import static java.util.Objects.requireNonNull;

import java.net.URI;

/**
 * A gene. Note that equality is based on the NCBI gene id only (as multiple objects with the same id but a different
 * name/symbol should be regarded as invalid and not as multiple DIFFERENT genes).
 */
public class Gene extends BiologicalEntity {
    @Override protected String idPrefix() { return "ncbigene:"; }
    @Override protected String idRegex() { return "^(ncbigene|NCBIGENE):([0-9]+)$"; }
    @Override protected int regexGroup() { return 2; }
    @Override protected String uriPrefix() { return "http://identifiers.org/ncbigene/"; }

    private static final String PREFIX = "ncbigene";

    /**
     * The HGNC (HUGO Gene Nomenclature Committee) name.
     */
    private String symbol;

    public String getSymbol() {
        return symbol;
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
