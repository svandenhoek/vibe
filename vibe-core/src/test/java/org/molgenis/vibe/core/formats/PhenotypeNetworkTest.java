package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class PhenotypeNetworkTest {
    private static final Phenotype[] phenotypes = new Phenotype[]{
            new Phenotype("hp:0000000"),
            new Phenotype("hp:0000001"),
            new Phenotype("hp:0000002"),
            new Phenotype("hp:0000003"),
            new Phenotype("hp:0000004")
    };
    private Map<Integer, Set<Phenotype>> expectedNetwork;
    private PhenotypeNetwork phenotypeNetwork;


    @BeforeEach
    public void beforeEach() {
        phenotypeNetwork = new PhenotypeNetwork(phenotypes[0]);

        // The network as should be created within the class itself with as source phenotypes[0].
        expectedNetwork = new HashMap<>();
        HashSet<Phenotype> phenotypesDistance0 = new HashSet<>();
        phenotypesDistance0.add(phenotypes[0]);
        expectedNetwork.put(0, phenotypesDistance0);
    }

    @Test
    public void testSimpleNetwork() {
        addExpectedPhenotypesUsingDistance(new int[]{1,2}, 1);
        addExpectedPhenotypesUsingDistance(new int[]{3,4}, 2);

        phenotypeNetwork.add(expectedNetwork.get(1), 1);
        phenotypeNetwork.add(expectedNetwork.get(2), 2);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedNetwork.keySet(), phenotypeNetwork.getDistances()),
                () -> Assertions.assertEquals(expectedNetwork.get(0), phenotypeNetwork.getByDistance(0)),
                () -> Assertions.assertEquals(expectedNetwork.get(1), phenotypeNetwork.getByDistance(1)),
                () -> Assertions.assertEquals(expectedNetwork.get(2), phenotypeNetwork.getByDistance(2)),
                () -> Assertions.assertThrows(NullPointerException.class, () -> phenotypeNetwork.getByDistance(3))
        );
    }

    @Test
    public void testReplacingDistance() {
        addExpectedPhenotypesUsingDistance(new int[]{1}, 3);

        phenotypeNetwork.add(phenotypes[1], 9);
        phenotypeNetwork.add(phenotypes[1], 3); // 9 should be replaced
        phenotypeNetwork.add(phenotypes[1], 6); // 3 should not be replaced

        Assertions.assertEquals(3, phenotypeNetwork.getDistance(phenotypes[1]));
    }

    @Test
    public void testContainsForSourcePhenotype() {
        Assertions.assertTrue(phenotypeNetwork.contains(phenotypes[0]));
    }

    @Test
    public void testContainsForAddedPhenotype() {
        phenotypeNetwork.add(phenotypes[1], 3);
        Assertions.assertTrue(phenotypeNetwork.contains(phenotypes[1]));
    }

    @Test
    public void testContainsForNotAddedPhenotype() {
        Assertions.assertFalse(phenotypeNetwork.contains(phenotypes[1]));
    }

    /**
     * Adds phenotypes with the given distance to the {@code expectedNetwork}.
     * @param phenotypePositions the positions in {@code phenotypes} of the phenotypes that should be added to the
     * {@code expectedNetwork}
     * @param distance the distance to which the phenotypes should be added
     */
    private void addExpectedPhenotypesUsingDistance(int[] phenotypePositions, int distance) {
        HashSet<Phenotype> phenotypesForDistance = new HashSet<>();
        for(int i : phenotypePositions) {
            phenotypesForDistance.add(phenotypes[i]);
        }
        expectedNetwork.put(distance, phenotypesForDistance);
    }
}
