package org.molgenis.vibe.query_output_digestion.prioritization.gene;

import org.molgenis.vibe.formats.Gene;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class DiseaseSpecificityIndexGenePrioritizer extends GenePrioritizer {
    public DiseaseSpecificityIndexGenePrioritizer(List<Gene> data) {
        super(data);
    }

    public DiseaseSpecificityIndexGenePrioritizer(Set<Gene> data) {
        super(data);
    }

    @Override
    public void run() {
        // Sorts the genes with highest disease specificity index first.
        getPriority().sort(Comparator.comparingDouble(Gene::getDiseaseSpecificityIndex).reversed());
    }
}
