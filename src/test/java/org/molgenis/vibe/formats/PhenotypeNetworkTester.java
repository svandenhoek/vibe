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
    private List<Set<Phenotype>> expectedOutput;
    private PhenotypeNetwork phenotypeNetwork;


    @BeforeMethod
    public void beforeMethod() {
        phenotypeNetwork = new PhenotypeNetwork(phenotypes[0]);
        expectedOutput = new ArrayList();
        addArrayRangeToExpectedOutputList(0,1); // Adds first element as source.
    }

    @Test
    public void testSimpleNetwork() {
        addArrayRangeToExpectedOutputList(1,3);
        addArrayRangeToExpectedOutputList(3,5);

        phenotypeNetwork.add(expectedOutput.get(1), 1);
        phenotypeNetwork.add(expectedOutput.get(2), 2);

        Assert.assertEquals(phenotypeNetwork.getMaxDistance(), 2);
        for(int i = 0; i < phenotypeNetwork.getMaxDistance(); i++) {
            Assert.assertEquals(phenotypeNetwork.getByDistance(i), expectedOutput.get(i));
        }
    }

    //todo: more tests

    /**
     * @see Arrays#copyOfRange(Object[], int, int)
     */
    private void addArrayRangeToExpectedOutputList(int from, int to) {
        Set<Phenotype> expectedOutputForADistance = new HashSet<>();
        expectedOutputForADistance.addAll(Arrays.asList(Arrays.copyOfRange(phenotypes, from, to)));
        expectedOutput.add(expectedOutputForADistance);
    }
}
