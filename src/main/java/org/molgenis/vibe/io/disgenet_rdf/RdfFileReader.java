package org.molgenis.vibe.io.disgenet_rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

abstract class RdfFileReader {
    private Model model;

    void readFile(String file) {
        Model model = ModelFactory.createDefaultModel() ;
        this.model = model.read(file);
    }

    ResultSet useQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execSelect();
    }

    ResultSet digestFile(String file, String query) {
        readFile(file);
        return useQuery(query);
    }
}
