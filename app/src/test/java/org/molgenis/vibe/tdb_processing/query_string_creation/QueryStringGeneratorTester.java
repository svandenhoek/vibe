package org.molgenis.vibe.tdb_processing.query_string_creation;

import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.input.ModelReader;
import org.molgenis.vibe.io.input.TripleStoreDbReader;
import org.molgenis.vibe.tdb_processing.QueryTester;
import org.molgenis.vibe.tdb_processing.query_runner.QueryRunnerRewindable;
import org.testng.Assert;
import org.testng.annotations.*;

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
public class QueryStringGeneratorTester extends QueryTester {
    private static final String delimiter = " - ";

    private ModelReader reader;

    private QueryRunnerRewindable runner;

    @BeforeClass
    public void beforeClass() throws IOException {
        reader = new TripleStoreDbReader(TestData.TDB_FULL.getDir());
    }

    @AfterClass
    public void afterClass() {
        reader.close();
    }

    @AfterMethod
    public void afterMethod() {
        runner.close();
    }

    @Test(groups = {"noTest"})
    public void showSourcesQuery() {
        System.out.println(QueryStringGenerator.getSources().getQuery());
    }

    @Test(groups = {"noTest"})
    public void showGenesForPhenotypeQuery() {
        System.out.println(QueryStringGenerator.getGenesForPhenotypes(new HashSet<>(Arrays.asList(new Phenotype("hp:0007469")))).getQuery());
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
