package org.molgenis.vibe.formats;

import java.net.URI;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Source {
    private String name;
    private Level level;

    private URI uri;

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public URI getUri() {
        return uri;
    }

    public Source(String name, Level level, URI uri) {
        this.name = requireNonNull(name);
        this.level = requireNonNull(level);
        this.uri = requireNonNull(uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return Objects.equals(name, source.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    public enum Level {
        CURATED, MODEL, LITERATURE;
    }
}
