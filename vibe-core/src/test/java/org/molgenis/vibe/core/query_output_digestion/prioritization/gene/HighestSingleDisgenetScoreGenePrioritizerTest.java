package org.molgenis.vibe.core.query_output_digestion.prioritization.gene;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.formats.*;

import java.util.*;

class HighestSingleDisgenetScoreGenePrioritizerTest {

    @Test
    void testOrdering() {
        Gene[] genes = new Gene[]{
                new Gene("ncbigene:1", new GeneSymbol("hgnc:A")),
                new Gene("ncbigene:2", new GeneSymbol("hgnc:B")),
                new Gene("ncbigene:3", new GeneSymbol("hgnc:C"))
        };

        Disease[] diseases = new Disease[]{
                new Disease("umls:C0000001"),
                new Disease("umls:C0000002"),
                new Disease("umls:C0000003"),
                new Disease("umls:C0000004"),
                new Disease("umls:C0000005"),
                new Disease("umls:C0000006")
        };

        Set<GeneDiseaseCombination> geneDiseaseCombinations = new HashSet<>( Arrays.asList(
                new GeneDiseaseCombination(genes[0], diseases[0], 0.4),
                new GeneDiseaseCombination(genes[0], diseases[1], 0.3),
                new GeneDiseaseCombination(genes[1], diseases[2], 0.6),
                new GeneDiseaseCombination(genes[1], diseases[3], 0.3),
                new GeneDiseaseCombination(genes[2], diseases[4], 0.4),
                new GeneDiseaseCombination(genes[2], diseases[5], 0.5)
        ));

        List<Gene> expectedPriority = new ArrayList<>( Arrays.asList(
                genes[1], // highest is 0.6
                genes[2], // highest is 0.5
                genes[0] // highest is 0.4
        ));

        GenePrioritizer prioritizer = new HighestSingleDisgenetScoreGenePrioritizer();
        List<Gene> actualPriority = prioritizer.sort(new GeneDiseaseCollection(geneDiseaseCombinations));
        Assertions.assertEquals(expectedPriority, actualPriority);
    }
}
