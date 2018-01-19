package org.molgenis.vibe.rdf_querying;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

public class SparqlQueryRunner {
    private Model model;

    /**
     * Creates a {@link SparqlQueryRunner} using the given {@link Model}.
     * @param model the model which should be used when running queries.
     */
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

    /**
     * Adds a limit to the items returned by a SPARQL query.
     * @param query the query to add a limit to
     * @param limit a maximum number of items the SPARQL is allowed to return ({@code int > 0})
     * @return the query with a limit appened to it
     * @throws IllegalArgumentException if {@code limit} <= 0
     */
    protected static String addLimit(String query, int limit) {
        if (limit > 0) {
            return query + " \nLIMIT " + limit;
        } else {
            throw new IllegalArgumentException("limit should be > 0");
        }
    }
}
