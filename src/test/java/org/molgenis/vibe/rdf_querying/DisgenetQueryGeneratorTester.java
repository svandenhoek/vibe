package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestFile;
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
        readerMini = new TripleStoreDbReader(TestFile.TDB_MINI.getFilePath());
        readerFull = new TripleStoreDbReader(TestFile.TDB_FULL.getFilePath());
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
        runner = new QueryRunnerRewindable(readerMini.getModel(), DisgenetQueryGenerator.getIriForHpo(new Hpo("hp:0009811")));

        Assert.assertEquals(runner.hasNext(), true);
        Assert.assertEquals(runner.next().get("hpo").toString(), "http://purl.obolibrary.org/obo/HP_0009811");
        Assert.assertEquals(runner.hasNext(), false);
    }

    @Test
    public void testHpoChildren() throws InvalidStringFormatException {
        Set<String> expectedReferences = new HashSet<>();
        expectedReferences.add("http://purl.obolibrary.org/obo/HP_0009811");
        expectedReferences.add("http://purl.obolibrary.org/obo/HP_0002967");
        expectedReferences.add("http://purl.obolibrary.org/obo/HP_0002996");
        expectedReferences.add("http://purl.obolibrary.org/obo/HP_0001377");
        expectedReferences.add("http://purl.obolibrary.org/obo/HP_0005060");

        String query = DisgenetQueryGenerator.getHpoChildren(new Hpo("hp:0009811"), new SparqlRange(0, true));
        System.out.println(query);
        runner = new QueryRunnerRewindable(readerMini.getModel(), query);
        assertRunnerHpoOutputWithExpectedResults(runner, expectedReferences);
    }

    @Test
    public void hpoGenesUnknownHpoMini() {
        runner = new QueryRunnerRewindable(readerMini.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:1234567"));

        Assert.assertEquals(runner.hasNext(), false, "match found while HPO not in file");
    }

    @Test
    public void hpoGenesHpoInFileMini() {
        runner = new QueryRunnerRewindable(readerMini.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:0009811"));


        int counter = 0;
        while(runner.hasNext()) {
            counter += 1;
            runner.next();
        }

        runner.reset();
        ResultSetFormatter.out(System.out, runner.getResultSet());

        // Expected can be counted in file using regex: ^<http://rdf.disgenet.org/resource/gda/ (note that 2 GDAs do not have a phenotype stored)
        Assert.assertEquals(counter, 12);
    }
}
