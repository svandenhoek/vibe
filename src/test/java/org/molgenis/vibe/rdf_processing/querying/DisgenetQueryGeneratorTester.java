package org.molgenis.vibe.rdf_processing.querying;

import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.Set;

public class DisgenetQueryGeneratorTester extends QueryTester {
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
        Set<String> expectedOutput = new HashSet<>();
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

        assertRunnerHpoOutputWithExpectedResults(runner, "hpo", expectedOutput);
    }

    @Test
    public void testHpoChildrenTill2LevelsDeep() throws InvalidStringFormatException {
        Set<String> expectedOutput = new HashSet<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0009811");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002967");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");

        String query = DisgenetQueryGenerator.getHpoChildren(new Hpo("hp:0009811"), new SparqlRange(2, false));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertRunnerHpoOutputWithExpectedResults(runner, "hpo", expectedOutput);
    }

    @Test
    public void testHpoChildrenTill2LevelsDeepDifferentParent() throws InvalidStringFormatException {
        Set<String> expectedOutput = new HashSet<>();
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001376");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedOutput.add("http://purl.obolibrary.org/obo/HP_0001377");

        String query = DisgenetQueryGenerator.getHpoChildren(new Hpo("hp:0001376"), new SparqlRange(2, false));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertRunnerHpoOutputWithExpectedResults(runner, "hpo", expectedOutput);
    }

    @Test
    public void testPdasSelfOnly() throws InvalidStringFormatException {
        Set<String> expectedOutput = new HashSet<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");

        String query = DisgenetQueryGenerator.getPdas(new Hpo("hp:0009811"), new SparqlRange(0));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertRunnerHpoOutputWithExpectedResults(runner, "disease", expectedOutput);
    }

    @Test
    public void testPdasNoGrandChilds() throws InvalidStringFormatException {
        Set<String> expectedOutput = new HashSet<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0152084");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0175704");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");


        String query = DisgenetQueryGenerator.getPdas(new Hpo("hp:0009811"), new SparqlRange(1, false));
        System.out.println(query);

        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        assertRunnerHpoOutputWithExpectedResults(runner, "disease", expectedOutput);
    }

    @Test
    public void testPdasAll() throws InvalidStringFormatException {
        Set<String> expectedOutput = new HashSet<>();
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

        assertRunnerHpoOutputWithExpectedResults(runner, "disease", expectedOutput);
    }

    @Test
    public void hpoGenesUnknownHpoMini() {
        runner = new QueryRunnerRewindable(readerMini.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:1234567"));

        Assert.assertEquals(runner.hasNext(), false, "match found while HPO not in file");
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
