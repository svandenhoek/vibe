package org.molgenis.vibe.rdf_processing.query_string_creation;

/**
 * Contains general functions that can be useful for creating SPARQL queries.
 */
public abstract class QueryStringGenerator {
    /**
     * Adds a limit to the items returned by a SPARQL query.
     * @param query the query to add a limit to
     * @param limit a maximum number of items the SPARQL is allowed to return ({@code int > 0})
     * @return the query with a limit appended to it
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
