package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Source implements ResourceUri {
    private String name;
    private Level level;

    private URI uri;

    public String getName() {
        return name.split(" ")[0];
    }

    public String getFullName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public URI getUri() {
        return uri;
    }

    /**
     * Simple constructor allowing for easy comparison of collections.
     * @param name
     */
    public Source(String name) {
        this.name = requireNonNull(name);
    }

    /**
     * A constructor for describing a source with an {@link URI} id from an RDF database.
     * @param name
     * @param level
     * @param uri
     */
    public Source(String name, Level level, URI uri) {
        this.name = requireNonNull(name);
        this.level = requireNonNull(level);
        this.uri = requireNonNull(uri);
    }

    /**
     * A constructor for describing a source with an {@link URI} id from an RDF database.
     * @param name
     * @param level
     * @param uri
     * @throws InvalidStringFormatException if {@code level} could not be converted into an appropriate {@link Level}
     */
    public Source(String name, String level, URI uri) throws InvalidStringFormatException {
        this.name = requireNonNull(name);
        this.level = Level.retrieveLevelByDisgenetVoidString(level);
        this.uri = requireNonNull(uri);
    }

    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", uri=" + uri +
                '}';
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
        CURATED("curated", "source_evidence_curated"),
        MODEL("model", "source_evidence_predicted"),
        LITERATURE("literature", "source_evidence_literature");

        private String readableString;
        private String disgenetVoidUri;

        Level(String readableString, String disgenetVoidUriEnd) {
            this.readableString = readableString;
            this.disgenetVoidUri = disgenetVoidUriEnd;
        }

        public static Level retrieveLevelByDisgenetVoidString(String levelString) throws InvalidStringFormatException {
            levelString = levelString.toLowerCase();

            for(Level level : Level.values()) {
                if(levelString.equals(level.readableString) ||
                        levelString.endsWith(level.disgenetVoidUri)) {
                    return level;
                }
            }
            throw new InvalidStringFormatException("Could not generate a Source.Level from given String");
        }
    }
}
