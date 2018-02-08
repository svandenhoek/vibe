package org.molgenis.vibe.formats;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class GeneDiseaseCombinationTester {
    private Gene gene = new Gene();
    private Disease disease = new Disease("http://disease.test", "myDisease") {

    };
    private double score = 0.123456789;
    private Source source1 = new Source("ORPHANET", Source.Level.CURATED);
    private Source source2 = new Source("BEFREE", Source.Level.LITERATURE);

    private GeneDiseaseCombination geneDiseaseCombo;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        geneDiseaseCombo = new GeneDiseaseCombination(gene, disease, score);
    }

    @Test
    public void addingMultipleSourcesWithoutEvidence() {
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1);

        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source1), new Integer(3));
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source2), new Integer(1));
    }

    @Test
    public void addingMultipleSourcesWithEvidence() {
        List<String> source1Evidence = Arrays.asList("pubmedId1", "pubmedId2");
        List<String> source2Evidence = Arrays.asList("pubmedId3", "pubmedId4");

        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1, source1Evidence.get(1));
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source1), new Integer(2));
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source2), new Integer(2));

        Assert.assertEquals(geneDiseaseCombo.getSourcesEvidence().get(source1), source1Evidence);
        Assert.assertEquals(geneDiseaseCombo.getSourcesEvidence().get(source2), source2Evidence);
    }

    @Test
    public void addingMultipleSourcesWithAndWithoutEvidence() {
        List<String> source1Evidence = Arrays.asList("pubmedId1");
        List<String> source2Evidence = Arrays.asList("pubmedId2", "pubmedId3");

        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2, source2Evidence.get(0));
        geneDiseaseCombo.add(source2);
        geneDiseaseCombo.add(source1, source1Evidence.get(0));
        geneDiseaseCombo.add(source1);
        geneDiseaseCombo.add(source2, source2Evidence.get(1));

        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source1), new Integer(3));
        Assert.assertEquals(geneDiseaseCombo.getSourcesCount().get(source2), new Integer(4));

        Assert.assertEquals(geneDiseaseCombo.getSourcesEvidence().get(source1), source1Evidence);
        Assert.assertEquals(geneDiseaseCombo.getSourcesEvidence().get(source2), source2Evidence);
    }
}
