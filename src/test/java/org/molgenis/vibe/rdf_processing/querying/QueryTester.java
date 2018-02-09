package org.molgenis.vibe.rdf_processing.querying;

import org.apache.jena.query.QuerySolution;
import org.testng.Assert;

import java.util.*;

public class QueryTester {
    void assertSingleFieldFromRunnerOutput(QueryRunner runner, String field, List<String> expectedOutput) {
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

    void assertQueryResultCountWithExpectedResult(QueryRunner runner, int expectedCount) {
        int actualCount = 0;
        while(runner.hasNext()) {
            actualCount += 1;
            runner.next();
        }

        Assert.assertEquals(actualCount, expectedCount);
    }
}
