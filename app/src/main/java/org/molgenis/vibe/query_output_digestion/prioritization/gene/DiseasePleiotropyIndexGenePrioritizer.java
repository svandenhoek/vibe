package org.molgenis.vibe.query_output_digestion.prioritization.gene;

import org.molgenis.vibe.formats.Gene;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class DiseasePleiotropyIndexGenePrioritizer extends GenePrioritizer {
    public DiseasePleiotropyIndexGenePrioritizer(List<Gene> data) {
        super(data);
    }

    public DiseasePleiotropyIndexGenePrioritizer(Set<Gene> data) {
        super(data);
    }

    @Override
    public void run() {
        // Sorts the genes with lowest disease pleiotropy index first.
        getPriority().sort(Comparator.comparingDouble(Gene::getDiseasePleiotropyIndex));
    }
}
