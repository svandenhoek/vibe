package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestFile;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.testng.Assert;
import org.testng.annotations.*;

public class DisgenetQueryGeneratorTester {
    private ModelReader reader;

    private QueryRunnerRewindable runner;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        reader = new TripleStoreDbReader(TestFile.TDB_MINI.getFilePath());
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        reader.close();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        runner.close();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidLimit() {
        new QueryRunner(reader.getModel(), DisgenetQueryGenerator.getGdaGeneDisease(0));
    }

//    @Test
//    public void geneDiseaseAssociation() {
//        ResultSet results = runner.getGdaGeneDisease(10);
//        ResultSetPrinter.print(results);
//    }

    @Test
    public void hpoGenesUnknownHpo() {
        runner = new QueryRunnerRewindable(reader.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:1234567"));

        Assert.assertEquals(runner.hasNext(), false, "match found while HPO not in file");
    }

    @Test
    public void hpoGenesHpoInFile() {
        runner = new QueryRunnerRewindable(reader.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:0009811"));


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
