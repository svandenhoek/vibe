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

    public ModelFilesReader(String file) {
        model = RDFDataMgr.loadModel(file);
    }

    public ModelFilesReader(String file, Lang lang) {
        model = RDFDataMgr.loadModel(file, lang);
    }

    public ModelFilesReader(String[] files) {
        this(files[0]);
        if(files.length > 1) {
            readFiles(files, 1);
        }
    }

    public ModelFilesReader(String[] files, Lang lang) {
        this(files[0], lang);
        if(files.length > 1) {
            readFiles(files, lang, 1);
        }
    }

    public ModelFilesReader read(String file) {
        RDFDataMgr.read(model, file);
        return this;
    }

    public ModelFilesReader read(String file, Lang lang) {
        RDFDataMgr.read(model, file, lang);
        return this;
    }

    public ModelFilesReader read(String[] files) {
        readFiles(files, 0);
        return this;
    }

    public ModelFilesReader read(String[] files, Lang lang) {
        readFiles(files, lang, 0);
        return this;
    }

    private void readFiles(String[] files, int pos) {
        for (int i = pos; i < files.length; i++) {
            RDFDataMgr.read(model, files[i]);
        }
    }

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
