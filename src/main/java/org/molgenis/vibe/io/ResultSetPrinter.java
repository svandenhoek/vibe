package org.molgenis.vibe.io;

import org.apache.commons.collections.IteratorUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ResultSetPrinter {
    public static void print(ResultSet resultSet, boolean header) {
        // For the first QuerySolution, prints the header if requested.
        if(resultSet.hasNext()) {
            print(resultSet.next(), header);
        }

        // Prints out remaining QuerySolutions.
        while(resultSet.hasNext()) {
            printSolution(resultSet.next());
        }
    }

    public static void print(QuerySolution querySolution, boolean header) {
        if(header) { printHeader(querySolution); }
        printSolution(querySolution);
    }

    public static void printHeader(QuerySolution querySolution) {
        System.out.println(IteratorUtils.toList(querySolution.varNames()).stream().collect(Collectors.joining(" - ")));
        System.out.println("--------------------------------------------------------------------------------");
    }

    public static void printSolution(QuerySolution querySolution) {
        Iterator<String> varNames = querySolution.varNames();
        List<String> queryResults = new ArrayList<>();

        while(varNames.hasNext()) {
            String varName = varNames.next();
            queryResults.add(querySolution.get(varName).toString());
        }

        System.out.println(queryResults.stream().collect(Collectors.joining(" - ")));
    }
}
