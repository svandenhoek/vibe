package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.util.*;

class BiologicalEntityCollectionTest {
    private static BiologicalEntityImpl[] array1;
    private static BiologicalEntityImpl[] array2;
    private static BiologicalEntityCombinationImpl[] combinations;
    private static BiologicalEntityCombinationImpl combinationDuplicate;

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
    @BeforeAll
    static void beforeAll() {
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

        combinationDuplicate = new BiologicalEntityCombinationImpl(array1[0], array2[0]);
    }

    // Tests.
    @Test
    void addMultipleCombinationsAndRetrieveSingleOne() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        Assertions.assertEquals(combinations[1], collection.get(combinations[1]));
    }

    @Test
    void testGetT3Ordered() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        Assertions.assertEquals(Arrays.asList(combinations), collection.getT3Ordered());
    }

    @Test
    void addMultipleCombinationsAndRetrieveByT1() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));

        Set<BiologicalEntityCombinationImpl> expectedOutput = new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5]));
        Assertions.assertEquals(expectedOutput, collection.getByT1(array1[1]));
    }

    @Test
    void addMultipleCombinationsAndRetrieveByT2() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));

        Set<BiologicalEntityCombinationImpl> expectedOutput = new HashSet<>(Arrays.asList(combinations[1],combinations[2]));
        Assertions.assertEquals(expectedOutput, collection.getByT2(array2[1]));
    }

    @Test
    void retrieveByNonExistingT1() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assertions.assertEquals(null, collection.getByT1(array1[1]));
    }

    @Test
    void retrieveByNonExistingT2() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assertions.assertEquals(null, collection.getByT2(array2[1]));
    }

    @Test
    void testAddSingleCombination() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);

        Set<BiologicalEntityCombinationImpl> expectedOutput = new HashSet<>(Arrays.asList(combinations[0]));

        Assertions.assertAll(
                () -> Assertions.assertEquals(combinations[0], collection.get(combinations[0])),
                () -> Assertions.assertEquals(expectedOutput, collection.getByT1(array1[0])),
                () -> Assertions.assertEquals(expectedOutput, collection.getByT2(array2[0]))
        );
    }

    @Test
    void testRemoveSingleCombinationWithoutTriggeringRemovingEmptySet() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.remove(combinations[2]);

        Assertions.assertAll(
                // Validate full collection.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[0],combinations[1],combinations[3],combinations[4],combinations[5])), collection.getT3()),

                // Validate if grouped by gene is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[0],combinations[1])), collection.getByT1(array1[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1])),

                // Validate if grouped by disease is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[0])), collection.getByT2(array2[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[1])), collection.getByT2(array2[1])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]))
        );
    }

    @Test
    void testRemoveSingleCombinationWithTriggeringRemovingEmptySet() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.remove(combinations[0]);

        Assertions.assertAll(
                // Validate full collection.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[1],combinations[2],combinations[3],combinations[4],combinations[5])), collection.getT3()),

                // Validate if grouped by gene is correct.
                () ->  Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[1])), collection.getByT1(array1[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1])),

                // Validate if grouped by disease is correct.
                () -> Assertions.assertEquals(null, collection.getByT2(array2[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[1],combinations[2])), collection.getByT2(array2[1])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]))
        );
    }

    @Test
    void testAddAll() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));

        Assertions.assertAll(
                // Validate if full collection is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations)), collection.getT3()),

                // Validate if grouped by T1 is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[0],combinations[1])), collection.getByT1(array1[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1])),

                // Validate if grouped by T2 is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[0])), collection.getByT2(array2[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[1], combinations[2])), collection.getByT2(array2[1])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]))
        );
    }

    @Test
    void removeAll() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.removeAll(Arrays.asList(combinations[0],combinations[1]));

        Assertions.assertAll(
                // Validate if full collection is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getT3()),

                // Validate if grouped by T1 is correct.
                () -> Assertions.assertEquals(null, collection.getByT1(array1[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2],combinations[3],combinations[4],combinations[5])), collection.getByT1(array1[1])),

                // Validate if grouped by T2 is correct.
                () -> Assertions.assertEquals(null, collection.getByT2(array2[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getByT2(array2[1])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[3])), collection.getByT2(array2[2])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[4])), collection.getByT2(array2[3])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[5])), collection.getByT2(array2[4]))
        );
    }

    @Test
    void testRetainAll() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.retainAll(Arrays.asList(combinations[2]));

        Assertions.assertAll(
                // Validate if full collection is correct.
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getT3()),

                // Validate if grouped by gene is correct.
                () -> Assertions.assertEquals(null, collection.getByT1(array1[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getByT1(array1[1])),

                // Validate if grouped by disease is correct.
                () -> Assertions.assertEquals(null, collection.getByT2(array2[0])),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[2])), collection.getByT2(array2[1])),
                () -> Assertions.assertEquals(null, collection.getByT2(array2[2])),
                () -> Assertions.assertEquals(null, collection.getByT2(array2[3])),
                () -> Assertions.assertEquals(null, collection.getByT2(array2[4]))
        );
    }

    @Test
    void testClear() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.clear();
        Assertions.assertEquals(new HashSet<>(), collection.getT3());
    }

    @Test
    void testIsEmptyWhenNotYetUsed() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        Assertions.assertTrue(collection.isEmpty());
    }

    @Test
    void testIsEmptyWhenNotEmpty() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assertions.assertFalse(collection.isEmpty());
    }

    @Test
    void testIsEmptyAfterClearing() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        collection.clear();
        Assertions.assertTrue(collection.isEmpty());
    }

    @Test
    void testAddingAlreadyExistingItem() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        boolean added1 = collection.add(combinations[0]);
        boolean added2 = collection.add(combinationDuplicate);

        Assertions.assertAll(
                () -> Assertions.assertTrue(added1),
                () -> Assertions.assertFalse(added2),
                () -> Assertions.assertEquals(new HashSet<>(Arrays.asList(combinations[0])), collection.getT3())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6})
    void testSize(int number) {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        for(int i = 0; i < number; i++) {
            collection.add(combinations[i]);
        }
        Assertions.assertEquals(number, collection.size());
    }

    @Test
    void containsifPresent() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assertions.assertTrue(collection.contains(combinations[0]));
    }

    @Test
    void containsifNotPresent() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.add(combinations[0]);
        Assertions.assertFalse(collection.contains(combinations[1]));
    }

    @Test
    void containsifNotPresentDueToRemoval() {
        BiologicalEntityCollectionImpl collection = new BiologicalEntityCollectionImpl();
        collection.addAll(Arrays.asList(combinations));
        collection.remove(combinations[3]);
        Assertions.assertFalse(collection.contains(combinations[3]));
    }

    @Test
    void hashNotEqual() {
        BiologicalEntityCollectionImpl collection1 = new BiologicalEntityCollectionImpl();
        collection1.add(combinations[0]);
        collection1.add(combinations[1]);

        BiologicalEntityCollectionImpl collection2 = new BiologicalEntityCollectionImpl();
        collection2.add(combinations[2]);

        Assertions.assertNotEquals(collection1.hashCode(), collection2.hashCode());
    }
}
