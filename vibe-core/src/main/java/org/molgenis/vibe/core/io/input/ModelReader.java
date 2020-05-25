package org.molgenis.vibe.core.io.input;

import org.apache.jena.rdf.model.Model;

import java.io.Closeable;

/**
 * Interface describing a {@link Class} that reads in a {@link Model}.
 */
public interface ModelReader extends Closeable {

    Model getModel();

    void close();
}
