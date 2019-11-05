package org.molgenis.vibe.formats;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

public class GeneDiseaseCollectionTester {
    GeneDiseaseCollection collection;
    GeneDiseaseCombination[] gdcs;
    Gene[] genes;
    Disease[] diseases;

    @BeforeClass
    public void beforeClass() {
        genes = new Gene[]{
                new Gene("ncbigene:1111111"),
                new Gene("ncbigene:2222222")
        };

        diseases = new Disease[]{
                new Disease("umls:C3333333"),
                new Disease("umls:C4444444"),
                new Disease("umls:C5555555")
        };

        gdcs = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0]),
                new GeneDiseaseCombination(genes[0], diseases[1]),
                new GeneDiseaseCombination(genes[1], diseases[1]),
                new GeneDiseaseCombination(genes[1], diseases[2])
        };
    }

    @BeforeMethod
    public void beforeMethod() {
        collection = new GeneDiseaseCollection();
    }

    @Test
    public void addRetrieveSingleGeneDiseaseAssociation() {
        Set<GeneDiseaseCombination> expectedOutput = new HashSet<>(Arrays.asList(gdcs[0]));

        collection.add(gdcs[0]);
        Assert.assertEquals(collection.get(gdcs[0]), gdcs[0]);
        Assert.assertEquals(collection.getByGene(genes[0]), expectedOutput);
        Assert.assertEquals(collection.getByDisease(diseases[0]), expectedOutput);
    }

    @Test
    public void addMultipleGeneDiseaseAssociationRetrieveSingleGdc() {
        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.get(gdcs[1]), gdcs[1]);
    }

    @Test
    public void addMultipleGeneDiseaseAssociationRetrieveByGene() {
        Set<GeneDiseaseCombination> expectedOutput = new HashSet<>(Arrays.asList(gdcs[2],gdcs[3]));

        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.getByGene(genes[1]), expectedOutput);
    }

    @Test
    public void addMultipleGeneDiseaseAssociationRetrieveByDisease() {
        Set<GeneDiseaseCombination> expectedOutput = new HashSet<>(Arrays.asList(gdcs[1],gdcs[2]));

        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.getByDisease(diseases[1]), expectedOutput);
    }

    @Test
    public void testOrderOfOrderedGeneDiseaseAssociation() {
        // Made explicit to make order clear (though technically not needed as order is already correct).
        List<GeneDiseaseCombination> expectedOutput = new ArrayList<>(Arrays.asList(gdcs[0],gdcs[1],gdcs[2],gdcs[3]));

        collection.addAll(Arrays.asList(gdcs));
        Assert.assertEquals(collection.getGeneDiseaseCombinationsOrdered(), expectedOutput);
    }

    @Test
    public void retrieveByNonExistingGene() {
        collection.add(gdcs[0]);
        Assert.assertEquals(collection.getByGene(genes[1]), null);
    }

    @Test
    public void retrieveByNonExistingDisease() {
        collection.add(gdcs[0]);
        Assert.assertEquals(collection.getByDisease(diseases[1]), null);
    }

    //TODO: More tests for basic java.util.Collection functionalities!

    @Test
    public void testRemoveWithoutTriggeringRemovingEmptySet() {
        collection.addAll(Arrays.asList(gdcs));
        collection.remove(gdcs[2]);

        // Validate full collection.
        Assert.assertEquals(collection, new HashSet<>(Arrays.asList(gdcs[0],gdcs[1],gdcs[3])));

        // Validate if grouped by gene is correct.
        Assert.assertEquals(collection.getByGene(genes[0]), new HashSet<>(Arrays.asList(gdcs[0],gdcs[1])));
        Assert.assertEquals(collection.getByGene(genes[1]), new HashSet<>(Arrays.asList(gdcs[3])));

        // Validate if grouped by disease is correct.
        Assert.assertEquals(collection.getByDisease(diseases[0]), new HashSet<>(Arrays.asList(gdcs[0])));
        Assert.assertEquals(collection.getByDisease(diseases[1]), new HashSet<>(Arrays.asList(gdcs[1])));
        Assert.assertEquals(collection.getByDisease(diseases[2]), new HashSet<>(Arrays.asList(gdcs[3])));
    }

    @Test
    public void testRemoveWithTriggeringRemovingEmptySet() {
        collection.addAll(Arrays.asList(gdcs));
        collection.remove(gdcs[0]);

        // Validate full collection.
        Assert.assertEquals(collection, new HashSet<>(Arrays.asList(gdcs[1],gdcs[2],gdcs[3])));

        // Validate if grouped by gene is correct.
        Assert.assertEquals(collection.getByGene(genes[0]), new HashSet<>(Arrays.asList(gdcs[1])));
        Assert.assertEquals(collection.getByGene(genes[1]), new HashSet<>(Arrays.asList(gdcs[2],gdcs[3])));

        // Validate if grouped by disease is correct.
        Assert.assertEquals(collection.getByDisease(diseases[0]), null);
        Assert.assertEquals(collection.getByDisease(diseases[1]), new HashSet<>(Arrays.asList(gdcs[1],gdcs[2])));
        Assert.assertEquals(collection.getByDisease(diseases[2]), new HashSet<>(Arrays.asList(gdcs[3])));
    }

    @Test
    public void testRemoveAll() {
        collection.addAll(Arrays.asList(gdcs));
        collection.removeAll(Arrays.asList(gdcs[0],gdcs[1]));

        // Validate if full collection is correct.
        Assert.assertEquals(collection, new HashSet<>(Arrays.asList(gdcs[2],gdcs[3])));

        // Validate if grouped by gene is correct.
        Assert.assertEquals(collection.getByGene(genes[0]), null);
        Assert.assertEquals(collection.getByGene(genes[1]), new HashSet<>(Arrays.asList(gdcs[2],gdcs[3])));

        // Validate if grouped by disease is correct.
        Assert.assertEquals(collection.getByDisease(diseases[0]), null);
        Assert.assertEquals(collection.getByDisease(diseases[1]), new HashSet<>(Arrays.asList(gdcs[2])));
        Assert.assertEquals(collection.getByDisease(diseases[2]), new HashSet<>(Arrays.asList(gdcs[3])));
    }
}
