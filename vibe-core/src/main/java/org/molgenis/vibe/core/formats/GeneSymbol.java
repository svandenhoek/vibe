package org.molgenis.vibe.core.formats;

import java.net.URI;

public class GeneSymbol extends Entity {
    private static final String ID_PREFIX = "hgnc:";
    private static final String ID_REGEX = "^(hgnc|HGNC):([A-Z0-9-]+)$";
    private static final int REGEX_ID_GROUP = 2;
    private static final String URI_PREFIX = "http://identifiers.org/hgnc.symbol/";

    @Override
    protected String getIdPrefix() {
        return ID_PREFIX;
    }

    @Override
    protected String getIdRegex() {
        return ID_REGEX;
    }

    @Override
    protected int getRegexIdGroup() {
        return REGEX_ID_GROUP;
    }

    @Override
    protected String getUriPrefix() {
        return URI_PREFIX;
    }

    public GeneSymbol(String id) {
        super(id);
    }

    public GeneSymbol(URI uri) {
        super(uri);
    }

    @Override
    public String toString() {
        return "GeneSymbol{" + super.toString() + '}';
    }
}
