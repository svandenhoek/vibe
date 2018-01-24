package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;

public interface ModelReader {

    Model getModel();

    void close();
}
