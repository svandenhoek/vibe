package org.molgenis.vibe.formats;

public class Source {
    private String name;
    private Level level;

    public enum Level {
        CURATED, MODEL, LITERATURE;
    }
}
