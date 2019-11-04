package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.DiseasePleiotropyIndexGenePrioritizer;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.GenePrioritizer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiseasePleiotropyIndexGenePrioritizerTester {
    @Test
    public void testOrdering() {
        List<Gene> genes = new ArrayList<>( Arrays.asList(
                new Gene(URI.create("http://identifiers.org/ncbigene/1"), 0.3, 0.3),
                new Gene(URI.create("http://identifiers.org/ncbigene/2"), 0.2, 0.9),
                new Gene(URI.create("http://identifiers.org/ncbigene/3"), 0.1, 0.5)
        ));

        List<Gene> expectedPriority = new ArrayList<>( Arrays.asList(
                genes.get(0), // 0.3 first
                genes.get(2), // 0.5 second
                genes.get(1) // 0.9 third
        ));

        GenePrioritizer prioritizer = new DiseasePleiotropyIndexGenePrioritizer(genes);
        prioritizer.run();
        Assert.assertEquals(prioritizer.getPriority(), expectedPriority);
    }
}
