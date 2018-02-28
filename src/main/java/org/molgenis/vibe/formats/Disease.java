package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * A disease. Note that equality is based on the id only.
 *
 * If no prefix is given for validation, an umls id is assumed.
 */
public class Disease extends BiologicalEntity {
    @Override protected String prefix() { return "umls:"; }
    @Override protected String regex() { return "^((umls|UMLS):)?C([0-9]+)$"; }
    @Override protected int regexGroup() { return 3; }

    public Disease(String id) {
        super(id);
    }

    public Disease(String id, String name, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
    }
}
