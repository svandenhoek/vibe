package org.molgenis.vibe.io.input;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Creates an (Ontology) {@link Model} from RDF files.
 */
public class ModelFilesReader implements ModelReader {
    /**
     * The model that is created from the RDF files.
     */
    private Model model;

    @Override
    public Model getModel() {
        return model;
    }

    public ModelFilesReader(String file) {
        model = generateModel();
        read(file);
    }

    public ModelFilesReader(String file, Lang lang) {
        model = generateModel();
        read(file, lang);
    }

    /**
     * Generates a new {@link Model} using the {@link ModelFactory}.
     * @return a new {@link Model}
     */
    protected Model generateModel() {
        return ModelFactory.createDefaultModel();
    }

    /**
     * Adds the {@code file} to the existing {@link Model}
     * @param file a {@link String} defining the location of an RDF file
     * @return itself for fluent programming
     */
    public ModelFilesReader read(String file) {
        RDFDataMgr.read(model, file);
        return this;
    }

    /**
     * Adds the {@code file} to the existing {@link Model}
     * @param file a {@link String} defining the location of an RDF file
     * @param lang the type of RDF file as described by {@link Lang}
     * @return itself for fluent programming
     */
    public ModelFilesReader read(String file, Lang lang) {
        RDFDataMgr.read(model, file, lang);
        return this;
    }

    @Override
    public void close() {
        model.close();
    }
}
