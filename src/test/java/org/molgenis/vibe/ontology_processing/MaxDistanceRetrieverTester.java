package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.OntologyModelFilesReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Note that these tests use data from the Human Phenotype Ontology for validation. However, this was kept as minimal as
 * possible while still being able to actually test the functioning of the code and only reflects what is EXPECTED to be
 * found within the Human Phenotype Ontology. Additionally, the actual data on which these tests are executed on are
 * available externally and are not included in the repository itself.
 *
 * The full Human Phenotype Ontology dataset can be downloaded from: https://human-phenotype-ontology.github.io/downloads.html
 * The license can be found on: https://human-phenotype-ontology.github.io/license.html
 */
public class MaxDistanceRetrieverTester {
    private OntModel model;
    @BeforeClass
    public void beforeClass() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader();
        reader.read(TestData.ONTOLOGY_FILE.getFiles()[0]);
        model = reader.getModel();
    }

    @Test
    public void retrieveWithDistance0() {
        Set<Phenotype> startPhenotypes = new HashSet<>();
        startPhenotypes.add(new Phenotype("hp:0001377"));

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 0);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypes(), startPhenotypes);
    }

    /**
     * HP_0001377 subClassOf HP_0002996 -> 1
     * HP_0005852 subClassOf HP_0001377 -> 1
     * HP_0005060 subClassOf HP_0001377 -> 1
     */
    @Test
    public void retrieveWithDistance1() {
        Set<Phenotype> startPhenotypes = new HashSet<>();
        startPhenotypes.add(new Phenotype("hp:0001377"));

        Set<Phenotype> expectedOutput = new HashSet<>();
        expectedOutput.addAll(Arrays.asList(
                new Phenotype("hp:0001377"), // subClassOf HP_0002996
                new Phenotype("hp:0002996"),
                new Phenotype("hp:0005852"),
                new Phenotype("hp:0005060")
        ));

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 1);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypes(), expectedOutput);
    }

    /**
     * START: HP_0001377 subClassOf HP_0002996 -> 1
     * HP_0002987 subClassOf HP_0002996 -> 2
     * HP_0006376 subClassOf HP_0002996 -> 2
     * HP_0006394 subClassOf HP_0002996 -> 2
     * HP_0002996 subClassOf HP_0001376 -> 2 [A]
     * HP_0002996 subClassOf HP_0009811 -> 2
     * HP_0005852 subClassOf HP_0001377 -> 1
     * HP_0005060 subClassOf HP_0001377 -> 1
     * HP_0005060 subClassOf HP_0006376 -> 2 [A]
     *
     * Numbers indicate the distance to a HPO, characters indicate a reference to the same HPO through a different route.
     */
    @Test
    public void retrieveWithDistance2() {
        Set<Phenotype> startPhenotypes = new HashSet<>();
        startPhenotypes.add(new Phenotype("hp:0001377"));

        Set<Phenotype> expectedOutput = new HashSet<>();
        expectedOutput.addAll(Arrays.asList(
                new Phenotype("hp:0001377"),
                new Phenotype("hp:0002987"),
                new Phenotype("hp:0006376"),
                new Phenotype("hp:0006394"),
                new Phenotype("hp:0002996"),
                new Phenotype("hp:0005852"),
                new Phenotype("hp:0005060"),
                new Phenotype("hp:0001376"),
                new Phenotype("hp:0009811")

        ));

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 2);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypes(), expectedOutput);
    }
}
