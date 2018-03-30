package org.molgenis.vibe.io;

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

    public ModelFilesReader() {
        model = generateModel();
    }

    /**
     * Generates a new {@link Model} using the {@link ModelFactory}.
     * @return
     */
    protected Model generateModel() {
        return ModelFactory.createDefaultModel();
    }

    /**
     * Adds the {@code file} to the existing {@link Model}
     * @param file a {@link String} defining the location of an RDF file
     * @return
     */
    public ModelFilesReader read(String file) {
        RDFDataMgr.read(model, file);
        return this;
    }

    /**
     * Adds the {@code file} to the existing {@link Model}
     * @param file a {@link String} defining the location of an RDF file
     * @param lang the type of RDF file as described by {@link Lang}
     * @return
     */
    public ModelFilesReader read(String file, Lang lang) {
        RDFDataMgr.read(model, file, lang);
        return this;
    }

    /**
     * Adds the {@code files} to the existing {@link Model}
     * @param files a {@link String} array defining the location of the RDF files.
     * @return
     */
    public ModelFilesReader read(String[] files) {
        for(String file : files) {
            read(file);
        }
        return this;
    }

    /**
     * Adds the {@code files} to the existing {@link Model}
     * @param files a {@link String} array defining the location of the RDF files.
     * @param lang the type of RDF file as described by {@link Lang}
     * @return
     */
    public ModelFilesReader read(String[] files, Lang lang) {
        for(String file : files) {
            read(file, lang);
        }
        return this;
    }

    @Override
    public void close() {
        model.close();
    }
}
