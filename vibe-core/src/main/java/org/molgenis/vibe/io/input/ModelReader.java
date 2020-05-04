package org.molgenis.vibe.io.input;

import org.apache.jena.rdf.model.Model;

/**
 * Interface describing a {@link Class} that reads in a {@link Model}.
 */
public interface ModelReader {

    Model getModel();

    void close();
}
