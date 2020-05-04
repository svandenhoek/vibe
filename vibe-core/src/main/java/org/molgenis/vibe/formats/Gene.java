package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Objects;

/**
 * A gene. Note that equality is determined through {@link URI}{@code s} from the domain
 * <a href="http://identifiers.org">http://identifiers.org</a> describing NCBI Entrez gene identifiers. This was done
 * as DisGeNET converted other gene-identifiers (such as HGNC symbols) into NCBI Entrez gene identifiers
 * (<a href="http://disgenet.org/dbinfo#section41>http://disgenet.org/dbinfo#section41">source</a>) within the database.
 */
public class Gene extends BiologicalEntity {
    private static final String ID_PREFIX = "ncbigene:";
    private static final String ID_REGEX = "^(ncbigene|NCBIGENE):([0-9]+)$";
    private static final int REGEX_ID_GROUP = 2;
    private static final String URI_PREFIX = "http://identifiers.org/ncbigene/";

    /**
     * The HGNC (HUGO Gene Nomenclature Committee) name.
     */
    private GeneSymbol symbol;

    /**
     * The id stored as {@code int}.
     */
    private int idInt;

    public GeneSymbol getSymbol() {
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

    public Gene(String id, GeneSymbol symbol) {
        super(id);
        this.idInt = Integer.parseInt(getId());
        this.symbol = requireNonNull(symbol);
    }

    public Gene(URI uri, GeneSymbol symbol) {
        super(uri);
        this.idInt = Integer.parseInt(getId());
        this.symbol = requireNonNull(symbol);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Gene{" +
                "symbol='" + symbol + '\'' +
                ' ' + super.toString() +
                '}';
    }

    @Override
    public int compareTo(Entity o) {
        if (o instanceof Gene) {
            Gene oGene = (Gene) o;
            return idInt - oGene.idInt;
        } else {
            return super.compareTo(o);
        }
    }

    @Override
    public boolean allFieldsEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gene gene = (Gene) o;
        return super.allFieldsEquals(gene) &&
                idInt == gene.idInt &&
                symbol.allFieldsEquals(gene.symbol);
    }
}
