package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;
import org.molgenis.vibe.io.OntologyModelFilesReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
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
    @BeforeClass
    public void beforeClass() {
        OntologyModelFilesReader reader = new OntologyModelFilesReader(TestData.ONTOLOGY_FILE.getFiles()[0]);
        model = reader.getModel();
    }

    @Test
    public void retrieveWithDistance0() {
        List<Phenotype> startPhenotypes = new ArrayList<>();
        startPhenotypes.add(new Phenotype("hp:0001377"));

        Set<PhenotypeNetwork> expectedOutput = new HashSet<>();

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 0);
        expectedOutput.add(expectedNetwork1);

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 0);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypeNetworks(), expectedOutput);
    }

    @Test
    public void retrieveWithDistance1() {
        List<Phenotype> startPhenotypes = new ArrayList<>();
        startPhenotypes.add(new Phenotype("hp:0001377"));

        Set<PhenotypeNetwork> expectedOutput = new HashSet<>();

        PhenotypeNetwork expectedNetwork1 = new PhenotypeNetwork(startPhenotypes.get(0));
        expectedNetwork1.add(new Phenotype("hp:0001377"), 0);
        expectedNetwork1.add(new Phenotype("hp:0002996"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005852"), 1);
        expectedNetwork1.add(new Phenotype("hp:0005060"), 1);
        expectedOutput.add(expectedNetwork1);

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 1);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypeNetworks(), expectedOutput);
    }

    @Test
    public void retrieveWithDistance2() {
        List<Phenotype> startPhenotypes = new ArrayList<>();
        startPhenotypes.add(new Phenotype("hp:0001377"));

        Set<PhenotypeNetwork> expectedOutput = new HashSet<>();

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
        expectedOutput.add(expectedNetwork1);

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 2);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypeNetworks(), expectedOutput);
    }

    @Test
    public void retrieveWithDistance3With2RoutesHavingDifferentDistanceToHpo() {
        List<Phenotype> startPhenotypes = new ArrayList<>();
        startPhenotypes.add(new Phenotype("hp:0005060"));

        Set<PhenotypeNetwork> expectedOutput = new HashSet<>();

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

        expectedOutput.add(expectedNetwork1);

        MaxDistanceRetriever retriever = new MaxDistanceRetriever(model, startPhenotypes, 3);
        retriever.run();
        Assert.assertEquals(retriever.getRetrievedPhenotypeNetworks(), expectedOutput);
    }
}
