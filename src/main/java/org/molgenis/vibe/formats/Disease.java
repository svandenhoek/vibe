package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A disease. Note that equality is based on the id only.
 */
public class Disease implements ResourceUri {
    private static final String PREFIX = "umls";
    /**
     * The disease id.
     */
    private String id;

    /**
     * The disease name.
     */
    private String name;

    /**
     * The {@link URI} that refers to this disease within the RDF database.
     */
    private URI uri;

    public String getId() {
        return id;
    }

    public String getFormattedId() {
        return PREFIX + ":" + id;
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
     * @param id
     */
    public Disease(String id) {
        this.id = id;
    }

    /**
     * A constructor for describing a disease with an {@link URI} id from an RDF database.
     * @param id
     * @param name
     * @param uri
     * @throws InvalidStringFormatException see {@link #retrieveIdNumbers(String)}
     */
    public Disease(String id, String name, URI uri) throws InvalidStringFormatException {
        this.id = retrieveIdNumbers(id);
        this.name = requireNonNull(name);
        this.uri = requireNonNull(uri);
    }

    /**
     * Validates and retrieves the ID without prefix from a {@link String} describing a disease id.
     * @param id a {@link String} containing the disease id with prefix
     * @return an {@code int} with the disease id without prefix (if present)
     * @throws InvalidStringFormatException if {@code id} does not adhere to the regex: ^((umls|UMLS):)?C([0-9]+)$
     */
    private String retrieveIdNumbers(String id) throws InvalidStringFormatException {
        Matcher m = Pattern.compile("^((umls|UMLS):)?C([0-9]+)$").matcher(id);
        if(m.matches()) {
            return m.group(3);
        } else {
            throw new InvalidStringFormatException(id + " does not adhere the required format: ^((umls|UMLS):)?C([0-9]+)$");
        }
    }

    @Override
    public String toString() {
        return "Disease{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(id, disease.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
