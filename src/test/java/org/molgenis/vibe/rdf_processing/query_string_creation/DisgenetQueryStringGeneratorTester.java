package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.rdf_processing.QueryTester;
import org.molgenis.vibe.rdf_processing.querying.QueryRunnerRewindable;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URI;
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
public class DisgenetQueryStringGeneratorTester extends QueryTester {
    private static final String delimiter = " - ";

    private ModelReader reader;

    private QueryRunnerRewindable runner;

    @BeforeClass
    public void beforeClass() {
        reader = new TripleStoreDbReader(TestData.TDB_MINI.getDir());
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        reader.close();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        runner.close();
    }

    private void runQueryTest(QueryString queryString, String[] fieldOrder, List<List<String>> expectedOutput) {
        runQueryTest(queryString, fieldOrder, expectedOutput, true);
    }

    private void runQueryTest(QueryString queryString, String[] fieldOrder, List<List<String>> expectedOutput, boolean verbose) {
        if(verbose) {
            System.out.println(queryString.getQuery());
        }

        runner = new QueryRunnerRewindable(reader.getModel(), queryString);

        if(verbose) {
            ResultSetFormatter.out(System.out, runner.getResultSet());
            runner.reset();
        }

        assertRunnerOutput(runner, fieldOrder, expectedOutput);
    }

    @Test
    public void testSourcesUnique() {
        QueryString queryString = DisgenetQueryStringGenerator.getSources();
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

    @Test
    public void testGenesForPhenotypes() {
        Set<Phenotype> phenotypes = new HashSet<>();
        phenotypes.add(new Phenotype(URI.create("http://purl.obolibrary.org/obo/HP_0002996")));

        String[] fieldOrder = {"gene", "geneId", "geneTitle", "geneSymbolTitle"};

        List<List<String>> expectedOutput = Arrays.asList(
                Arrays.asList("http://identifiers.org/ncbigene/1280", "ncbigene:1280", "collagen type II alpha 1 chain", "COL2A1"),
                Arrays.asList("http://identifiers.org/ncbigene/8243", "ncbigene:8243", "structural maintenance of chromosomes 1A", "SMC1A"),
                Arrays.asList("http://identifiers.org/ncbigene/1291", "ncbigene:1291", "collagen type VI alpha 1 chain", "COL6A1"),
                Arrays.asList("http://identifiers.org/ncbigene/1292", "ncbigene:1292", "collagen type VI alpha 2 chain", "COL6A2")
        );

        QueryString queryString = DisgenetQueryStringGenerator.getGenesForPhenotypes(phenotypes);
        runQueryTest(queryString, fieldOrder, expectedOutput);
    }

    @Test
    public void testGdaForGenes() {
        Set<Gene> genes = new HashSet<>();
        genes.add(new Gene("ncbigene:1291", "collagen type II alpha 1 chain", "COL2A1", 0.393643700083081E0, 0.75E0, URI.create("http://identifiers.org/ncbigene/1291")));

        String[] fieldOrder = {"gene", "disease", "diseaseId", "diseaseTitle", "gdaScoreNumber", "gdaSource", "evidence"};

        List<List<String>> expectedOutput = Arrays.asList(
                Arrays.asList("http://identifiers.org/ncbigene/1291", "http://linkedlifedata.com/resource/umls/id/C1834674", "umls:C1834674", "Bethlem myopathy", "0.68357144819477E0", "http://rdf.disgenet.org/v5.0.0/void/MGD"),
                Arrays.asList("http://identifiers.org/ncbigene/1291", "http://linkedlifedata.com/resource/umls/id/C0026850", "umls:C0026850", "Muscular Dystrophy", "0.214763469460921E0", "http://rdf.disgenet.org/v5.0.0/void/BEFREE", "http://identifiers.org/pubmed/19519726")
        );

        QueryString queryString = DisgenetQueryStringGenerator.getGdasWithDiseasesForGenes(genes);
        runQueryTest(queryString, fieldOrder, expectedOutput);
    }
}
