package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Objects;

public class Disease implements ResourceUri {
    private URI uri;

    private String name;

    public URI getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public Disease(String uri, String name) throws IllegalArgumentException {
        this(URI.create(uri), name);
    }

    public Disease(URI uri, String name) {
        this.uri = requireNonNull(uri);
        this.name = requireNonNull(name);
    }

    @Override
    public String toString() {
        return "Disease{" +
                "uri=" + uri +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return Objects.equals(uri, disease.uri) &&
                Objects.equals(name, disease.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uri, name);
    }
}
