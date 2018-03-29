package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * A disease. Note that equality is based on the id only.
 */
public class Disease extends BiologicalEntity {
    @Override protected String idPrefix() { return "umls:"; }
    @Override protected String idRegex() { return "^(umls|UMLS):(C[0-9]+)$"; }
    @Override protected int regexGroup() { return 2; }
    @Override protected String uriPrefix() { return "http://linkedlifedata.com/resource/umls/id/"; }

    public Disease(String id) {
        super(id);
    }

    public Disease(String id, String name, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
    }
}
