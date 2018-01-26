package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;

public class QueryRunnerRewindable extends QueryRunner {

    private ResultSetRewindable resultSetRewindable;

    public QueryRunnerRewindable(Model model, String queryString) {
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
