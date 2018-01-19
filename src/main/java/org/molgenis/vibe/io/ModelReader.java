package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Creates an (Ontology) {@link Model} from RDF files.
 */
public class ModelReader {
    /**
     * The model that is created from the RDF files.
     */
    private Model model = null;

    public Model getModel() {
        return model;
    }

    /**
     * If no {@link Model} is loaded, generates one from the given {@code file}. Otherwise, adds the {@code file} to the
     * existing model.
     * @param file location of RDF file
     * @return {@code this}, for method chaining
     */
    public ModelReader readFile(String file) {
        if(model == null) {
            model = RDFDataMgr.loadModel(file);
        } else  {
            RDFDataMgr.read(model, file);
        }

        return this;
    }

    /**
     * If no {@link Model} is loaded, generates one from the given {@code file}. Otherwise, adds the {@code file} to the
     * existing model.
     * @param file location of RDF file
     * @param fileType RDF file syntax (<a href="https://jena.apache.org/documentation/io/rdf-input.html#determining-the-rdf-syntax">list of Jena supported syntaxes</a>)
     * @return {@code this}, for method chaining
     */
    public ModelReader readFile(String file, Lang fileType) {
        if(model == null) {
            model = RDFDataMgr.loadModel(file, fileType);
        } else  {
            RDFDataMgr.read(model, file, fileType);
        }

        return this;
    }

    /**
     * If no {@link Model} is loaded, generates one from the given {@code files}. Otherwise, adds the {@code files} to the
     * existing model.
     * {@link Model}.
     * @param files {@link String}{@code []}
     */

    /**
     * If no {@link Model} is loaded, generates one from the given {@code files}. Otherwise, adds the {@code files} to the
     * existing model.
     * @param files location of RDF files
     * @return {@code this}, for method chaining
     */
    public ModelReader readFiles(String[] files) {
        readFile(files[0]);

        for(int i = 1; i < files.length;i++) {
            RDFDataMgr.read(model, files[i]);
        }

        return this;
    }

    /**
     * If no {@link Model} is loaded, generates one from the given {@code files}. Otherwise, adds the {@code files} to the
     * existing model.
     * @param files location of RDF files
     * @param fileType RDF file syntax (<a href="https://jena.apache.org/documentation/io/rdf-input.html#determining-the-rdf-syntax">list of Jena supported syntaxes</a>)
     * @return {@code this}, for method chaining
     */
    public ModelReader readFiles(String[] files, Lang fileType) {
        readFile(files[0], fileType);

        for(int i = 1; i < files.length;i++) {
            RDFDataMgr.read(model, files[i], fileType);
        }

        return this;
    }

    /**
     * Clears the {@link Model}.
     * @return {@code this}, for method chaining
     */
    public ModelReader clear() {
        model = null;
        return this;
    }
}
