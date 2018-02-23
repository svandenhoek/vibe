package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines a HPO term.
 */
public class Hpo implements ResourceUri {
    private static final String PREFIX = "hp";

    /**
     * The Human Phenotype Ontology id.
     */
    private String id;

    /**
     * The {@link URI} that refers to this HPO within the RDF database.
     */
    private URI uri;

    public String getId() {
        return id;
    }

    /**
     * @return a {@link String} containing the ID with "hp:" prefix.
     */
    public String getFormattedId() {
        return PREFIX + ":" + id;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * Simple constructor allowing for easy comparison of collections.
     * @param id
     * @throws InvalidStringFormatException
     */
    public Hpo(String id) throws InvalidStringFormatException {
        this.id = retrieveIdNumbers(id);
    }

    /**
     * A constructor for describing an HPO.
     * @param id
     * @param uri
     * @throws InvalidStringFormatException
     */
    public Hpo(String id, URI uri) throws InvalidStringFormatException {
        this.id = retrieveIdNumbers(id);
        this.uri = requireNonNull(uri);
    }

    /**
     * Validates and retrieves the ID without prefix from a {@link String} describing an HPO id.
     * @param hpoTerm a {@link String} containing the HPO id (with prefix)
     * @return an {@code int} with the HPO id without prefix (if present)
     * @throws InvalidStringFormatException if {@code hpoTerm} does not adhere to the regex: ^((hp|HP):)?([0-9]{7})$
     */
    private String retrieveIdNumbers(String hpoTerm) throws InvalidStringFormatException {
        Matcher m = Pattern.compile("^((hp|HP):)?([0-9]{7})$").matcher(hpoTerm);
        if(m.matches()) {
            return m.group(3);
        } else {
            throw new InvalidStringFormatException(hpoTerm + " does not adhere the required format: ^((hp|HP):)?([0-9]{7})$");
        }
    }

    @Override
    public String toString() {
        return "Hpo{" +
                "id='" + id + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hpo hpo = (Hpo) o;
        return Objects.equals(id, hpo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
