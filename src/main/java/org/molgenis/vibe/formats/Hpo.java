package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * Defines a HPO term.
 */
public class Hpo extends BiologicalEntity {
    @Override protected String prefix() { return "hp"; }
    @Override protected String regex() { return "^((hp|HP):)?([0-9]{7})$"; }
    @Override protected int regexGroup() { return 3; }

    public Hpo(String id) {
        super(id);
    }

    public Hpo(String id, String name, URI uri) throws InvalidStringFormatException {
        super(id, name, uri);
    }
}
