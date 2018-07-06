package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.EnumTypeDefiner;
import org.molgenis.vibe.formats.GeneDiseaseCollection;

public enum GenePrioritizerFactory implements EnumTypeDefiner{
    HIGHEST_DISGENET_SCORE("gda_max") {
        @Override
        public GenePrioritizer create(GeneDiseaseCollection geneDiseaseCollection) {
             return new HighestSingleDisgenetScoreGenePrioritizer(geneDiseaseCollection);
        }
    },
    DISEASE_SPECIFICITY_INDEX("dsi") {
        @Override
        public GenePrioritizer create(GeneDiseaseCollection geneDiseaseCollection) {
            return new DiseaseSpecificityIndexGenePrioritizer(geneDiseaseCollection.getGenes());
        }
    },
    DISEASE_PLEIOTROPY_INDEX("dpi") {
        @Override
        public GenePrioritizer create(GeneDiseaseCollection geneDiseaseCollection) {
            return new DiseasePleiotropyIndexGenePrioritizer(geneDiseaseCollection.getGenes());
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
