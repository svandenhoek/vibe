package org.molgenis.vibe.io;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBException; // A TDBException is also available in: org.apache.jena.tdb2.TDBException
import org.apache.jena.tdb.TDBFactory;

import java.io.IOException;
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

    /**
     * @param dir {@link Path} to the TDB
     * @throws IOException see {@link #TripleStoreDbReader(String)}
     */
    public TripleStoreDbReader(Path dir) throws IOException {
        this(dir.toString());
    }

    /**
     * @param dir {@link String} containing the path to the TDB
     * @throws IOException thrown when something goes wrong with digesting the TDB (such as the TDB already being used by another JVM)
     */
    public TripleStoreDbReader(String dir) throws IOException {
        try {
            dataset = TDBFactory.createDataset(requireNonNull(dir));
            dataset.begin(ReadWrite.READ);
            model = dataset.getDefaultModel();
        } catch (TDBException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void close() {
        model.close();
        dataset.end();
        dataset.close();
    }
}
