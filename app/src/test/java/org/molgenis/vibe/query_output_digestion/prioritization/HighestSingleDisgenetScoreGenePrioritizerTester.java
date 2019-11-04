package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.GenePrioritizer;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.HighestSingleDisgenetScoreGenePrioritizer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class HighestSingleDisgenetScoreGenePrioritizerTester {

    @Test
    public void testOrdering() {
        Gene[] genes = new Gene[]{
                new Gene("ncbigene:1"),
                new Gene("ncbigene:2"),
                new Gene("ncbigene:3")
        };

        Disease[] diseases = new Disease[]{
                new Disease("umls:C1"),
                new Disease("umls:C2"),
                new Disease("umls:C3"),
                new Disease("umls:C4"),
                new Disease("umls:C5"),
                new Disease("umls:C6")
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

        GenePrioritizer prioritizer = new HighestSingleDisgenetScoreGenePrioritizer(new GeneDiseaseCollection(geneDiseaseCombinations));
        prioritizer.run();
        Assert.assertEquals(prioritizer.getPriority(), expectedPriority);
    }
}
