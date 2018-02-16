package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.apache.jena.query.Syntax;

public class QueryString {
    private String query;

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
