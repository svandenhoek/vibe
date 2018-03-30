package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.formats.Phenotype;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public abstract class PhenotypesRetriever {
    private OntModel model;
    private Set<Phenotype> inputPhenotypes;
    private Set<Phenotype> retrievedPhenotypes = new HashSet<>();

    public Set<Phenotype> getInputPhenotypes() {
        return inputPhenotypes;
    }

    public Set<Phenotype> getRetrievedPhenotypes() {
        return retrievedPhenotypes;
    }

    protected void setRetrievedPhenotypes(Set<Phenotype> phenotypes) {
        this.retrievedPhenotypes = phenotypes;
    }

    protected void addRetrievedPhenotype(Phenotype phenotype) {
        retrievedPhenotypes.add(phenotype);
    }

    public PhenotypesRetriever(OntModel model, Set<Phenotype> inputPhenotypes) {
        this.model = requireNonNull(model);
        this.inputPhenotypes = requireNonNull(inputPhenotypes);
    }

    protected OntClass retrievePhenotypeFromModel(Phenotype phenotype) {
        return model.getOntClass(phenotype.getUri().toString());
    }

    public abstract void run();
}
