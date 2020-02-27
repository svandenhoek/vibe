package org.molgenis.vibe.tdb_processing.query_string_creation;

import org.apache.jena.query.ResultSetFormatter;
import org.junit.*;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.io.input.ModelReader;
import org.molgenis.vibe.io.input.TripleStoreDbReader;
import org.molgenis.vibe.tdb_processing.query_runner.QueryRunnerRewindable;

import java.io.IOException;
import java.util.*;

/**
 * Note that these tests use data from DisGeNET for validation. However, this was kept as minimal as possible while still
 * being able to actually test the functioning of the code and only reflects what is EXPECTED to be found within the
 * DisGeNET dataset when using the query (on a technical basis). Additionally, the actual data on which these tests are
 * executed on are available externally and are not included in the repository itself.
 *
 * The full DisGeNET RDF dataset can be downloaded from: http://rdf.disgenet.org/download/
 * The license can be found on: http://www.disgenet.org/ds/DisGeNET/html/legal.html
 */
public class QueryStringGeneratorTester {
    private static ModelReader reader;
    private QueryRunnerRewindable runner;

    @BeforeClass
    public static void beforeClass() throws IOException {
        reader = new TripleStoreDbReader(TestData.TDB.getDir());
    }

    @AfterClass
    public static void afterClass() {
        if(reader != null) {
            reader.close();
        }
    }

    @After
    public void afterMethod() {
        runner.close();
    }

    @Test
    public void testSourcesUnique() {
        QueryString queryString = QueryStringGenerator.getSources();
        runner = new QueryRunnerRewindable(reader.getModel(), queryString);

        Set<String> sources = new HashSet<>();

        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        while(runner.hasNext()) {
            String source = runner.next().get("source").asResource().getURI();
            if(sources.contains(source)) {
                Assert.fail("a source URI was found more than once");
            }
            sources.add(source);
        }
    }
}
