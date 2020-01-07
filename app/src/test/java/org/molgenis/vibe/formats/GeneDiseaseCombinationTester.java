package org.molgenis.vibe.formats;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

public class GeneDiseaseCombinationTester {
    private Gene gene = new Gene("ncbigene:0", new GeneSymbol("hgnc:A"));
    private Disease disease = new Disease("umls:C01234567");
    private double score = 0.123456789;
    private Source source1 = new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"));
    private Source source2 = new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"));

    private GeneDiseaseCombination geneDiseaseCombo;

    @BeforeMethod
    public void beforeMethod() {
        geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
    }

    @Test
    public void addingMultipleSourcesWithoutEvidence() {
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1);

        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source1), Integer.valueOf(3));
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source2), Integer.valueOf(1));
    }

    @Test
    public void addingMultipleSourcesWithEvidence() {
        List<URI> source1Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"), URI.create("http://identifiers.org/pubmed/2"));
        List<URI> source2Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/3"), URI.create("http://identifiers.org/pubmed/4"));

        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1, source1Evidence.get(1));
        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source1), Integer.valueOf(2));
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source2), Integer.valueOf(2));

        Assert.assertEquals(geneDiseaseCombo.getEvidenceForSource(source1), source1Evidence);
        Assert.assertEquals(geneDiseaseCombo.getEvidenceForSource(source2), source2Evidence);
    }

    @Test
    public void addingMultipleSourcesWithAndWithoutEvidence() {
        List<URI> source1Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"));
        List<URI> source2Evidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source1), Integer.valueOf(3));
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source2), Integer.valueOf(4));

        Assert.assertEquals(geneDiseaseCombo.getEvidenceForSource(source1), source1Evidence);
        Assert.assertEquals(geneDiseaseCombo.getEvidenceForSource(source2), source2Evidence);
    }

    @Test
    public void retrieveCountsWhenNothingIsStored() {
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount(), new HashMap<>());
    }

    @Test
    public void retrieveSourcesWithEvidenceWhenNothingIsStored() {
        Assert.assertEquals(geneDiseaseCombo.getSourcesWithEvidence(), new HashSet<>());
    }

    @Test
    public void retrieveCountForNonExistingSource() {
        Assert.assertEquals(geneDiseaseCombo.getCountForSource(source1), 0);
    }

    @Test
    public void retrieveEvidenceForNonExistingSource() {
        Assert.assertEquals(geneDiseaseCombo.getEvidenceForSource(source1), null);
    }

    @Test
    public void testGetAllEvidenceSimplified() {
        List<URI> sourceEvidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"),
                URI.create("https://www.ncbi.nlm.nih.gov/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        geneDiseaseCombo.add(source1, sourceEvidence.get(0));
        geneDiseaseCombo.add(source1, sourceEvidence.get(1));
        geneDiseaseCombo.add(source1, sourceEvidence.get(2));

        Assert.assertEquals(geneDiseaseCombo.getAllEvidenceSimplified(), new HashSet<>(Arrays.asList("1","https://www.ncbi.nlm.nih.gov/pubmed/2", "3")));
    }

    @Test
    public void testGetAllEvidenceSimplifiedOrdered() {
        List<URI> sourceEvidence = Arrays.asList(URI.create("http://identifiers.org/pubmed/1"),
                URI.create("https://www.ncbi.nlm.nih.gov/pubmed/2"), URI.create("http://identifiers.org/pubmed/3"));

        geneDiseaseCombo.add(source1, sourceEvidence.get(0));
        geneDiseaseCombo.add(source1, sourceEvidence.get(1));
        geneDiseaseCombo.add(source1, sourceEvidence.get(2));

        Assert.assertEquals(geneDiseaseCombo.getAllEvidenceSimplifiedOrdered(), new ArrayList<>(Arrays.asList("1","3","https://www.ncbi.nlm.nih.gov/pubmed/2")));
    }
}
