package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.molgenis.vibe.TestFile;
import org.molgenis.vibe.io.ModelReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Iterator;

public class DisgenetQueryRunnerTester {
    private DisgenetQueryRunner runner;

    @BeforeClass
    public void initialize() {
        String[] fileSet = new String[]{TestFile.GDA1_RDF.getFilePath(),
                TestFile.GENE_RDF.getFilePath(),
                TestFile.DISEASE_DISEASE_RDF.getFilePath(),
                TestFile.PHENOTYPE_RDF.getFilePath(),
                TestFile.ONTOLOGY.getFilePath()};

        ModelReader reader = new ModelReader();
        runner = new DisgenetQueryRunner(reader.readFiles(fileSet).getModel());
    }

    private void printQueryOutput(ResultSet results) {
        StringBuilder strBuilder = new StringBuilder();
        int i = 0;
        while(results.hasNext()) {
            QuerySolution result = results.next();
            Iterator<String> varNames = result.varNames();
            while(varNames.hasNext()) {
                String varName = varNames.next();
                strBuilder.append(result.get(varName).toString()).append(" - ");
            }

            System.out.println(strBuilder.toString());
            strBuilder.setLength(0);
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidLimit() {
        runner.getGdaGeneDisease(0);
    }

    @Test
    public void geneDiseaseAssociation() {
        ResultSet results = runner.getGdaGeneDisease(10);
        printQueryOutput(results);
    }

//    @Test
//    public void hpoGenes() {
//        ResultSet results = runner.getHpoGenes("hp:0009811", 10);
//        printQueryOutput(results);
//    }
}
