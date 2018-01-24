package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    public ModelReader read(Path file) {
        return read(file.toString());
    }

    public ModelReader read(Path file, Lang fileType) {
        return read(file.toString(), fileType);
    }

    public ModelReader read(Path[] files) {
        return read(pathsToStrings(files));
    }

    public ModelReader read(Path[] files, Lang fileType) {
        return read(pathsToStrings(files), fileType);
    }

    private String[] pathsToStrings(Path[] files) {
        return Arrays.stream(files).map(Path::toString).toArray(String[]::new);
    }

    /**
     * If no {@link Model} is loaded, generates one from the given {@code file}. Otherwise, adds the {@code file} to the
     * existing model.
     * @param file location of RDF file
     * @return {@code this}, for method chaining
     */
    public ModelReader read(String file) {
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
    public ModelReader read(String file, Lang fileType) {
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
     * @param files location of RDF files
     * @return {@code this}, for method chaining
     */
    public ModelReader read(String[] files) {
        if(files.length > 0) {
            read(files[0]);

            for (int i = 1; i < files.length; i++) {
                RDFDataMgr.read(model, files[i]);
            }
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
    public ModelReader read(String[] files, Lang fileType) {
        if(files.length > 0) {
            read(files[0], fileType);

            for (int i = 1; i < files.length; i++) {
                RDFDataMgr.read(model, files[i], fileType);
            }
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
