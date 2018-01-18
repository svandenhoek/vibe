package org.molgenis.vibe.io.disgenet_rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

abstract class RdfFileReader {
    private Model model = null;

    /**
     * Generates a {@link Model} for {@link #useQuery(String)} based on a single RDF file.
     * @param file {@link String}{@code []}
     */
    public RdfFileReader readFile(String file) {
        if(model == null) {
            model = RDFDataMgr.loadModel(file);
        } else  {
            RDFDataMgr.read(model, file);
        }

        return this;
    }

    public RdfFileReader readFile(String file, Lang fileType) {
        if(model == null) {
            model = RDFDataMgr.loadModel(file, fileType);
        } else  {
            RDFDataMgr.read(model, file, fileType);
        }

        return this;
    }

    /**
     * Generates a {@link Model} for {@link #useQuery(String)} based on a multiple RDF files merged together in a single
     * {@link Model}.
     * @param files {@link String}{@code []}
     */
    public RdfFileReader readFiles(String[] files) {
        readFile(files[0]);

        for(int i = 1; i < files.length;i++) {
            RDFDataMgr.read(model, files[i]);
        }

        return this;
    }

    public RdfFileReader readFiles(String[] files, Lang fileType) {
        readFile(files[0], fileType);

        for(int i = 1; i < files.length;i++) {
            RDFDataMgr.read(model, files[i], fileType);
        }

        return this;
    }

    public RdfFileReader clear() {
        model = null;
        return this;
    }

    /**
     * Use a SPARQL query for retrieving data.
     * @param queryString {@link String}
     * @return {@link ResultSet}
     */
    public ResultSet useQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execSelect();
    }
}
