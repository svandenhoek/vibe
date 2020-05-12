package org.molgenis.vibe.core.query_output_digestion.prioritization.gene;

import org.molgenis.vibe.core.formats.EnumTypeDefiner;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;

public enum GenePrioritizerFactory implements EnumTypeDefiner{
    HIGHEST_DISGENET_SCORE("gda_max") {
        @Override
        public GenePrioritizer create(GeneDiseaseCollection geneDiseaseCollection) {
             return new HighestSingleDisgenetScoreGenePrioritizer(geneDiseaseCollection);
        }
    };

    private String id;

    @Override
    public String getId() {
        return id;
    }

    GenePrioritizerFactory(String id) {
        this.id = id;
    }

    public abstract GenePrioritizer create(GeneDiseaseCollection geneDiseaseCollection);

    public static GenePrioritizerFactory retrieve(String name) {
        return EnumTypeDefiner.retrieve(name, GenePrioritizerFactory.class);
    }
}
