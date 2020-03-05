package org.molgenis.vibe.formats;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

public class PhenotypeNetworkCollectionTester {
    private static final Phenotype[] phenotypes1 = new Phenotype[]{
            new Phenotype("hp:0000000"),
            new Phenotype("hp:0000001"),
            new Phenotype("hp:0000002"),
            new Phenotype("hp:0000003"),
            new Phenotype("hp:0000004") // overlaps between set 1 and 2
    };

    private static final Phenotype[] phenotypes2 = new Phenotype[]{
            new Phenotype("hp:1000000"),
            new Phenotype("hp:1000001"),
            new Phenotype("hp:1000002"),
            new Phenotype("hp:1000003"),
            new Phenotype("hp:0000004") // overlaps between set 1 and 2
    };

    private static PhenotypeNetwork network1;
    private static PhenotypeNetwork network2;

    @BeforeAll
    public static void beforeAll() {
        network1 = new PhenotypeNetwork(phenotypes1[0]);
        network1.add(Arrays.asList(phenotypes1[1], phenotypes1[2], phenotypes1[3]), 1);
        network1.add(Arrays.asList(phenotypes1[4]), 2);

        network2 = new PhenotypeNetwork(phenotypes2[0]);
        network2.add(Arrays.asList(phenotypes2[1]), 1);
        network2.add(Arrays.asList(phenotypes2[2], phenotypes2[3], phenotypes2[4]), 2);
    }

    @Test
    public void testAdd() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        Assertions.assertEquals(network1, collection.getPhenotypeNetworkBySource(network1.getSource()));
    }

    @Test
    public void testRetrieveAllPhenotypes() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        collection.add(network2);
        Assertions.assertEquals(new HashSet<>(Arrays.asList(ArrayUtils.addAll(phenotypes1, phenotypes2))), collection.getPhenotypes());
    }

    @Test
    public void testRemovalOfStoredNetworkThroughNetwork() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        collection.add(network2);
        boolean outValue = collection.remove(network1);

        Assertions.assertAll(
                () -> Assertions.assertTrue(outValue),
                () -> Assertions.assertNull(collection.getPhenotypeNetworkBySource(network1.getSource())),
                () -> Assertions.assertEquals(network2, collection.getPhenotypeNetworkBySource(network2.getSource())),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(phenotypes2)), collection.getPhenotypes())
        );
    }

    @Test
    public void testRemovalOfStoredNetworkThroughSourcePhenotype() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        collection.add(network2);
        PhenotypeNetwork outValue = collection.remove(network1.getSource());
        
        Assertions.assertAll(
                () -> Assertions.assertEquals(network1, outValue),
                () -> Assertions.assertNull(collection.getPhenotypeNetworkBySource(network1.getSource())),
                () -> Assertions.assertEquals(network2, collection.getPhenotypeNetworkBySource(network2.getSource())),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(phenotypes2)), collection.getPhenotypes())
        );
    }

    @Test
    public void testRemovalOfNonStoredNetworkThroughNetwork() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        Assertions.assertFalse(collection.remove(network2));
    }

    @Test
    public void testRemovalOfNonStoredNetworkThroughSourcePhenotype() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        Assertions.assertNull(collection.remove(network2.getSource()));
    }

    @Test
    public void testClear() {
        PhenotypeNetworkCollection collection = new PhenotypeNetworkCollection();
        collection.add(network1);
        collection.clear();
        Assertions.assertEquals(new HashSet<>(), collection.getPhenotypes());
    }
}
