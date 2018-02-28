package org.molgenis.vibe.formats;

import java.util.Set;

public class GeneDiseaseCollection extends BiologicalEntityCollection<Gene, Disease, GeneDiseaseCombination> {

    public Set<Gene> getGenes() {
        return getT1();
    }

    public Set<Disease> getDiseases() {
        return getT2();
    }

    public Set<GeneDiseaseCombination> getGeneDiseaseCombinations() {
        return getT3();
    }

    public Set<GeneDiseaseCombination> getByGene(Gene gene) {
        return getByT1(gene);
    }

    public Set<GeneDiseaseCombination> getByDisease(Disease disease) {
        return getByT2(disease);
    }
}
