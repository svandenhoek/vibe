package org.molgenis.vibe.formats;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Currently unsupported.
 */
public class PhenotypeDiseaseCombination extends BiologicalEntityCombination<Phenotype, Disease> {
    public PhenotypeDiseaseCombination(Phenotype phenotype, Disease disease) {
        super(phenotype, disease);
        throw new NotImplementedException("placeholder class");
    }
}
