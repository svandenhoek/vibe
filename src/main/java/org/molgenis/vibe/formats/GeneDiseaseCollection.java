package org.molgenis.vibe.formats;

import java.util.Set;

public class GeneDiseaseCollection extends BiologicalEntityCollection<Gene, Disease, GeneDiseaseCombination> {

    public Set<GeneDiseaseCombination> getByGene(Gene gene) {
        return getByT1(gene);
    }

    public Set<GeneDiseaseCombination> getByDisease(Disease disease) {
        return getByT2(disease);
    }
}
