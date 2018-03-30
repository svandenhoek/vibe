package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * A disease. Note that equality is based on the id only.
 */
public class Disease extends BiologicalEntity {
    private static final String ID_PREFIX = "umls:";
    private static final String ID_REGEX = "^(umls|UMLS):(C[0-9]+)$";
    private static final int REGEX_ID_GROUP = 2;
    private static final String URI_PREFIX = "http://linkedlifedata.com/resource/umls/id/";

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

    public Disease(String id) {
        super(id);
    }

    public Disease(String id, String name, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
    }
}
