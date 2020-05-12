package org.molgenis.vibe.core.formats;

import java.util.ArrayList;
import java.util.Comparator;
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
     * @see super#getByT1(BiologicalEntity)
     */
    public Set<GeneDiseaseCombination> getByGene(Gene gene) {
        return getByT1(gene);
    }

    /**
     * Get the {@link GeneDiseaseCombination}{@code s} for a single {@link Disease}.
     * @param disease the {@link Disease} to retrieve {@link GeneDiseaseCombination}{@code s} for
     * @return the {@link GeneDiseaseCombination}{@code s} belonging to {@code disease}
     * @see super#getByT2(BiologicalEntity)
     */
    public Set<GeneDiseaseCombination> getByDisease(Disease disease) {
        return getByT2(disease);
    }

    public GeneDiseaseCollection() {
    }

    public GeneDiseaseCollection(Set<GeneDiseaseCombination> combinationsMap) {
        super(combinationsMap);
    }

    /**
     * Wrapper for {@link #getByGene(Gene)} that returns an ordered {@link List} based on
     * {@link GeneDiseaseCombination#getDisgenetScore()} (high->low) instead of a {@link Set}.
     * @param gene
     * @return {@link #getByDisease(Disease)} as ordered {@link List} based on
     * {@link GeneDiseaseCombination#getDisgenetScore()} (high->low)
     * @see #getByGene(Gene)
     */
    public List<GeneDiseaseCombination> getByGeneOrderedByGdaScore(Gene gene) {
        return getByBiologicalEntityOrderedByGdaScore(getByGene(gene));
    }

    /**
     * Wrapper for {@link #getByDisease(Disease)} that returns an ordered {@link List} based on
     * {@link GeneDiseaseCombination#getDisgenetScore()} (high->low) instead of a {@link Set}.
     * @param disease
     * @return {@link #getByDisease(Disease)} as ordered {@link List} based on
     * {@link GeneDiseaseCombination#getDisgenetScore()} (high->low)
     * @see #getByDisease(Disease)
     */
    public List<GeneDiseaseCombination> getByDiseaseOrderedByGdaScore(Disease disease) {
        return getByBiologicalEntityOrderedByGdaScore(getByDisease(disease));
    }

    /**
     * Creates an ordered {@link List} based on {@link GeneDiseaseCombination#getDisgenetScore()} using the data from the
     * given {@link Set} containing {@link GeneDiseaseCombination}{@code s}.
     * @param geneDiseaseCombinationSet the {@link Set} to be turned into a ordered {@link List}
     * @return an ordered {@link List} containing {@link GeneDiseaseCombination}{@code s}
     */
    private List<GeneDiseaseCombination> getByBiologicalEntityOrderedByGdaScore(Set<GeneDiseaseCombination> geneDiseaseCombinationSet) {
        List<GeneDiseaseCombination> orderedList = new ArrayList<>();
        orderedList.addAll(geneDiseaseCombinationSet);
        orderedList.sort(Comparator.comparing(GeneDiseaseCombination::getDisgenetScore).reversed());

        return orderedList;
    }
}
