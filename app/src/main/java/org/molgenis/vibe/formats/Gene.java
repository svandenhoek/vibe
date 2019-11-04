package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import static java.util.Objects.requireNonNull;

import java.net.URI;

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
//    private String symbol; // Currently unused.

    /**
     * The Disease Specificity Index (DSI) as stored within DisGeNET for a gene.
     * A higher score indicates a higher specificity (less associated diseases).
     * A 0 indicates only associations with phenotypes.
     * @see <a href="http://www.disgenet.org/web/DisGeNET/menu/dbinfo#specificity">http://www.disgenet.org/web/DisGeNET/menu/dbinfo#specificity</a>
     */
    private Double diseaseSpecificityIndex;

    /**
     * The Disease Pleiotropy Index (DPI) as stored within DisGeNET for a gene.
     * A higher score indicates more disease (MeSH) classes being associated.
     * A 0 indicates either only associations with phenotypes or the associated diseases do not have a MeSH class.
     * @see <a href="http://www.disgenet.org/web/DisGeNET/menu/dbinfo#pleiotropy">http://www.disgenet.org/web/DisGeNET/menu/dbinfo#pleiotropy</a>
     */
    private Double diseasePleiotropyIndex;

//    public String getSymbol() {
//        return symbol;
//    }

    public Double getDiseaseSpecificityIndex() {
        return diseaseSpecificityIndex;
    }

    public Double getDiseasePleiotropyIndex() {
        return diseasePleiotropyIndex;
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

    public Gene(URI uri) {
        super(uri);
    }

    /**
     * @param uri {@link BiologicalEntity#uri}
     * @param dsi diseaseSpecificityIndex
     * @param dpi diseasePleiotropyIndex
     * @throws InvalidStringFormatException
     */
    public Gene(URI uri, Double dsi, Double dpi) throws InvalidStringFormatException {
        super(uri);
        this.diseaseSpecificityIndex = requireNonNull(dsi);
        this.diseasePleiotropyIndex = requireNonNull(dpi);
    }

    @Override
    protected void generateCompareValue() {
        setCompareValue(Integer.parseInt(getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Gene{" +
//                "symbol='" + symbol + '\'' +
                ", diseaseSpecificityIndex=" + diseaseSpecificityIndex +
                ", diseasePleiotropyIndex=" + diseasePleiotropyIndex +
                "} " + super.toString();
    }
}
