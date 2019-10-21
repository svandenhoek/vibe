package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.formats.EnumTypeDefiner;
import org.molgenis.vibe.formats.Phenotype;

import java.util.Collection;

public enum PhenotypesRetrieverFactory implements EnumTypeDefiner {
    CHILDREN("children", "Retrieving HPO children.") {
        @Override
        public PhenotypesRetriever create(OntModel model, Collection<Phenotype> inputPhenotypes, int distance) {
            return new ChildrenRetriever(model, inputPhenotypes, distance);
        }
    },
    DISTANCE("distance", "Retrieving connected HPOs.") {
        @Override
        public PhenotypesRetriever create(OntModel model, Collection<Phenotype> inputPhenotypes, int distance) {
            return new MaxDistanceRetriever2(model, inputPhenotypes, distance);
        }
    };

    private String id;

    private String description;

    @Override
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    PhenotypesRetrieverFactory(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public abstract PhenotypesRetriever create(OntModel model, Collection<Phenotype> inputPhenotypes, int distance);

    public static PhenotypesRetrieverFactory retrieve(String name) {
        return EnumTypeDefiner.retrieve(name, PhenotypesRetrieverFactory.class);
    }
}
