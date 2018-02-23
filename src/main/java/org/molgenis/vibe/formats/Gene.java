package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A gene. Note that equality is based on the NCBI gene id only (as multiple objects with the same id but a different
 * name/symbol should be regarded as invalid and not as multiple DIFFERENT genes).
 */
public class Gene implements ResourceUri {
    private static final String PREFIX = "ncbigene";

    /**
     * The NCBI gene id.
     */
    private int id;

    /**
     * The gene name.
     */
    private String name;

    /**
     * The HGNC (HUGO Gene Nomenclature Committee) name.
     */
    private String symbol;

    /**
     * The {@link URI} that refers to this gene within the RDF database.
     */
    private URI uri;

    public int getId() {
        return id;
    }

    public String getFormattedId() {
        return PREFIX + ":" + id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * Simple constructor allowing for easy comparison of collections.
     * @param id
     */
    public Gene(int id) {
        this.id = requireNonNull(id);
    }

    /**
     * A constructor for describing a gene with an {@link URI} id from an RDF database.
     * @param id
     * @param name
     * @param symbol
     * @throws NumberFormatException if the id could not be converted to a number
     */
    public Gene(String id, String name, String symbol, URI uri) throws NumberFormatException {
        this.id = Integer.parseInt(retrieveIdNumbers(id));
        this.name = requireNonNull(name);
        this.symbol = requireNonNull(symbol);
        this.uri = requireNonNull(uri);
    }

    /**
     * A constructor for describing a gene with an {@link URI} id from an RDF database.
     * @param id
     * @param name
     * @param symbol
     * @param uri
     */
    public Gene(int id, String name, String symbol, URI uri) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.symbol = requireNonNull(symbol);
        this.uri = requireNonNull(uri);
    }

    /**
     * Validates and retrieves the ID without prefix from a {@link String} describing a gene id.
     * @param gene a {@link String} containing the gene id (with prefix)
     * @return an {@code int} with the gene id without prefix (if present)
     * @throws InvalidStringFormatException if {@code gene} does not adhere to the regex: ^((hp|HP):)?([0-9]{7})$
     */
    private String retrieveIdNumbers(String gene) throws InvalidStringFormatException {
        Matcher m = Pattern.compile("^(ncbigene:)?([0-9]+)$").matcher(gene);
        if(m.matches()) {
            return m.group(2);
        } else {
            throw new InvalidStringFormatException(gene + " does not adhere the required format: ^(ncbigene:)?([0-9]+)$");
        }
    }

    @Override
    public String toString() {
        return "Gene{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gene gene = (Gene) o;
        return id == gene.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
