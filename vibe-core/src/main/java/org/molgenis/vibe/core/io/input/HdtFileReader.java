package org.molgenis.vibe.core.io.input;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Reader for a HDT file. Note that when the HDT file is stored in a read-only directory, a
 * {@link java.io.FileNotFoundException} will be thrown when no index file is present.
 */
public class HdtFileReader implements ModelReader {
    /**
     * The model that is created from the HDT file.
     */
    private Model model;

    /**
     * The HDT.
     */
    private HDT hdt;

    /**
     * The HDT as Jena Graph.
     */
    private HDTGraph graph;

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void close() {
        model.close();
        graph.close();
        try {
            hdt.close();
        } catch (IOException e) {
            System.err.println("An error occurred while trying to close the HDT.");
        }
    }

    public HdtFileReader(Path file) throws IOException {
        this(file.toString());
    }

    public HdtFileReader(String file) throws IOException {
        hdt = HDTManager.loadIndexedHDT(file, null);
        graph = new HDTGraph(hdt);
        model = ModelFactory.createModelForGraph(graph);
    }
}
