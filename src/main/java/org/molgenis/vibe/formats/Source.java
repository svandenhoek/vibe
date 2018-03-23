package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Describes the source (the database) for a specific piece of information. Based on the <a href="http://www.disgenet.org/web/DisGeNET/menu/dbinfo#sources">DisGeNET sources/levels</a>.
 */
public class Source implements ResourceUri {
    /**
     * The name of the {@link Source}.
     */
    private String name;

    /**
     * The {@link Source} level.
     */
    private Level level;

    /**
     * The {@link Source} {@link URI}.
     */
    private URI uri;

    /**
     * Retrieves the short name (first word only).
     * @return the {@link Source} name
     */
    public String getName() {
        return name.split(" ")[0];
    }

    /**
     * Retrieves the full name.
     * @return the {@link Source} name
     */
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

    /**
     * The possible {@link Source} levels.
     */
    public enum Level {
        CURATED("curated", "source_evidence_curated"),
        MODEL("model", "source_evidence_predicted"),
        LITERATURE("literature", "source_evidence_literature");

        /**
         * Readable version of the enum.
         */
        private String readableString;

        /**
         * How the {@link Level} is identified within the DisGeNET database.
         */
        private String disgenetVoidUri;

        Level(String readableString, String disgenetVoidUriEnd) {
            this.readableString = readableString;
            this.disgenetVoidUri = disgenetVoidUriEnd;
        }

        /**
         * Retrieves the {@link Level} based on a {@link String}. This {@link String} is compared with {@link Level#disgenetVoidUri}.
         * @param levelString a {@link String} used for defining the {@link Level}.
         * @return a {@link Level} belonging to the {@code levelString}
         * @throws InvalidStringFormatException if no {@link Level} could be defined from the {@code levelString}
         */
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
