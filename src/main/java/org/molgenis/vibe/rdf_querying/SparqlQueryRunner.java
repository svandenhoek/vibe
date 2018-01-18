package org.molgenis.vibe.rdf_querying;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

public class SparqlQueryRunner {
    private Model model;

    public SparqlQueryRunner(Model model) {
        requireNonNull(model);

        this.model = model;
    }

    /**
     * Use a SPARQL query for retrieving data.
     * @param queryString {@link String}
     * @return {@link ResultSet}
     */
    public ResultSet runQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execSelect();
    }

    protected static String addLimit(String query, int limit) {
        if (limit > 0) {
            return query + " \nLIMIT " + limit;
        } else {
            throw new IllegalArgumentException("limit should be > 0");
        }
    }
}
