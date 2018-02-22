package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Objects;

/**
 * A gene. Note that equality is based on the NCBI gene id only (as multiple objects with the same id but a different
 * name/symbol should be regarded as invalid and not as multiple DIFFERENT genes).
 */
public class Gene implements ResourceUri {
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
     * A constructor for describing a gene.
     * @param id
     * @param name
     * @param symbol
     */
    public Gene(int id, String name, String symbol) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.symbol = requireNonNull(symbol);
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

    @Override
    public String toString() {
        return "Gene{" +
                "uri=" + uri +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
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
