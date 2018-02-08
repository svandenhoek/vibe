package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

public class Source {
    private String name;
    private Level level;

    public String getName() {
        return name;
    }

    public Level getLevel() {
        return level;
    }

    public Source(String name, Level level) {
        this.name = requireNonNull(name);
        this.level = requireNonNull(level);
    }

    public enum Level {
        CURATED, MODEL, LITERATURE;
    }
}
