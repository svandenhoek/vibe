package org.molgenis.vibe.formats;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.util.*;

public class BiologicalEntityCollectionTester {
    private static BiologicalEntityImpl[] array1;
    private static BiologicalEntityImpl[] array2;
    private static BiologicalEntityCombinationImpl[] combinations;

    // Implementation of classes.
    private static class BiologicalEntityImpl extends BiologicalEntity {
        public BiologicalEntityImpl(String id) {
            super(id);
        }

        public BiologicalEntityImpl(URI uri) {
            super(uri);
        }

        public BiologicalEntityImpl(String id, String name) {
            super(id, name);
        }

        public BiologicalEntityImpl(URI uri, String name) {
            super(uri, name);
        }

        @Override
        protected String getIdPrefix() {
            return "prefix:";
        }

        @Override
        protected String getIdRegex() {
            return "^prefix:([0-9]+)$";
        }

        @Override
        protected int getRegexIdGroup() {
            return 1;
        }

        @Override
        protected String getUriPrefix() {
            return "https://test.com/path/";
        }
    }

    private static class BiologicalEntityCombinationImpl extends BiologicalEntityCombination<BiologicalEntityImpl,BiologicalEntityImpl> {
        public BiologicalEntityCombinationImpl(BiologicalEntityImpl biologicalEntity, BiologicalEntityImpl biologicalEntity2) {
            super(biologicalEntity, biologicalEntity2);
        }
    }

    private static class BiologicalEntityCollectionImpl extends BiologicalEntityCollection<BiologicalEntityImpl,BiologicalEntityImpl,BiologicalEntityCombinationImpl> {
        public BiologicalEntityCollectionImpl() {
        }

        public BiologicalEntityCollectionImpl(Collection<? extends BiologicalEntityCombinationImpl> combinations) {
            super(combinations);
        }
    }

    // Preparing data.
    @BeforeClass
    public static void beforeClass() {
        array1 = new BiologicalEntityImpl[]{
                new BiologicalEntityImpl("prefix:1"),
                new BiologicalEntityImpl("prefix:2")
        };

        array2 = new BiologicalEntityImpl[]{
                new BiologicalEntityImpl("prefix:11"),
                new BiologicalEntityImpl("prefix:12"),
                new BiologicalEntityImpl("prefix:13"),
                new BiologicalEntityImpl("prefix:14"),
                new BiologicalEntityImpl("prefix:15")
        };

        combinations = new BiologicalEntityCombinationImpl[]{
                new BiologicalEntityCombinationImpl(array1[0], array2[0]),
                new BiologicalEntityCombinationImpl(array1[0], array2[1]),
                new BiologicalEntityCombinationImpl(array1[1], array2[1]),
                new BiologicalEntityCombinationImpl(array1[1], array2[2]),
                new BiologicalEntityCombinationImpl(array1[1], array2[3]),
                new BiologicalEntityCombinationImpl(array1[1], array2[4])
        };
    }

    // Tests.
    @Test
    public void addMultipleCombinationsAndRetrieveSingleOne() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        Assert.assertEquals(combinations[1], collection.get(combinations[1]));
    }

    @Test
    public void testGetT3Ordered() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        Assert.assertEquals(Arrays.asList(combinations), collection.getT3Ordered());
    }

    @Test
    public void addMultipleCombinationsAndRetrieveByT1() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));

        Set<BiologicalEntityCombinationImpl> expectedOutput = new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5]));
        Assert.assertEquals(expectedOutput, collection.getByT1(array1[1]));
    }

    @Test
    public void addMultipleCombinationsAndRetrieveByT2() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));

        Set<BiologicalEntityCombinationImpl> expectedOutput = new HashSet<>(Arrays.asList(combinations[1],combinations[2]));
        Assert.assertEquals(expectedOutput, collection.getByT2(array2[1]));
    }

    @Test
    public void retrieveByNonExistingT1() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assert.assertEquals(null, collection.getByT1(array1[1]));
    }

    @Test
    public void retrieveByNonExistingT2() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assert.assertEquals(null, collection.getByT2(array2[1]));
    }

    @Test
    public void testAddSingleCombination() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);

        Set<BiologicalEntityCombinationImpl> expectedOutput = new HashSet<>(Arrays.asList(combinations[0]));

        Assert.assertEquals(combinations[0], collection.get(combinations[0]));
        Assert.assertEquals(expectedOutput, collection.getByT1(array1[0]));
        Assert.assertEquals(expectedOutput, collection.getByT2(array2[0]));
    }

    @Test
    public void testRemoveSingleCombinationWithoutTriggeringRemovingEmptySet() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.remove(combinations[2]);

        // Validate full collection.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[0],combinations[1],combinations[3],combinations[4],combinations[5])), collection.getT3());

        // Validate if grouped by gene is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[0],combinations[1])), collection.getByT1(array1[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1]));

        // Validate if grouped by disease is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[0])), collection.getByT2(array2[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[1])), collection.getByT2(array2[1]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]));
    }

    @Test
    public void testRemoveSingleCombinationWithTriggeringRemovingEmptySet() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.remove(combinations[0]);

        // Validate full collection.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[1],combinations[2],combinations[3],combinations[4],combinations[5])), collection.getT3());

        // Validate if grouped by gene is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[1])), collection.getByT1(array1[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1]));

        // Validate if grouped by disease is correct.
        Assert.assertEquals(null, collection.getByT2(array2[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[1],combinations[2])), collection.getByT2(array2[1]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]));
    }

    @Test
    public void testAddAll() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));

        // Validate if full collection is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations)), collection.getT3());

        // Validate if grouped by T1 is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[0],combinations[1])), collection.getByT1(array1[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1]));

        // Validate if grouped by T2 is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[0])), collection.getByT2(array2[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[1], combinations[2])), collection.getByT2(array2[1]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]));
    }

    @Test
    public void removeAll() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.removeAll(Arrays.asList(combinations[0],combinations[1]));

        // Validate if full collection is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getT3());

        // Validate if grouped by T1 is correct.
        Assert.assertEquals(null, collection.getByT1(array1[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1]));

        // Validate if grouped by T2 is correct.
        Assert.assertEquals(null, collection.getByT2(array2[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getByT2(array2[1]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]));
    }

    @Test
    public void testRetainAll() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.retainAll(Arrays.asList(combinations[2]));

        // Validate if full collection is correct.
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getT3());

        // Validate if grouped by gene is correct.
        Assert.assertEquals(null, collection.getByT1(array1[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getByT1(array1[1]));

        // Validate if grouped by disease is correct.
        Assert.assertEquals(null, collection.getByT2(array2[0]));
        Assert.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getByT2(array2[1]));
        Assert.assertEquals(null, collection.getByT2(array2[2]));
        Assert.assertEquals(null, collection.getByT2(array2[3]));
        Assert.assertEquals(null, collection.getByT2(array2[4]));
    }
}
