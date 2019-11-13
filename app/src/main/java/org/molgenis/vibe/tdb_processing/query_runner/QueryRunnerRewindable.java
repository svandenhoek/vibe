package org.molgenis.vibe.tdb_processing.query_runner;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.molgenis.vibe.tdb_processing.query_string_creation.QueryString;

/**
 * Is able to run a SPARQL query on a {@link Model} and iterate over the results multiple times.
 */
public class QueryRunnerRewindable extends QueryRunner {

    /**
     * Object for iterating multiple times over the query output.
     */
    private ResultSetRewindable resultSetRewindable;

    public QueryRunnerRewindable(Model model, QueryString queryString) {
        super(model, queryString);
        resultSetRewindable = ResultSetFactory.copyResults(super.getResultSet());
    }

    @Override
    public ResultSetRewindable getResultSet() {
        return resultSetRewindable;
    }

    @Override
    public boolean hasNext() {
        return resultSetRewindable.hasNext();
    }

    @Override
    public QuerySolution next() {
        return resultSetRewindable.next();
    }

    public void reset() {
        resultSetRewindable.reset();
    }
}
