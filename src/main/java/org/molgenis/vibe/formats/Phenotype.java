package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * Defines a phenotype.
 */
public class Phenotype extends BiologicalEntity {
    @Override protected String idPrefix() { return "hp:"; }
    @Override protected String idRegex() { return "^(hp|HP):([0-9]{7})$"; }
    @Override protected int regexGroup() { return 2; }
    @Override protected String uriPrefix() { return "http://purl.obolibrary.org/obo/HP_"; }

    public Phenotype(String id) {
        super(id);
    }

    public Phenotype(URI uri) {
        super(uri);
    }

    public Phenotype(String id, String name, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
    }
}
