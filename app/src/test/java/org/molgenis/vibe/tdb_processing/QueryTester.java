package org.molgenis.vibe.tdb_processing;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.molgenis.vibe.tdb_processing.query_runner.QueryRunner;
import org.testng.Assert;

import java.util.*;

public class QueryTester {
    private static final String DELIMITER = " - ";

    public void assertRunnerOutput(QueryRunner runner, String[] fieldOrder, List<List<String>> expectedOutput) {
        // Stores actual output.
        List<String> actualOutput = new ArrayList<>();

        while(runner.hasNext()) {
            QuerySolution result = runner.next();
            List<String> singleResultActualOutput = new ArrayList<>();

            // Goes through the fields (in the described order).
            for(int i = 0; i < fieldOrder.length; i++) {
                RDFNode singleFieldFromSingleResult = result.get(fieldOrder[i]);
                // Checks if retrieved field returned something (in case of optional fields).
                if(singleFieldFromSingleResult != null) {
                    // Without retrieving the literal or resource object type, toString() would also return data describing
                    // a literal besides the value itself (such as "^^http://www.w3.org/2001/XMLSchema#double").
//                    singleResultActualOutput.add(singleFieldFromSingleResult.toString());
                    if(singleFieldFromSingleResult.isLiteral()) {
                        singleResultActualOutput.add(singleFieldFromSingleResult.asLiteral().getString());
                    } else {
                        singleResultActualOutput.add(singleFieldFromSingleResult.asResource().toString());
                    }
                }
            }
            // Adds field results separated by the delimiter.
            actualOutput.add(String.join(DELIMITER, singleResultActualOutput));
        }

        // Converts expected output to similar format as actual output (a String with a delimiter between fields).
        List<String> comparableExpectedOutput = new ArrayList<>();
        for(List<String> ExpectedItem : expectedOutput) {
            comparableExpectedOutput.add(String.join(DELIMITER, ExpectedItem));
        }

        // Sorts collections for better comparison.
        Collections.sort(actualOutput);
        Collections.sort(comparableExpectedOutput);

        // Checks if output is as expected.
        Assert.assertEquals(actualOutput, comparableExpectedOutput);
    }

    public void assertSingleFieldFromRunnerOutput(QueryRunner runner, String field, List<String> expectedOutput) {
        List<String> actualOutput = new ArrayList<>();
        while(runner.hasNext()) {
            QuerySolution result = runner.next();
            actualOutput.add(result.get(field).toString());
        }

        // Sorts collections for better comparison.
        Collections.sort(actualOutput);
        Collections.sort(expectedOutput);

        // Checks if output is as expected.
        Assert.assertEquals(actualOutput, expectedOutput);
    }

    public void assertQueryResultCountWithExpectedResult(QueryRunner runner, int expectedCount) {
        int actualCount = 0;
        while(runner.hasNext()) {
            actualCount += 1;
            runner.next();
        }

        Assert.assertEquals(actualCount, expectedCount);
    }
}
