package org.molgenis.vibe.formats;

/**
 * Currently unsupported.
 */
public class PhenotypeDiseaseCombination extends BiologicalEntityCombination<Hpo, Disease> {
    public PhenotypeDiseaseCombination(Hpo hpo, Disease disease) {
        super(hpo, disease);
    }
}
