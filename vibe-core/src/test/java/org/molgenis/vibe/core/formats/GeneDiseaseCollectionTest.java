package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.*;

public class GeneDiseaseCollectionTest {
    private static GeneDiseaseCombination[] gdcs;
    private static Gene[] genes;
    private static Disease[] diseases;
    private static Source[] sources;

    @BeforeAll
    public static void beforeAll() {
        genes = new Gene[]{
                new Gene("ncbigene:1111111", new GeneSymbol("hgnc:AAA")),
                new Gene("ncbigene:2222222", new GeneSymbol("hgnc:BBB"))
        };

        diseases = new Disease[]{
                new Disease("umls:C1111111"),
                new Disease("umls:C2222222"),
                new Disease("umls:C3333333"),
                new Disease("umls:C4444444"),
                new Disease("umls:C5555555")
        };

        gdcs = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0], 0.5),
                new GeneDiseaseCombination(genes[0], diseases[1], 0.3),
                new GeneDiseaseCombination(genes[1], diseases[1], 0.6),
                new GeneDiseaseCombination(genes[1], diseases[2], 0.3),
                new GeneDiseaseCombination(genes[1], diseases[3], 0.8),
                new GeneDiseaseCombination(genes[1], diseases[4], 0.1)
        };

        sources = new Source[]{
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"), "Orphanet", Source.Level.CURATED),
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"), "Befree", Source.Level.LITERATURE)
        };
    }

    @Test
    public void testGetByGeneOrderedByGdaScore() {
        GeneDiseaseCollection collection = new GeneDiseaseCollection();
        collection.addAll(Arrays.asList(gdcs));

        // Validate order.
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(gdcs[4],gdcs[2],gdcs[3],gdcs[5])), collection.getByGeneOrderedByGdaScore(genes[1]));
    }

    @Test
    public void testGetByDiseaseOrderedByGdaScore() {
        GeneDiseaseCollection collection = new GeneDiseaseCollection();
        collection.addAll(Arrays.asList(gdcs));

        // Validate order.
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(gdcs[2],gdcs[1])), collection.getByDiseaseOrderedByGdaScore(diseases[1]));
    }

    @Test
    public void testequalsWhenequal() {
        GeneDiseaseCollection collection1 = new GeneDiseaseCollection();
        collection1.addAll(Arrays.asList(gdcs[1]));

        GeneDiseaseCollection collection2 = new GeneDiseaseCollection();
        collection2.addAll(Arrays.asList(gdcs[1]));

        Assertions.assertTrue(collection1.equals(collection2));
    }

    @Test
    public void testequalsWhenNotequalDueToDifference() {
        GeneDiseaseCollection collection1 = new GeneDiseaseCollection();
        collection1.addAll(Arrays.asList(gdcs[1]));

        GeneDiseaseCollection collection2 = new GeneDiseaseCollection();
        collection2.addAll(Arrays.asList(gdcs[2]));

        Assertions.assertFalse(collection1.equals(collection2));
    }

    @Test
    public void testequalsWhenNotequalDueToSize() {
        GeneDiseaseCollection collection1 = new GeneDiseaseCollection();
        collection1.addAll(Arrays.asList(gdcs[1]));

        GeneDiseaseCollection collection2 = new GeneDiseaseCollection();
        collection2.addAll(Arrays.asList(gdcs[1], gdcs[2]));

        Assertions.assertFalse(collection1.equals(collection2));
    }

    /**
     * While {@link GeneDiseaseCollection#allFieldsEquals(Object)} should not return {@code false} if
     * {@link GeneDiseaseCollection#equals(Object)}, this test ensures the custom deep equals works correctly for usage
     * in other tests.
     */
    @Test
    public void testAllEqualsWhenGdcHasDifferentScore() {
        GeneDiseaseCollection collection1 = new GeneDiseaseCollection();
        collection1.add(new GeneDiseaseCombination(genes[0], diseases[0], 0.5));

        GeneDiseaseCollection collection2 = new GeneDiseaseCollection();
        collection2.add(new GeneDiseaseCombination(genes[0], diseases[0], 0.3));

        Assertions.assertAll(
                () -> Assertions.assertTrue(collection1.equals(collection2)),
                () -> Assertions.assertFalse(collection1.allFieldsEquals(collection2))
        );
    }

    /**
     * While {@link GeneDiseaseCollection#allFieldsEquals(Object)} should not return {@code false} if
     * {@link GeneDiseaseCollection#equals(Object)}, this test ensures the custom deep equals works correctly for usage
     * in other tests.
     */
    @Test
    public void testAllEqualsWhenGdcHasDifferentPubmedYear() {
        Double score = 0.42;

        GeneDiseaseCollection collection1 = new GeneDiseaseCollection();
        GeneDiseaseCombination collection1Gdc = new GeneDiseaseCombination(genes[0], diseases[0], score);
        collection1Gdc.add(sources[0], new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), 2000));
        collection1.add(collection1Gdc);

        GeneDiseaseCollection collection2 = new GeneDiseaseCollection();
        GeneDiseaseCombination collection2Gdc = new GeneDiseaseCombination(genes[0], diseases[0], score);
        collection2Gdc.add(sources[0], new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), 2020));
        collection2.add(collection2Gdc);

        PubmedEvidence evidenceFromGdc1 = collection1.getByGene(genes[0]).iterator().next().getPubmedEvidenceForSource(sources[0]).iterator().next();
        PubmedEvidence evidenceFromGdc2 = collection2.getByGene(genes[0]).iterator().next().getPubmedEvidenceForSource(sources[0]).iterator().next();

        Assertions.assertAll(
                () -> Assertions.assertTrue(collection1.equals(collection2)),
                () -> Assertions.assertFalse(collection1.allFieldsEquals(collection2)),
                () -> Assertions.assertTrue(evidenceFromGdc1.equals(evidenceFromGdc2)), // URI is identifier, so equals of PubmedEvidence is true.
                () -> Assertions.assertFalse(evidenceFromGdc1.allFieldsEquals(evidenceFromGdc2))
        );
    }
}
