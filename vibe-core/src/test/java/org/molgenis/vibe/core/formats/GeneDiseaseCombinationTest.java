package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.*;

public class GeneDiseaseCombinationTest {
    private Gene gene = new Gene("ncbigene:0", new GeneSymbol("hgnc:A"));
    private Disease disease = new Disease("umls:C0123456");
    private Gene gene2 = new Gene("ncbigene:1", new GeneSymbol("hgnc:B"));
    private Disease disease2 = new Disease("umls:C1234567");
    private double score1 = 0.42;
    private double score2 = 0.24;
    private Source source1 = new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"), "Orphanet", Source.Level.CURATED);
    private Source source2 = new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"), "Befree", Source.Level.LITERATURE);
    private int year1 = 2000;
    private int year2 = 2020;

    @Test
    public void addingMultipleSourcesWithoutEvidence() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1);

        Assertions.assertAll(
                () -> Assertions.assertEquals(Integer.valueOf(3), geneDiseaseCombo.getSourcesCount().get(source1)),
                () -> Assertions.assertEquals(Integer.valueOf(1), geneDiseaseCombo.getSourcesCount().get(source2))
        );
    }

    @Test
    public void addingMultipleSourcesWithEvidence() {
        List<PubmedEvidence> source1Evidence = Arrays.asList(
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1),
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/2"), year1)
        );
        List<PubmedEvidence> source2Evidence = Arrays.asList(
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/3"), year1),
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/4"), year1)
        );

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1, source1Evidence.get(1));
        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assertions.assertAll(
                () -> Assertions.assertEquals(Integer.valueOf(2), geneDiseaseCombo.getSourcesCount().get(source1)),
                () -> Assertions.assertEquals(Integer.valueOf(2), geneDiseaseCombo.getSourcesCount().get(source2)),

                () -> Assertions.assertEquals(new HashSet<>(source1Evidence), geneDiseaseCombo.getPubmedEvidenceForSource(source1)),
                () -> Assertions.assertEquals(new HashSet<>(source2Evidence), geneDiseaseCombo.getPubmedEvidenceForSource(source2))
        );
    }

    @Test
    public void addingMultipleSourcesWithAndWithoutEvidence() {
        List<PubmedEvidence> source1Evidence = Arrays.asList(
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1)
        );
        List<PubmedEvidence> source2Evidence = Arrays.asList(
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/2"), year1),
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/3"), year1)
        );

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assertions.assertAll(
                () -> Assertions.assertEquals(Integer.valueOf(3), geneDiseaseCombo.getSourcesCount().get(source1)),
                () -> Assertions.assertEquals(Integer.valueOf(4), geneDiseaseCombo.getSourcesCount().get(source2)),

                () -> Assertions.assertEquals(new HashSet<>(source1Evidence), geneDiseaseCombo.getPubmedEvidenceForSource(source1)),
                () -> Assertions.assertEquals(new HashSet<>(source2Evidence), geneDiseaseCombo.getPubmedEvidenceForSource(source2))
        );
    }

    @Test
    public void retrieveCountsWhenNothingIsStored() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        Assertions.assertEquals(new HashMap<>(), geneDiseaseCombo.getSourcesCount());
    }

    @Test
    public void retrieveSourcesWithPubmedEvidenceWhenNothingIsStored() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        Assertions.assertEquals(new HashSet<>(), geneDiseaseCombo.getSourcesWithPubmedEvidence());
    }

    @Test
    public void retrieveCountForNonExistingSource() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        Assertions.assertEquals(0, geneDiseaseCombo.getCountForSource(source1));
    }

    @Test
    public void retrievePubmedEvidenceForNonExistingSource() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        Assertions.assertEquals(null, geneDiseaseCombo.getPubmedEvidenceForSource(source1));
    }

    @Test
    public void assertEqualsWhenEqualWithScore() {
        Assertions.assertTrue(new GeneDiseaseCombination(gene, disease, score1).equals(new GeneDiseaseCombination(gene, disease, score1)));
    }

    @Test
    public void assertEqualsWhenEqualWithoutScore() {
        Assertions.assertTrue(new GeneDiseaseCombination(gene, disease).equals(new GeneDiseaseCombination(gene, disease)));
    }

    /**
     * Equals only checks whether it is the same gene-disease combo.
     */
    @Test
    public void assertEqualsWhenEqualWithDifferentScore() {
        Assertions.assertTrue(new GeneDiseaseCombination(gene, disease, score1).equals(new GeneDiseaseCombination(gene, disease, score2)));
    }

    @Test
    public void assertEqualsWhenGeneDifferent() {
        Assertions.assertFalse(new GeneDiseaseCombination(gene, disease).equals(new GeneDiseaseCombination(gene2, disease)));
    }

    @Test
    public void assertEqualsWhenDiseaseDifferent() {
        Assertions.assertFalse(new GeneDiseaseCombination(gene, disease).equals(new GeneDiseaseCombination(gene, disease2)));
    }

    @Test
    public void assertEqualsWhenBothDifferent() {
        Assertions.assertFalse(new GeneDiseaseCombination(gene, disease).equals(new GeneDiseaseCombination(gene2, disease2)));
    }

    /**
     * While {@link GeneDiseaseCombination#allFieldsEquals(Object)} should not return {@code false} if
     * {@link GeneDiseaseCombination#equals(Object)}, this test ensures the custom deep equals works correctly for usage
     * in other tests.
     */
    @Test
    public void testAllEqualsWhenPubmedIdDiffers() {
        GeneDiseaseCombination geneDiseaseCombo1 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo1.add(source1, new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1));

        GeneDiseaseCombination geneDiseaseCombo2 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo2.add(source1, new PubmedEvidence(URI.create("http://identifiers.org/pubmed/2"), year1));


        PubmedEvidence evidenceFromGdc1 = geneDiseaseCombo1.getPubmedEvidenceForSource(source1).iterator().next();
        PubmedEvidence evidenceFromGdc2 = geneDiseaseCombo2.getPubmedEvidenceForSource(source1).iterator().next();

        Assertions.assertAll(
                () -> Assertions.assertTrue(geneDiseaseCombo1.equals(geneDiseaseCombo2)),
                () -> Assertions.assertFalse(geneDiseaseCombo1.allFieldsEquals(geneDiseaseCombo2)),
                () -> Assertions.assertFalse(evidenceFromGdc1.equals(evidenceFromGdc2)), // URI is identifier, so equals of PubmedEvidence is false.
                () -> Assertions.assertFalse(evidenceFromGdc1.allFieldsEquals(evidenceFromGdc2))
        );
    }

    /**
     * While {@link GeneDiseaseCombination#allFieldsEquals(Object)} should not return {@code false} if
     * {@link GeneDiseaseCombination#equals(Object)}, this test ensures the custom deep equals works correctly for usage
     * in other tests.
     */
    @Test
    public void testAllEqualsWhenPubmedYearDiffers() {
        GeneDiseaseCombination geneDiseaseCombo1 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo1.add(source1, new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1));

        GeneDiseaseCombination geneDiseaseCombo2 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo2.add(source1, new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year2));

        PubmedEvidence evidenceFromGdc1 = geneDiseaseCombo1.getPubmedEvidenceForSource(source1).iterator().next();
        PubmedEvidence evidenceFromGdc2 = geneDiseaseCombo2.getPubmedEvidenceForSource(source1).iterator().next();

        Assertions.assertAll(
                () -> Assertions.assertTrue(geneDiseaseCombo1.equals(geneDiseaseCombo2)),
                () -> Assertions.assertFalse(geneDiseaseCombo1.allFieldsEquals(geneDiseaseCombo2)),
                () -> Assertions.assertTrue(evidenceFromGdc1.equals(evidenceFromGdc2)), // URI is identifier, so equals of PubmedEvidence is true.
                () -> Assertions.assertFalse(evidenceFromGdc1.allFieldsEquals(evidenceFromGdc2))
        );
    }

    /**
     * While {@link GeneDiseaseCombination#allFieldsEquals(Object)} should not return {@code false} if
     * {@link GeneDiseaseCombination#equals(Object)}, this test ensures the custom deep equals works correctly for usage
     * in other tests.
     */
    @Test
    public void testAllEqualsWhenScoreDiffers() {
        GeneDiseaseCombination geneDiseaseCombo1 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo1.add(source1, new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1));

        GeneDiseaseCombination geneDiseaseCombo2 = new GeneDiseaseCombination(gene, disease, score2);
        geneDiseaseCombo2.add(source1, new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1));

        Assertions.assertAll(
                () -> Assertions.assertTrue(geneDiseaseCombo1.equals(geneDiseaseCombo2)),
                () -> Assertions.assertFalse(geneDiseaseCombo1.allFieldsEquals(geneDiseaseCombo2))
        );
    }

    /**
     * While {@link GeneDiseaseCombination#allFieldsEquals(Object)} should not return {@code false} if
     * {@link GeneDiseaseCombination#equals(Object)}, this test ensures the custom deep equals works correctly for usage
     * in other tests.
     */
    @Test
    public void testAllEqualsWhenSourceCountDiffers() {
        GeneDiseaseCombination geneDiseaseCombo1 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo1.add(source1);

        GeneDiseaseCombination geneDiseaseCombo2 = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo2.add(source1);
        geneDiseaseCombo2.add(source1);

        Assertions.assertAll(
            () -> Assertions.assertTrue(geneDiseaseCombo1.equals(geneDiseaseCombo2)),
            () -> Assertions.assertFalse(geneDiseaseCombo1.allFieldsEquals(geneDiseaseCombo2))
        );
    }

    @Test
    public void testRetrievalYearOrderedPubmedEvidenceForSource() {
        List<PubmedEvidence> expectedList = new ArrayList<>();
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/3"), year2)); // most recent first
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1)); // same year -> lowest number first
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/2"), year1));

        // Adding order should not matter as stored using Set.
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo.add(source1, expectedList.get(1));
        geneDiseaseCombo.add(source1, expectedList.get(2));
        geneDiseaseCombo.add(source1, expectedList.get(0));

        Assertions.assertEquals(geneDiseaseCombo.getPubmedEvidenceForSourceSortedByReleaseDate(source1),
                expectedList);
    }

    @Test
    public void testRetrievalYearOrderedPubmedEvidence() {
        List<PubmedEvidence> expectedList = new ArrayList<>();
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/4"), year2)); // most recent first
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), year1)); // same year -> lowest number first
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/2"), year1));
        expectedList.add(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/3"), year1));

        // Adding order should not matter as stored using Set.
        // Source should not matter when retrieving all results.
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score1);
        geneDiseaseCombo.add(source1, expectedList.get(3));
        geneDiseaseCombo.add(source2, expectedList.get(1));
        geneDiseaseCombo.add(source2, expectedList.get(2));
        geneDiseaseCombo.add(source1, expectedList.get(0));

        Assertions.assertEquals(geneDiseaseCombo.getAllPubMedEvidenceSortedByYear(),
                expectedList);
    }
}
