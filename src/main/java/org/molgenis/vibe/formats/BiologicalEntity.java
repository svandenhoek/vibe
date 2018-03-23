package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Describes a biological entity (such as a {@link Gene}, {@link Disease} or {@link Phenotype}).
 */
public abstract class BiologicalEntity implements ResourceUri, Comparable<BiologicalEntity> {
    /**
     * The entity prefix.
     * @return a {@link String} containing the prefix.
     */
    protected abstract String prefix();

    /**
     * A regular expression an input {@link String} should adhere to when deriving the {@link BiologicalEntity} from it.
     * @return
     */
    protected abstract String regex();

    /**
     * The group within the regular expression the actual {@link BiologicalEntity#id} is stored in.
     * @return
     */
    protected abstract int regexGroup();

    /**
     * The unique id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * The {@link URI}.
     */
    private URI uri;

    /**
     * @return the {@link BiologicalEntity} ID without prefix.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the {@link BiologicalEntity} ID with prefix.
     */
    public String getFormattedId() {
        return prefix() + id;
    }

    protected void setId(String id) {
        this.id = requireNonNull(id);
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = requireNonNull(name);
    }

    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * Simple constructor allowing for easy comparison of collections.
     * @param id
     */
    public BiologicalEntity(String id) {
        this.id = retrieveIdFromString(requireNonNull(id));
    }

    public BiologicalEntity(String id, String name, URI uri) throws InvalidStringFormatException {
        this.id = retrieveIdFromString(requireNonNull(id));
        this.name = requireNonNull(name);
        this.uri = requireNonNull(uri);
    }

    /**
     * Validates and retrieves the ID from a {@link String} describing an id (that also includes other information such as a prefix).
     * @param fullString the {@link String} to be digested
     * @return a {@link String} containing the id only
     * @throws InvalidStringFormatException if {@code fullString} did not adhere to the regular expression
     */
    protected String retrieveIdFromString(String fullString) throws InvalidStringFormatException {
        Matcher m = Pattern.compile(regex()).matcher(fullString);
        if(m.matches()) {
            return m.group(regexGroup());
        } else {
            throw new InvalidStringFormatException(fullString + " does not adhere the required format: " + regex());
        }
    }

    @Override
    public String toString() {
        return "BiologicalEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiologicalEntity that = (BiologicalEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(BiologicalEntity o) {
        return getFormattedId().compareTo(o.getFormattedId());
    }
}
