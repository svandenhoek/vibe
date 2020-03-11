package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;
import org.molgenis.vibe.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.io.input.OntologyModelFilesReader;

import java.util.Arrays;
import java.util.List;

public class PhenotypesRetrieverFactoryIT {
    private static OntModel model;

    @BeforeAll
    public static void beforeAll() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader(TestData.HPO_OWL.getFullPath());
        model = reader.getModel();
    }

    @AfterAll
    public static void afterAll() {
        if(model != null) {
            model.close();
        }
    }

    @Test
    public void retrieveWithDistance0() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0001377"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.DISTANCE.create(model, startPhenotypes, 0);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
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

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.DISTANCE.create(model, startPhenotypes, 1);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
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

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.DISTANCE.create(model, startPhenotypes, 2);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
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

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.DISTANCE.create(model, startPhenotypes, 3);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
    }

    @Test
    public void retrieveWithChildren0() {
        List<Phenotype> startPhenotypes = Arrays.asList(new Phenotype("hp:0002996"));

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));

        PhenotypeNetworkCollection expectedPhenotypeNetworkCollection = new PhenotypeNetworkCollection();
        expectedPhenotypeNetworkCollection.add(expectedNetwork1);

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.CHILDREN.create(model, startPhenotypes, 0);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
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

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.CHILDREN.create(model, startPhenotypes, 1);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
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

        PhenotypesRetriever retriever = PhenotypesRetrieverFactory.CHILDREN.create(model, startPhenotypes, 2);
        testRetriever(retriever, expectedPhenotypeNetworkCollection);
    }

    public void testRetriever(PhenotypesRetriever retriever, PhenotypeNetworkCollection expectedPhenotypeNetworkCollection) {
        retriever.run();
        Assertions.assertEquals(expectedPhenotypeNetworkCollection, retriever.getPhenotypeNetworkCollection());
    }
}
