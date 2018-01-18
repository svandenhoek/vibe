package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.Iterator;

public abstract class ResultSetPrinter {
    public static void print(ResultSet resultSet) {
        while(resultSet.hasNext()) {
            print(resultSet.next());
        }
    }

    public static void print(QuerySolution querySolution) {
        StringBuilder strBuilder = new StringBuilder();

        Iterator<String> varNames = querySolution.varNames();
        while(varNames.hasNext()) {
            String varName = varNames.next();
            strBuilder.append(querySolution.get(varName).toString()).append(" - ");
        }

        System.out.println(strBuilder.toString());
    }
}
