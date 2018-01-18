package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class ModelReader {
    private Model model = null;

    public Model getModel() {
        return model;
    }

    /**
     * Generates a {@link Model} based on a single RDF file.
     * @param file {@link String}{@code []}
     */
    public ModelReader readFile(String file) {
        if(model == null) {
            model = RDFDataMgr.loadModel(file);
        } else  {
            RDFDataMgr.read(model, file);
        }

        return this;
    }

    public ModelReader readFile(String file, Lang fileType) {
        if(model == null) {
            model = RDFDataMgr.loadModel(file, fileType);
        } else  {
            RDFDataMgr.read(model, file, fileType);
        }

        return this;
    }

    /**
     * Generates a {@link Model} based on a multiple RDF files merged together in a single
     * {@link Model}.
     * @param files {@link String}{@code []}
     */
    public ModelReader readFiles(String[] files) {
        readFile(files[0]);

        for(int i = 1; i < files.length;i++) {
            RDFDataMgr.read(model, files[i]);
        }

        return this;
    }

    public ModelReader readFiles(String[] files, Lang fileType) {
        readFile(files[0], fileType);

        for(int i = 1; i < files.length;i++) {
            RDFDataMgr.read(model, files[i], fileType);
        }

        return this;
    }

    public ModelReader clear() {
        model = null;
        return this;
    }
}
