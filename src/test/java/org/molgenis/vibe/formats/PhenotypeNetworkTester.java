package org.molgenis.vibe.formats;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

public class PhenotypeNetworkTester {
    private final Phenotype[] phenotypes= new Phenotype[]{
            new Phenotype("hp:0000000"),
            new Phenotype("hp:0000001"),
            new Phenotype("hp:0000002"),
            new Phenotype("hp:0000003"),
            new Phenotype("hp:0000004")
    };
    private Map<Integer, Set<Phenotype>> expectedNetwork;
    private PhenotypeNetwork phenotypeNetwork;


    @BeforeMethod
    public void beforeMethod() {
        phenotypeNetwork = new PhenotypeNetwork(phenotypes[0]);
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

        Assert.assertEquals(phenotypeNetwork.getDistances(), expectedNetwork.keySet());
        for(Integer distance : phenotypeNetwork.getDistances()) {
            Assert.assertEquals(phenotypeNetwork.getByDistance(distance), expectedNetwork.get(distance));
        }
    }

    @Test
    public void testReplacingDistance() {
        addExpectedPhenotypesUsingDistance(new int[]{1}, 3);

        phenotypeNetwork.add(phenotypes[1], 9);
        phenotypeNetwork.add(phenotypes[1], 3); // 9 should be replaced
        phenotypeNetwork.add(phenotypes[1], 6); // 3 should not be replaced

        Assert.assertEquals(phenotypeNetwork.getDistance(phenotypes[1]), 3);
    }

    //todo: more tests

    private void addExpectedPhenotypesUsingDistance(int[] phenotypePositions, int distance) {
        HashSet<Phenotype> phenotypesForDistance = new HashSet<>();
        for(int i : phenotypePositions) {
            phenotypesForDistance.add(phenotypes[i]);
        }
        expectedNetwork.put(distance, phenotypesForDistance);
    }
}
