package org.molgenis.vibe.core;

import org.junit.jupiter.api.*;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.formats.PhenotypeNetwork;
import org.molgenis.vibe.core.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class PhenotypesRetrievalRunnerIT {
    private static PhenotypesRetrievalRunner runner;

    @AfterEach
    public void afterEach() {
        runner.close();
    }

    @Test
    public void retrieveWithDistance0() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.DISTANCE, startPhenotypes, 0);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
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

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.DISTANCE, startPhenotypes, 1);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
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

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.DISTANCE, startPhenotypes, 2);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
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

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.DISTANCE, startPhenotypes, 3);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithChildren0() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0002996"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.CHILDREN, startPhenotypes, 0);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithChildren1() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0002996"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 1);
        expectedNetwork1.add(new Phenotype("hp:0002987"), 1);
        expectedNetwork1.add(new Phenotype("hp:0006376"), 1);
        expectedNetwork1.add(new Phenotype("hp:0006394"), 1);

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.CHILDREN, startPhenotypes, 1);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithChildren2() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0002996"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 1);
        expectedNetwork1.add(new Phenotype("hp:0002987"), 1);
        expectedNetwork1.add(new Phenotype("hp:0006376"), 1);
        expectedNetwork1.add(new Phenotype("hp:0006394"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005060"), 2);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 2);
        expectedNetwork1.add(new Phenotype("hp:0006471"), 2);

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.CHILDREN, startPhenotypes, 2);
        PhenotypeNetworkCollection actualPhenotypeNetworkCollection = runner.call();

        Assertions.assertEquals(expectedPhenotypeNetworkCollection, actualPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithNegativeDistance() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0002996"));

        runner = new PhenotypesRetrievalRunner(TestData.HPO_OWL.getFullPath(),
                PhenotypesRetrieverFactory.CHILDREN, startPhenotypes, 2);
        int newMaxDistance = -1;

        Exception exception = Assertions.assertThrows(InvalidParameterException.class, () -> runner.setMaxDistance(newMaxDistance) );
        Assertions.assertEquals("maxDistance must be >= 0: " + newMaxDistance, exception.getMessage());
    }
}
