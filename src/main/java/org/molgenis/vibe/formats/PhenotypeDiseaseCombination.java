package org.molgenis.vibe.formats;

/**
 * Currently unsupported.
 */
public class PhenotypeDiseaseCombination extends BiologicalEntityCombination<Phenotype, Disease> {
    public PhenotypeDiseaseCombination(Phenotype phenotype, Disease disease) {
        super(phenotype, disease);
    }
}
