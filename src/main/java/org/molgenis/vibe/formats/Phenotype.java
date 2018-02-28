package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * Defines a phenotype.
 *
 * If no prefix is given for validation, an HPO id is assumed.
 */
public class Phenotype extends BiologicalEntity {
    @Override protected String prefix() { return "hp:"; }
    @Override protected String regex() { return "^((hp|HP):)?([0-9]{7})$"; }
    @Override protected int regexGroup() { return 3; }

    public Phenotype(String id) {
        super(id);
    }

    public Phenotype(String id, String name, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
    }
}
