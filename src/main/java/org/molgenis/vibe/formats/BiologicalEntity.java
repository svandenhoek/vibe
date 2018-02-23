package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public abstract class BiologicalEntity implements ResourceUri, Comparable<BiologicalEntity> {
    protected abstract String prefix();
    protected abstract String regex();
    protected abstract int regexGroup();

    private String id;

    private String name;

    private URI uri;

    public String getId() {
        return id;
    }

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
        this.id = retrieveIdFromString(id);
    }

    public BiologicalEntity(String id, String name, URI uri) throws InvalidStringFormatException {
        this.id = retrieveIdFromString(id);
        this.name = name;
        this.uri = uri;
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
