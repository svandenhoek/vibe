package org.molgenis.vibe.io;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

/**
 * Creates an (Ontology) {@link Model} from a TDB database.
 */
public class TripleStoreDbReader implements ModelReader {
    /**
     * The model that is created from the RDF files.
     */
    private Model model;

    /**
     * The TDB dataset.
     */
    private Dataset dataset;

    @Override
    public Model getModel() {
        return model;
    }

    public TripleStoreDbReader(Path dir) {
        this(dir.toString());
    }

    public TripleStoreDbReader(String dir) {
        dataset = TDBFactory.createDataset(requireNonNull(dir));
        dataset.begin(ReadWrite.READ);
        model = dataset.getDefaultModel();
    }

    @Override
    public void close() {
        model.close();
        dataset.end();
        dataset.close();
    }
}
