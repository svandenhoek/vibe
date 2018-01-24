package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.molgenis.vibe.TestFile;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.ResultSetPrinter;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DisgenetQueryRunnerTester {
    private DisgenetQueryRunner runner;

    @BeforeClass
    public void initialize() {
        String[] fileSet = new String[]{TestFile.GDA1_RDF.getFilePath(),
                TestFile.GENE_RDF.getFilePath(),
                TestFile.DISEASE_DISEASE_RDF.getFilePath(),
                TestFile.PHENOTYPE_RDF.getFilePath(),
                TestFile.PDA_RDF.getFilePath(),
                TestFile.ONTOLOGY.getFilePath()};

        ModelReader reader = new ModelReader();
        runner = new DisgenetQueryRunner(reader.read(fileSet).getModel());
    }



    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidLimit() {
        runner.getGdaGeneDisease(0);
    }

//    @Test
//    public void geneDiseaseAssociation() {
//        ResultSet results = runner.getGdaGeneDisease(10);
//        ResultSetPrinter.print(results);
//    }

    @Test
    public void hpoGenesUnknownHpo() {
        ResultSet results = runner.getHpoGenes("hp:1234567");

        Assert.assertEquals(results.hasNext(), false, "match found while HPO not in file");
    }

    @Test
    public void hpoGenesHpoInFile() {
        ResultSet results = runner.getHpoGenes("hp:0009811");

        int counter = 0;

        if(results.hasNext()) {
            counter += 1;
            ResultSetPrinter.print(results.next(), true);
        }

        while(results.hasNext()) {
            counter += 1;
            QuerySolution result = results.next();
            if(counter <=10) {
                ResultSetPrinter.print(result, false);
            }
        }
        // Expected can be counted in file using regex: ^<http://rdf.disgenet.org/resource/gda/ (note that 2 GDAs do not have a phenotype stored)
        Assert.assertEquals(counter, 91);
    }
}
