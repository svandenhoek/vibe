package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.testng.Assert;

import java.util.HashSet;
import java.util.Set;

public class QueryTester {
    void assertRunnerHpoOutputWithExpectedResults(QueryRunner runner, Set<String> expectedReferences) {
        Set<String> actualReferences = new HashSet<>();
        while(runner.hasNext()) {
            QuerySolution result = runner.next();
            actualReferences.add(result.get("hpo").toString());
        }

        Assert.assertEquals(actualReferences, expectedReferences);
    }
}
