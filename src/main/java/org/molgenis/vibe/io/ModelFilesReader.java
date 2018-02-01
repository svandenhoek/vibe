package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
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

    /**
     * generates a {@link Model} from {@code file}.
     * @param file a {@link String} defining the location of an RDF file
     */
    public ModelFilesReader(String file) {
        model = RDFDataMgr.loadModel(file);
    }

    /**
     * generates a {@link Model} from {@code file}.
     * @param file a {@link String} defining the location of an RDF file
     * @param lang the type of RDF file as described by {@link Lang}
     */
    public ModelFilesReader(String file, Lang lang) {
        model = RDFDataMgr.loadModel(file, lang);
    }

    /**
     * generates a {@link Model} from multiple {@code files}.
     * @param files a {@link String} array defining the location of the RDF files.
     */
    public ModelFilesReader(String[] files) {
        this(files[0]);
        if(files.length > 1) {
            readFiles(files, 1);
        }
    }

    /**
     * generates a {@link Model} from multiple {@code files}.
     * @param files a {@link String} array defining the location of the RDF files.
     * @param lang the type of RDF file as described by {@link Lang}
     */
    public ModelFilesReader(String[] files, Lang lang) {
        this(files[0], lang);
        if(files.length > 1) {
            readFiles(files, lang, 1);
        }
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
        readFiles(files, 0);
        return this;
    }

    /**
     * Adds the {@code files} to the existing {@link Model}
     * @param files a {@link String} array defining the location of the RDF files.
     * @param lang the type of RDF file as described by {@link Lang}
     * @return
     */
    public ModelFilesReader read(String[] files, Lang lang) {
        readFiles(files, lang, 0);
        return this;
    }

    /**
     * Adds the {@code files} to the existing {@link Model} starting from {@code pos} (RDF files in the array before
     * {@code pos} are ignored).
     * @param files a {@link String} array defining the location of the RDF files.
     * @param pos the starting position from {@code files} to be used when adding to the existing {@link Model}
     */
    private void readFiles(String[] files, int pos) {
        for (int i = pos; i < files.length; i++) {
            RDFDataMgr.read(model, files[i]);
        }
    }

    /**
     * Adds the {@code files} to the existing {@link Model} starting from {@code pos} (RDF files in the array before
     * {@code pos} are ignored).
     * @param files a {@link String} array defining the location of the RDF files.
     * @param lang the type of RDF file as described by {@link Lang}
     * @param pos the starting position from {@code files} to be used when adding to the existing {@link Model}
     */
    private void readFiles(String[] files, Lang lang, int pos) {
        for (int i = pos; i < files.length; i++) {
            RDFDataMgr.read(model, files[i], lang);
        }
    }

    @Override
    public void close() {
        model.close();
    }
}
