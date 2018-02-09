package org.molgenis.vibe.rdf_processing.querying;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

public class DisgenetQueryGeneratorTester extends QueryTester {
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
        String query = DisgenetQueryGenerator.getIriForHpo(new Hpo("hp:0009811"));
        System.out.println(query);
        runner = new QueryRunnerRewindable(readerMini.getModel(), query);

        Assert.assertEquals(runner.hasNext(), true);
        Assert.assertEquals(runner.next().get("hpo").toString(), "http://purl.obolibrary.org/obo/HP_0009811");
        Assert.assertEquals(runner.hasNext(), false);
    }

    @Test
    public void testAllHpoChildren() throws InvalidStringFormatException {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0009811");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002967");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0005060");

        String query = DisgenetQueryGenerator.getHpoChildren(new Hpo("hp:0009811"), new SparqlRange(0, true));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
    }

    @Test
    public void testHpoChildrenTill2LevelsDeep() throws InvalidStringFormatException {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0009811");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002967");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");

        String query = DisgenetQueryGenerator.getHpoChildren(new Hpo("hp:0009811"), new SparqlRange(2, false));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
    }

    @Test
    public void testHpoChildrenTill2LevelsDeepDifferentParent() throws InvalidStringFormatException {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001376");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");

        String query = DisgenetQueryGenerator.getHpoChildren(new Hpo("hp:0001376"), new SparqlRange(2, false));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "hpo", expectedOutput);
    }

    @Test
    public void testPdasSelfOnly() throws InvalidStringFormatException {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");

        String query = DisgenetQueryGenerator.getPdas(new Hpo("hp:0009811"), new SparqlRange(0));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
    }

    @Test
    public void testPdasNoGrandChilds() throws InvalidStringFormatException {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0175704");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");


        String query = DisgenetQueryGenerator.getPdas(new Hpo("hp:0009811"), new SparqlRange(1, false));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
    }

    @Test
    public void testPdasAll() throws InvalidStringFormatException {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0175704");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0410538");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1850318");

        String query = DisgenetQueryGenerator.getPdas(new Hpo("hp:0009811"), new SparqlRange(0, true));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
    }

    @Test
    public void hpoGenesUnknownHpoMini() {
        runner = new QueryRunnerRewindable(readerMini.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:1234567"));

        Assert.assertEquals(runner.hasNext(), false, "match found while HPO not in file");
    }

    @Test
    public void geneDiseaseCombinationsForSingleDisease() {
        Set<Disease> inputDiseases = new HashSet<>();
        Disease disease = new Disease("http://linkedlifedata.com/resource/umls/id/C1853733", "HEMOCHROMATOSIS, TYPE 4");
        inputDiseases.add(disease);

        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add(String.join(delimiter, disease.getUri().toString(), "http://identifiers.org/ncbigene/30061", "SLC40A1", "0.682472541057918E0", "http://rdf.disgenet.org/v5.0.0/void/UNIPROT", "http://identifiers.org/pubmed/15466004"));
        expectedOutput.add(String.join(delimiter, disease.getUri().toString(), "http://identifiers.org/ncbigene/30061", "SLC40A1", "0.682472541057918E0", "http://rdf.disgenet.org/v5.0.0/void/CTD_human"));

        String query = DisgenetQueryGenerator.getGdas(inputDiseases, DisgenetAssociationType.GENE_DISEASE);
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
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
        strBuilder.append(output.get("geneId").asLiteral().getString()).append(delimiter);
        strBuilder.append(output.get("geneSymbolTitle").asLiteral().getString()).append(delimiter);
        strBuilder.append(output.get("gdaScore").asLiteral().getString()).append(delimiter);
        strBuilder.append(output.get("gdaSource").asResource().toString());
        if(output.get("evidence") != null) {
            strBuilder.append(delimiter).append(output.get("evidence").asResource().toString());
        }
        return strBuilder.toString();
    }

//
//    @Test
//    public void hpoGenesHpoInFileMini() {
//        runner = new QueryRunnerRewindable(readerMini.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:0009811"));
//        ResultSetFormatter.out(System.out, runner.getResultSet());
//        runner.reset();
//
//        // Expected can be counted in file using regex: ^<http://rdf.disgenet.org/resource/gda/ (note that 2 GDAs do not have a phenotype stored)
//        assertQueryResultCountWithExpectedResult(runner, 12);
//    }
}
