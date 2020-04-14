package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.*;

public class GeneDiseaseCombinationTest {
    private Gene gene = new Gene("ncbigene:0", new GeneSymbol("hgnc:A"));
    private Disease disease = new Disease("umls:C0123456");
    private Gene gene2 = new Gene("ncbigene:1", new GeneSymbol("hgnc:B"));
    private Disease disease2 = new Disease("umls:C1234567");
    private double score = 0.123456789;
    private Source source1 = new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"));
    private Source source2 = new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"));

    @Test
    public void addingMultipleSourcesWithoutEvidence() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
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
        List<URI> source1Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"), URI.create("http://identifiers.org/pubmed/2"));
        List<URI> source2Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/3"), URI.create("http://identifiers.org/pubmed/4"));

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1, source1Evidence.get(1));
        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assertions.assertAll(
                () -> Assertions.assertEquals(Integer.valueOf(2), geneDiseaseCombo.getSourcesCount().get(source1)),
                () -> Assertions.assertEquals(Integer.valueOf(2), geneDiseaseCombo.getSourcesCount().get(source2)),

                () -> Assertions.assertEquals(source1Evidence, geneDiseaseCombo.getEvidenceForSource(source1)),
                () -> Assertions.assertEquals(source2Evidence, geneDiseaseCombo.getEvidenceForSource(source2))
        );
    }

    @Test
    public void addingMultipleSourcesWithAndWithoutEvidence() {
        List<URI> source1Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"));
        List<URI> source2Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
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

                () -> Assertions.assertEquals(source1Evidence, geneDiseaseCombo.getEvidenceForSource(source1)),
                () -> Assertions.assertEquals(source2Evidence, geneDiseaseCombo.getEvidenceForSource(source2))
        );
    }

    @Test
    public void retrieveCountsWhenNothingIsStored() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        Assertions.assertEquals(new HashMap<>(), geneDiseaseCombo.getSourcesCount());
    }

    @Test
    public void retrieveSourcesWithEvidenceWhenNothingIsStored() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        Assertions.assertEquals(new HashSet<>(), geneDiseaseCombo.getSourcesWithEvidence());
    }

    @Test
    public void retrieveCountForNonExistingSource() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        Assertions.assertEquals(0, geneDiseaseCombo.getCountForSource(source1));
    }

    @Test
    public void retrieveEvidenceForNonExistingSource() {
        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        Assertions.assertEquals(null, geneDiseaseCombo.getEvidenceForSource(source1));
    }

    @Test
    public void testGetAllEvidenceOrdered() {
        List<URI> sourceEvidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"),
                URI.create("https://www.ncbi.nlm.nih.gov/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        geneDiseaseCombo.add(source1, sourceEvidence.get(0));
        geneDiseaseCombo.add(source1, sourceEvidence.get(1));
        geneDiseaseCombo.add(source1, sourceEvidence.get(2));

        Assertions.assertEquals(new ArrayList<>(Arrays.asList(sourceEvidence.get(0),sourceEvidence.get(2),sourceEvidence.get(1))),
                geneDiseaseCombo.getAllEvidenceOrdered());
    }

    @Test
    public void testGetAllEvidenceOrderedStrings() {
        List<URI> sourceEvidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"),
                URI.create("https://www.ncbi.nlm.nih.gov/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        geneDiseaseCombo.add(source1, sourceEvidence.get(0));
        geneDiseaseCombo.add(source1, sourceEvidence.get(1));
        geneDiseaseCombo.add(source1, sourceEvidence.get(2));

        Assertions.assertEquals(new ArrayList<>(Arrays.asList(sourceEvidence.get(0).toString(),sourceEvidence.get(2).toString(),sourceEvidence.get(1).toString())),
                geneDiseaseCombo.getAllEvidenceOrderedStrings());
    }

    @Test
    public void testGetAllEvidenceSimplified() {
        List<URI> sourceEvidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"),
                URI.create("https://www.ncbi.nlm.nih.gov/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        geneDiseaseCombo.add(source1, sourceEvidence.get(0));
        geneDiseaseCombo.add(source1, sourceEvidence.get(1));
        geneDiseaseCombo.add(source1, sourceEvidence.get(2));

        Assertions.assertEquals(new HashSet<>(Arrays.asList("1","https://www.ncbi.nlm.nih.gov/pubmed/2", "3")),
                geneDiseaseCombo.getAllEvidenceSimplified());
    }

    @Test
    public void testGetAllEvidenceSimplifiedOrdered() {
        List<URI> sourceEvidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"),
                URI.create("https://www.ncbi.nlm.nih.gov/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        GeneDiseaseCombination geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
        geneDiseaseCombo.add(source1, sourceEvidence.get(0));
        geneDiseaseCombo.add(source1, sourceEvidence.get(1));
        geneDiseaseCombo.add(source1, sourceEvidence.get(2));

        Assertions.assertEquals(new ArrayList<>(Arrays.asList("1","3","https://www.ncbi.nlm.nih.gov/pubmed/2")),
                geneDiseaseCombo.getAllEvidenceSimplifiedOrdered());
    }

    @Test
    public void assertEqualsWhenEqualWithScore() {
        Assertions.assertTrue(new GeneDiseaseCombination(gene, disease, score).equals(new GeneDiseaseCombination(gene, disease, score)));
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
        Assertions.assertTrue(new GeneDiseaseCombination(gene, disease, 0.1).equals(new GeneDiseaseCombination(gene, disease, 0.2)));
    }

    @Test
    public void assertEqualsWhenDifferent() {
        Assertions.assertFalse(new GeneDiseaseCombination(gene, disease).equals(new GeneDiseaseCombination(gene2, disease2)));
    }
}
