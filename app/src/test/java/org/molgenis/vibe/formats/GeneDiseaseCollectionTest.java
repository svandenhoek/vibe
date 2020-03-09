package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GeneDiseaseCollectionTest {
    private static GeneDiseaseCombination[] gdcs;
    private static Gene[] genes;
    private static Disease[] diseases;

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
}
