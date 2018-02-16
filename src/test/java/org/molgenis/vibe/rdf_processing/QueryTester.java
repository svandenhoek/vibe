package org.molgenis.vibe.rdf_processing;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;
import org.testng.Assert;

import java.util.*;

public class QueryTester {
    public void assertSingleFieldFromRunnerOutput(QueryRunner runner, String field, List<String> expectedOutput) {
        assertSingleFieldFromRunnerOutput(runner, field, expectedOutput, null);
    }

    public void assertSingleFieldFromRunnerOutput(QueryRunner runner, String field, List<String> expectedOutput, Map<String,List<String>> filters) {
        List<String> actualOutput = new ArrayList<>();
        while(runner.hasNext()) {
            QuerySolution result = runner.next();
//            // Filters results on values if requested.
//            if(filters != null) {
//                // Goes through all defined fields to filter on.
//                for(String filterField : filters.keySet()) {
//                    // Retrieves value of current item in that field.
//                    String filterFieldValue = result.get(filterField).toString();
//                    // Checks if current item needs to be filtered (skipped) based on field.
//                    if(filters.get(filterField).contains(filterFieldValue)) {
//                        continue;
//                    }
//                }
//            }
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
