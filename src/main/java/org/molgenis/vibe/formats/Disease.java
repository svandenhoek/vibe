package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Objects;

/**
 * A disease. Note that equality is based on the UMLS id only (as multiple objects with the same id but a different
 * name should be regarded as invalid and not as multiple DIFFERENT diseases).
 */
public class Disease implements ResourceUri {
    /**
     * The Unified Medical Language System id.
     */
    private String umls;

    /**
     * The disease name.
     */
    private String name;

    /**
     * The {@link URI} that refers to this disease within the RDF database.
     */
    private URI uri;

    public String getUmls() {
        return umls;
    }

    public String getName() {
        return name;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * Simple constructor allowing for easy comparison of collections.
     * @param umls
     */
    public Disease(String umls) {
        this.umls = umls;
    }

    /**
     * A constructor for describing a disease with an {@link URI} id from an RDF database.
     * @param umls
     * @param name
     * @param uri
     */
    public Disease(String umls, String name, URI uri) {
        this.umls = requireNonNull(umls);
        this.name = requireNonNull(name);
        this.uri = requireNonNull(uri);
    }

    @Override
    public String toString() {
        return "Disease{" +
                "umls='" + umls + '\'' +
                ", name='" + name + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(umls, disease.umls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(umls);
    }
}
