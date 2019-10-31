package org.molgenis.vibe.formats;

import java.util.List;
import java.util.Set;

/**
 * A collection of {@link GeneDiseaseCombination}{@code s}.
 */
public class GeneDiseaseCollection extends BiologicalEntityCollection<Gene, Disease, GeneDiseaseCombination> {

    /**
     * @return all {@link Gene}{@code s}.
     * @see #getT1()
     */
    public Set<Gene> getGenes() {
        return getT1();
    }

    /**
     * @return all {@link Disease}{@code s}.
     * @see #getT2()
     */
    public Set<Disease> getDiseases() {
        return getT2();
    }

    /**
     * @return all {@link GeneDiseaseCombination}{@code s}.
     * @see #getT3()
     */
    public Set<GeneDiseaseCombination> getGeneDiseaseCombinations() {
        return getT3();
    }

    /**
     *
     * @return all {@link GeneDiseaseCombination}{@code s}.
     * @see #getT3Ordered()
     */
    public List<GeneDiseaseCombination> getGeneDiseaseCombinationsOrdered() {
        return getT3Ordered();
    }

    /**
     * Get the {@link GeneDiseaseCombination}{@code s} for a single {@link Gene}.
     * @param gene the {@link Gene} to retrieve {@link GeneDiseaseCombination}{@code s} for
     * @return the {@link GeneDiseaseCombination}{@code s} belonging to {@code gene}
     * @see #getByT1(BiologicalEntity)
     */
    public Set<GeneDiseaseCombination> getByGene(Gene gene) {
        return getByT1(gene);
    }

    /**
     * Get the {@link GeneDiseaseCombination}{@code s} for a single {@link Disease}.
     * @param disease the {@link Disease} to retrieve {@link GeneDiseaseCombination}{@code s} for
     * @return the {@link GeneDiseaseCombination}{@code s} belonging to {@code disease}
     * @see #getByT2(BiologicalEntity)
     */
    public Set<GeneDiseaseCombination> getByDisease(Disease disease) {
        return getByT2(disease);
    }

    public GeneDiseaseCollection() {
    }

    public GeneDiseaseCollection(Set<GeneDiseaseCombination> combinationsMap) {
        super(combinationsMap);
    }
}
