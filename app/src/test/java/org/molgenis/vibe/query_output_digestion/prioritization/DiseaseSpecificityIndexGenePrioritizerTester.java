package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.Gene;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

public class DiseaseSpecificityIndexGenePrioritizerTester {
    @Test
    public void testOrdering() {
        List<Gene> genes = new ArrayList<>( Arrays.asList(
                new Gene(URI.create("http://identifiers.org/ncbigene/1"), 0.5, 0.1),
                new Gene(URI.create("http://identifiers.org/ncbigene/2"), 0.8, 0.2),
                new Gene(URI.create("http://identifiers.org/ncbigene/3"), 0.2, 0.3)
        ));

        List<Gene> expectedPriority = new ArrayList<>( Arrays.asList(
                genes.get(1), // 0.8 first
                genes.get(0), // 0.5 second
                genes.get(2) // 0.2 third
        ));

        GenePrioritizer prioritizer = new DiseaseSpecificityIndexGenePrioritizer(genes);
        prioritizer.run();
        Assert.assertEquals(prioritizer.getPriority(), expectedPriority);
    }
}
