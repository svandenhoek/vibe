package org.molgenis.vibe.formats;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GeneDiseaseCollectionTester {
    GeneDiseaseCollection collection;
    GeneDiseaseCombination[] gdcs;

    @BeforeClass
    public void beforeClass() {
        Gene gene1 = new Gene("1111111");
        Gene gene2 = new Gene("2222222");
        Disease disease1 = new Disease("C3333333");
        Disease disease2 = new Disease("C4444444");
        Disease disease3 = new Disease("C5555555");
        gdcs = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(gene1, disease1),
                new GeneDiseaseCombination(gene1, disease2),
                new GeneDiseaseCombination(gene2, disease2),
                new GeneDiseaseCombination(gene2, disease3)
        };
    }

    @BeforeMethod
    public void beforeMethod() {
        collection = new GeneDiseaseCollection();
    }

    @Test
    public void addRetrieveSingleGeneDiseaseAssociation() {
        Set<GeneDiseaseCombination> expectedOutput = new HashSet<>();
        expectedOutput.add(gdcs[0]);

        collection.add(gdcs[0]);
        Assert.assertEquals(collection.get(gdcs[0]), gdcs[0]);
        Assert.assertEquals(collection.getByGene(gdcs[0].getGene()), expectedOutput);
        Assert.assertEquals(collection.getByDisease(gdcs[0].getDisease()), expectedOutput);
    }

    @Test
    public void addMultipleGeneDiseaseAssociationRetrieveSingleGdc() {
        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.get(gdcs[1]), gdcs[1]);
    }

    @Test
    public void addMultipleGeneDiseaseAssociationRetrieveByGene() {
        Set<GeneDiseaseCombination> expectedOutput = new HashSet<>();
        expectedOutput.add(gdcs[2]);
        expectedOutput.add(gdcs[3]);

        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.getByGene(gdcs[2].getGene()), expectedOutput);
    }

    @Test
    public void addMultipleGeneDiseaseAssociationRetrieveByDisease() {
        Set<GeneDiseaseCombination> expectedOutput = new HashSet<>();
        expectedOutput.add(gdcs[1]);
        expectedOutput.add(gdcs[2]);

        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.getByDisease(gdcs[1].getDisease()), expectedOutput);
    }

    //TODO: More tests for basic java.util.Collection functionalities!
}
