package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.rdf_processing.QueryTester;
import org.molgenis.vibe.rdf_processing.querying.QueryRunnerRewindable;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.URI;
import java.util.*;

public class DisgenetQueryStringGeneratorTester extends QueryTester {
    private static final String delimiter = " - ";

    private ModelReader readerMini;
    private ModelReader readerFull;

    private QueryRunnerRewindable runner;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        readerMini = new TripleStoreDbReader(TestFilesDir.TDB_MINI.getDir());
        readerFull = new TripleStoreDbReader(TestFilesDir.TDB_FULL.getDir());
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        readerMini.close();
        readerFull.close();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        runner.close();
    }

    @Test
    public void testIriForHpo() throws InvalidStringFormatException {
        Set<Hpo> hpos = new HashSet<>();
        hpos.add(new Hpo("hp:0009811"));

        QueryString queryString = DisgenetQueryStringGenerator.getIriForHpo(hpos);
        System.out.println(queryString.getQuery());
        runner = new QueryRunnerRewindable(readerMini.getModel(), queryString);

        Assert.assertEquals(runner.hasNext(), true);
        Assert.assertEquals(runner.next().get("hpo").toString(), "http://purl.obolibrary.org/obo/HP_0009811");
        Assert.assertEquals(runner.hasNext(), false);
    }

    @Test
    public void testIriForMultiHpo() throws InvalidStringFormatException {
        Set<Hpo> hpos = new HashSet<>();
        hpos.add(new Hpo("hp:0009811"));
        hpos.add(new Hpo("hp:0002967"));
        hpos.add(new Hpo("hp:0002996"));
        hpos.add(new Hpo("hp:0001377"));

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0009811");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002967");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");

        QueryString queryString = DisgenetQueryStringGenerator.getIriForHpo(hpos);
        System.out.println(queryString.getQuery());

        runner = new QueryRunnerRewindable(readerMini.getModel(), queryString);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
    }

    @Test
    public void testAllHpoChildren() throws InvalidStringFormatException {
        Set<Hpo> hpos = new HashSet<>();
        hpos.add(new Hpo("hp:0009811"));

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0009811");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002967");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0005060");

        QueryString queryString = DisgenetQueryStringGenerator.getHpoChildren(hpos,
                new QueryStringPathRange(QueryStringPathRange.Predefined.ZERO_OR_MORE));
        System.out.println(queryString.getQuery());

        runner = new QueryRunnerRewindable(readerMini.getModel(), queryString);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
    }

//    @Test
//    public void testHpoChildrenTill2LevelsDeep() throws InvalidStringFormatException {
//        Set<Hpo> hpos = new HashSet<>();
//        hpos.add(new Hpo("hp:0009811"));
//
//        List<String> expectedOutput = new ArrayList<>();
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0009811");
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002967");
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");
//
//        String query = DisgenetQueryStringGenerator.getHpoChildren(hpos, new QueryStringPathRange(2, false));
//        System.out.println(query);
//
//        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
//        ResultSetFormatter.out(System.out, runner.getResultSet());
//        runner.reset();
//
//        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
//    }
//
//    /**
//     * @see QueryStringGeneratorTester#testHpoSubClassOfOnlyGrandChildrenWithoutDistinct()
//     * @throws InvalidStringFormatException
//     */
//    @Test(groups = {"dependencyBug"})
//    public void testHpoChildrenTill2LevelsDeepDifferentParent() throws InvalidStringFormatException {
//        Set<Hpo> hpos = new HashSet<>();
//        hpos.add(new Hpo("hp:0009811"));
//
//        List<String> expectedOutput = new ArrayList<>();
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001376");
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
//        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");
//
//        String query = DisgenetQueryStringGenerator.getHpoChildren(hpos, new QueryStringPathRange(2, false));
//        System.out.println(query);
//
//        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
//        ResultSetFormatter.out(System.out, runner.getResultSet());
//        runner.reset();
//
//        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
//    }

    @Test
    public void testPdasSingleHpo() throws InvalidStringFormatException {
        Set<Hpo> hpos = new HashSet<>();
        hpos.add(new Hpo(URI.create("http://purl.obolibrary.org/obo/HP_0009811"), "hp:0009811"));

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");

        QueryString queryString = DisgenetQueryStringGenerator.getPdas(hpos);
        System.out.println(queryString.getQuery());

        runner = new QueryRunnerRewindable(readerMini.getModel(), queryString);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
    }

    @Test
    public void testPdasMultipleHpo() throws InvalidStringFormatException {
        Set<Hpo> hpos = new HashSet<>();
        hpos.add(new Hpo(URI.create("http://purl.obolibrary.org/obo/HP_0009811"), "hp:0009811"));
        hpos.add(new Hpo(URI.create("http://purl.obolibrary.org/obo/HP_0002967"), "hp:0002967"));
        hpos.add(new Hpo(URI.create("http://purl.obolibrary.org/obo/HP_0002996"), "hp:0002966"));

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0175704");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");

        QueryString queryString = DisgenetQueryStringGenerator.getPdas(hpos);
        System.out.println(queryString.getQuery());

        runner = new QueryRunnerRewindable(readerMini.getModel(), queryString);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
    }

//    @Test
//    public void testPdasNoGrandChilds() throws InvalidStringFormatException {
//        List<String> expectedOutput = new ArrayList<>();
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0175704");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");
//
//
//        String query = DisgenetQueryStringGenerator.getPdas(new Hpo("hp:0009811"), new QueryStringPathRange(1, false));
//        System.out.println(query);
//
//        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
//        ResultSetFormatter.out(System.out, runner.getResultSet());
//        runner.reset();
//
//        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
//    }
//
//    @Test
//    public void testPdasAll() throws InvalidStringFormatException {
//        List<String> expectedOutput = new ArrayList<>();
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0175704");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0410538");
//        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1850318");
//
//        String query = DisgenetQueryStringGenerator.getPdas(new Hpo("hp:0009811"), new QueryStringPathRange(0, true));
//        System.out.println(query);
//
//        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
//        ResultSetFormatter.out(System.out, runner.getResultSet());
//        runner.reset();
//
//        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
//    }

    @Test
    public void geneDiseaseCombinationsForSingleDisease() {
        Set<Disease> inputDiseases = new HashSet<>();
        Disease disease = new Disease("http://linkedlifedata.com/resource/umls/id/C1853733", "HEMOCHROMATOSIS, TYPE 4");
        inputDiseases.add(disease);

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add(String.join(delimiter, disease.getUri().toString(), "http://identifiers.org/ncbigene/30061", "ncbigene:30061", "SLC40A1", "0.682472541057918E0", "http://rdf.disgenet.org/v5.0.0/void/UNIPROT", "http://identifiers.org/pubmed/15466004"));
        expectedOutput.add(String.join(delimiter, disease.getUri().toString(), "http://identifiers.org/ncbigene/30061", "ncbigene:30061", "SLC40A1", "0.682472541057918E0", "http://rdf.disgenet.org/v5.0.0/void/CTD_human"));

        QueryString queryString = DisgenetQueryStringGenerator.getGdas(inputDiseases, DisgenetAssociationType.GENE_DISEASE);
        System.out.println(queryString.getQuery());

        runner = new QueryRunnerRewindable(readerMini.getModel(), queryString);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        List<String> actualOutput = retrieveOutputFromGdaRunner(runner);

        // Sorts collections for better comparison.
        Collections.sort(expectedOutput);
        Collections.sort(actualOutput);

        Assert.assertEquals(actualOutput, expectedOutput);
    }

    private List<String> retrieveOutputFromGdaRunner(QueryRunnerRewindable runner) {
        List<String> output = new ArrayList<>();
        while(runner.hasNext()) {
            output.add(retrieveGdaOutput(runner.next()));
        }
        return output;
    }

    private String retrieveGdaOutput(QuerySolution output) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(output.get("disease").asResource().toString()).append(delimiter);
        strBuilder.append(output.get("gene").asResource().toString()).append(delimiter);
        strBuilder.append(output.get("geneId").asLiteral().getString()).append(delimiter);
        strBuilder.append(output.get("geneSymbolTitle").asLiteral().getString()).append(delimiter);
        strBuilder.append(output.get("gdaScoreNumber").asLiteral().getString()).append(delimiter);
        strBuilder.append(output.get("gdaSource").asResource().toString());
        if(output.get("evidence") != null) {
            strBuilder.append(delimiter).append(output.get("evidence").asResource().toString());
        }
        return strBuilder.toString();
    }
}
