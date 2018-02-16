package org.molgenis.vibe.rdf_processing.querying;

import org.apache.jena.atlas.lib.Closeable;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.molgenis.vibe.rdf_processing.query_string_creation.QueryString;

import java.util.Iterator;

/**
 * Is able to run a SPARQL query on a {@link Model}.
 */
public class QueryRunner implements Closeable, Iterator {
    /**
     * Object storing the query and model for query execution.
     */
    private QueryExecution qexec;

    /**
     * Object for iterating over the query output (usually querying is done during the actual iteration over the results).
     */
    private ResultSet results;

    public QueryRunner(Model model, QueryString queryString) {
        Query query = QueryFactory.create(queryString.getQuery(), queryString.getSyntax());
        qexec = QueryExecutionFactory.create(query, model);
        results = qexec.execSelect();
    }

    /**
     * Retrieve the output from the {@link Model} based on the given {@code queryString}. Note that depending on the
     * underlying implementation, the actual running of the query is done while retrieving the actual results.
     * @return
     * @see QueryExecution#execSelect()
     */
    public ResultSet getResultSet() {
        return results;
    }

    @Override
    public boolean hasNext() {
        return results.hasNext();
    }

    @Override
    public QuerySolution next() {
        return results.next();
    }

    @Override
    public void close() {
        qexec.close();
    }
}
