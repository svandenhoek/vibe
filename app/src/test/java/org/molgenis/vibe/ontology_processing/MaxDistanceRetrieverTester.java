package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;
import org.molgenis.vibe.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.io.input.OntologyModelFilesReader;

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
    private static OntModel model;

    @BeforeAll
    public static void beforeAll() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader(TestData.HPO_OWL.getFullPath());
        model = reader.getModel();
    }

    @Test
    public void retrieveWithDistance0() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        testRetriever(startPhenotypes, 0, expectedPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithDistance1() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0002996"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005060"), 1);

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        testRetriever(startPhenotypes, 1, expectedPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithDistance2() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0002996"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005060"), 1);
        expectedNetwork1.add(new Phenotype("hp:0009811"), 2);
        expectedNetwork1.add(new Phenotype("hp:0001376"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006376"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006394"), 2);
        expectedNetwork1.add(new Phenotype("hp:0002987"), 2);

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        testRetriever(startPhenotypes, 2, expectedPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithDistance3With2RoutesHavingDifferentDistanceToHpo() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0005060"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 1);
        expectedNetwork1.add(new Phenotype("hp:0006376"), 1);
        expectedNetwork1.add(new Phenotype("hp:0002996"), 2);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006471"), 2);
        expectedNetwork1.add(new Phenotype("hp:0009811"), 3);
        expectedNetwork1.add(new Phenotype("hp:0001376"), 3);
        expectedNetwork1.add(new Phenotype("hp:0006394"), 3);
        expectedNetwork1.add(new Phenotype("hp:0002987"), 3);

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        testRetriever(startPhenotypes, 3, expectedPhenotypeNetworkCollection);
    }

    public void testRetriever(List<Phenotype> startPhenotypes, int maxDistance, PhenotypeNetworkCollection expectedPhenotypeNetworkCollection) {
        PhenotypesRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, maxDistance);
        retriever.run();
        Assertions.assertEquals(expectedPhenotypeNetworkCollection, retriever.getPhenotypeNetworkCollection());
    }
}
