package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;
import org.molgenis.vibe.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.io.OntologyModelFilesReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Note that these tests use data from the Human Phenotype Ontology for validation. However, this was kept as minimal as
 * possible while still being able to actually test the functioning of the code and only reflects what is EXPECTED to be
 * found within the Human Phenotype Ontology. Additionally, the actual data on which these tests are executed on are
 * available externally and are not included in the repository itself.
 *
 * The full Human Phenotype Ontology dataset can be downloaded from: https://human-phenotype-ontology.github.io/downloads.html
 * The license can be found on: https://human-phenotype-ontology.github.io/license.html
 *
 * Please view the README.md in the externally downloadable resources for an overview about how all the HPOs are connected
 * to each other.
 */
public class MaxDistanceRetrieverTester {
    private OntModel model;
    private List<Phenotype> startPhenotypes;
    private PhenotypeNetworkCollection expectedPhenotypeNetworkCollection;

    @BeforeClass
    public void beforeClass() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader(TestData.ONTOLOGY_FILE.getFiles()[0]);
        model = reader.getModel();
    }

    @BeforeMethod
    public void beforeMethod() {
        startPhenotypes = new ArrayList<>();
        expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
    }

    public void testRetriever1(int maxDistance) {
        PhenotypesRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, maxDistance);
        retriever.run();
        Assert.assertEquals(retriever.getPhenotypeNetworkCollection(), expectedPhenotypeNetworkCollection);
    }

    public void testRetriever2(int maxDistance) {
        PhenotypesRetriever retriever = new MaxDistanceRetriever2(model, startPhenotypes, maxDistance);
        retriever.run();
        Assert.assertEquals(retriever.getPhenotypeNetworkCollection(), expectedPhenotypeNetworkCollection);
    }

    @Test
    public void retriever1WithDistance0() {
        retrieveWithDistance0();
        testRetriever1(0);
    }

    @Test
    public void retriever2WithDistance0() {
        retrieveWithDistance0();
        testRetriever2(0);
    }

    public void retrieveWithDistance0() {
        startPhenotypes.add(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 0);
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);
    }

    @Test
    public void retriever1WithDistance1() {
        retrieveWithDistance1();
        testRetriever1(1);
    }

    @Test
    public void retriever2WithDistance1() {
        retrieveWithDistance1();
        testRetriever2(1);
    }

    public void retrieveWithDistance1() {
        startPhenotypes.add(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 0);
        expectedNetwork1.add(new Phenotype("hp:0002996"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005060"), 1);
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);
    }

    @Test
    public void retriever1WithDistance2() {
        retrieveWithDistance2();
        testRetriever1(2);
    }

    @Test
    public void retriever2WithDistance2() {
        retrieveWithDistance2();
        testRetriever2(2);
    }


    public void retrieveWithDistance2() {
        startPhenotypes.add(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 0);
        expectedNetwork1.add(new Phenotype("hp:0002996"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005060"), 1);
        expectedNetwork1.add(new Phenotype("hp:0009811"), 2);
        expectedNetwork1.add(new Phenotype("hp:0001376"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006376"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006394"), 2);
        expectedNetwork1.add(new Phenotype("hp:0002987"), 2);
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);
    }

    @Test
    public void retriever1WithDistance3With2RoutesHavingDifferentDistanceToHpo() {
        retrieveWithDistance3With2RoutesHavingDifferentDistanceToHpo();
        testRetriever1(3);
    }

    @Test
    public void retriever2WithDistance3With2RoutesHavingDifferentDistanceToHpo() {
        retrieveWithDistance3With2RoutesHavingDifferentDistanceToHpo();
        testRetriever2(3);
    }

    public void retrieveWithDistance3With2RoutesHavingDifferentDistanceToHpo() {
        startPhenotypes.add(new Phenotype("hp:0005060"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0005060"), 0);
        expectedNetwork1.add(new Phenotype("hp:0001377"), 1);
        expectedNetwork1.add(new Phenotype("hp:0006376"), 1);
        expectedNetwork1.add(new Phenotype("hp:0002996"), 2);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006471"), 2);
        expectedNetwork1.add(new Phenotype("hp:0009811"), 3);
        expectedNetwork1.add(new Phenotype("hp:0001376"), 3);
        expectedNetwork1.add(new Phenotype("hp:0006394"), 3);
        expectedNetwork1.add(new Phenotype("hp:0002987"), 3);
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);
    }
}
