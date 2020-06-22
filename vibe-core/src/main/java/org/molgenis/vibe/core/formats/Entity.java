package org.molgenis.vibe.core.formats;

import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Describes an entity.
 *
 * Be sure to update toString() to include all new fields when subclassing! Please refer to
 * org.molgenis.vibe.rdf_processing.GenesForPhenotypeRetrieverTester within the test code for more information.
 *
 */
public abstract class Entity implements ResourceUri, Comparable<Entity>, allFieldsEquals {
    /**
     * The entity prefix.
     * @return a {@link String} containing the prefix.
     */
    protected abstract String getIdPrefix();

    /**
     * A regular expression an input {@link String} should adhere to when deriving the {@link Entity} from it.
     * @return
     */
    protected abstract String getIdRegex();

    /**
     * The group within the regular expression the actual {@link Entity#id} is stored in.
     * @return
     */
    protected abstract int getRegexIdGroup();

    /**
     * The String describing the URI prefix it should match with.
     * @return
     */
    protected abstract String getUriPrefix();

    /**
     * The unique id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * The {@link URI}. Note that while in general triplets use IRIs, Apache Jena refers to URIs.
     * @see org.apache.jena.rdf.model.Resource#getURI()
     */
    private URI uri;

    /**
     * @return the {@link Entity} ID without prefix.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the {@link Entity} ID with prefix.
     */
    public String getFormattedId() {
        return getIdPrefix() + ":" + id;
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

    public Entity(String id) {
        this.id = retrieveIdFromString(requireNonNull(id));
        uri = URI.create( getUriPrefix() + this.id );
    }

    public Entity(URI uri) {
        this.uri = uri;
        String uriString = this.uri.toString();
        validateUri(uriString);
        id = uriString.split(getUriPrefix())[1];

        // Throws InvalidStringFormatException in case URI does not generate and ID that adheres to the required regex.
        retrieveIdFromString(getFormattedId());
    }

    public Entity(String id, String name) {
        this(id);
        this.name = requireNonNull(name);
    }

    public Entity(URI uri, String name) {
        this(uri);
        this.name = requireNonNull(name);
    }

    private void validateUri(String uriString) {
        if(!uriString.startsWith(getUriPrefix())) {
            throw new IllegalArgumentException("The URI \"" + uriString + "\" does not start with: " + getUriPrefix());
        } else if(uriString.length() == getUriPrefix().length()) {
            throw new IllegalArgumentException("The URI \"" + uriString + "\" does not contain anything after the prefix.");
        }
    }

    /**
     * Validates and retrieves the ID from a {@link String} describing an id (that also includes other information such as a prefix).
     * @param fullString the {@link String} to be digested
     * @return a {@link String} containing the id only
     * @throws InvalidStringFormatException if {@code fullString} did not adhere to the regular expression
     */
    protected String retrieveIdFromString(String fullString) throws InvalidStringFormatException {
        Matcher m = Pattern.compile(getIdRegex()).matcher(fullString);
        if(m.matches()) {
            return m.group(getRegexIdGroup());
        } else {
            throw new InvalidStringFormatException(fullString + " does not adhere the required format: " + getIdRegex());
        }
    }

    /**
     * While uniqueness is based on the {@link #id} (as each {@link #id} should only occur once and data belonging to it
     * should be consistent), {@link #toString()} can be used for testing whether data retrieval from external sources
     * yielded the expected results.
     * @return
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity that = (Entity) o;
        return Objects.equals(uri, that.uri);
    }

    @Override
    public boolean allFieldsEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(name, entity.name) &&
                Objects.equals(uri, entity.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }

    @Override
    public int compareTo(Entity o) {
        return getId().compareTo(o.getId());
    }
}
