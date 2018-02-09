package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;

public class Gene {
    private URI uri;

    private int id;

    private String name;

    private String symbol;

    public URI getUri() {
        return uri;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Gene(String uri, int id, String name, String symbol) throws IllegalArgumentException {
        this(URI.create(uri), id, name, symbol);
    }

    public Gene(URI uri, int id, String name, String symbol) {
        this.uri = requireNonNull(uri);
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.symbol = requireNonNull(symbol);
    }
}
