package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.testng.Assert;

import java.util.HashSet;
import java.util.Set;

public class QueryTester {
    void assertRunnerHpoOutputWithExpectedResults(QueryRunner runner, String field, Set<String> expectedOutput) {
        Set<String> actualOutput = new HashSet<>();
        while(runner.hasNext()) {
            QuerySolution result = runner.next();
            actualOutput.add(result.get(field).toString());
        }

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
