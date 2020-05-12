package org.molgenis.vibe.core.tdb_processing.query_string_creation;

import org.apache.jena.query.Syntax;

/**
 * A {@link String} that can be used for SPARQL querying.
 */
public class QueryString {
    /**
     * The {@link String} to be used.
     */
    private String query;

    /**
     * The {@link Syntax} needed for the query.
     */
    private Syntax syntax;

    public String getQuery() {
        return query;
    }

    public Syntax getSyntax() {
        return syntax;
    }

    public QueryString(String query) {
        this.query = query;
        this.syntax = Syntax.defaultQuerySyntax;
    }

    public QueryString(String query, Syntax syntax) {
        this.query = query;
        this.syntax = syntax;
    }
}
