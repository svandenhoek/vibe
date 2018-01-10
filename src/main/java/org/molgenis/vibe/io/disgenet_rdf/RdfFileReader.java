package org.molgenis.vibe.io.disgenet_rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

abstract class RdfFileReader {
    private Model model;

    /**
     * Generates a {@link Model} for {@link #useQuery(String)} based on a single RDF file.
     * @param file {@link String}{@code []}
     */
    public void readFile(String file) {
        Model model = ModelFactory.createDefaultModel() ;
        this.model = model.read(file);
    }

    /**
     * Generates a {@link Model} for {@link #useQuery(String)} based on a multiple RDF files merged together in a single
     * {@link Model}.
     * @param files {@link String}{@code []}
     */
    public void readFiles(String[] files) {
        //TODO create test code to validate whether this works correctly (is based on https://jena.apache.org/documentation/io/rdf-input.html#example-2-using-the-rdfdatamgr)
        // Reads in first file.
        if(files.length > 0) {
            model = RDFDataMgr.loadModel(files[0]);
        }
        for(int i = 1; i < files.length;i++) {
            // Adds other files to already created model.
            RDFDataMgr.read(model, files[i]);
        }
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
