package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.TestFile;
import org.molgenis.vibe.io.ModelFilesReader;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.ResultSetPrinter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DisgenetQueryGeneratorTester {
    private ModelReader reader;

    private QueryRunner runner;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        String[] fileSet = new String[]{TestFile.GDA1_RDF.getFilePath(),
                TestFile.GENE_RDF.getFilePath(),
                TestFile.DISEASE_DISEASE_RDF.getFilePath(),
                TestFile.PHENOTYPE_RDF.getFilePath(),
                TestFile.PDA_RDF.getFilePath(),
                TestFile.ONTOLOGY.getFilePath()};

        reader = new ModelFilesReader(fileSet);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        reader.close();
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
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
        runner = new QueryRunner(reader.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:1234567"));

        Assert.assertEquals(runner.hasNext(), false, "match found while HPO not in file");
    }

    @Test
    public void hpoGenesHpoInFile() {
        runner = new QueryRunner(reader.getModel(), DisgenetQueryGenerator.getHpoGenes("hp:0009811"));

        int counter = 0;

        if(runner.hasNext()) {
            counter += 1;
            ResultSetPrinter.print(runner.next(), true);
        }

        while(runner.hasNext()) {
            counter += 1;
            QuerySolution result = runner.next();
            if(counter <=10) {
                ResultSetPrinter.print(result, false);
            }
        }
        // Expected can be counted in file using regex: ^<http://rdf.disgenet.org/resource/gda/ (note that 2 GDAs do not have a phenotype stored)
        Assert.assertEquals(counter, 91);
    }
}
