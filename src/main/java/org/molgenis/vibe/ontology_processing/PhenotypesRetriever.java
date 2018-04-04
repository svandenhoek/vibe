package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;

import java.util.*;

import static java.util.Objects.requireNonNull;

public abstract class PhenotypesRetriever {
    private OntModel model;
    private Collection<Phenotype> inputPhenotypes;
    private Set<PhenotypeNetwork> retrievedPhenotypeNetworks = new HashSet<>();

    public Collection<Phenotype> getInputPhenotypes() {
        return inputPhenotypes;
    }

    public Set<PhenotypeNetwork> getRetrievedPhenotypeNetworks() {
        return retrievedPhenotypeNetworks;
    }

    protected void setRetrievedPhenotypeNetworks(Set<PhenotypeNetwork> retrievedPhenotypeNetworks) {
        this.retrievedPhenotypeNetworks = retrievedPhenotypeNetworks;
    }

    protected void addRetrievedPhenotypeNetworks(PhenotypeNetwork phenotypeNetwork) {
        retrievedPhenotypeNetworks.add(phenotypeNetwork);
    }

    public PhenotypesRetriever(OntModel model, Collection<Phenotype> inputPhenotypes) {
        this.model = requireNonNull(model);
        this.inputPhenotypes = requireNonNull(inputPhenotypes);
    }

    protected OntClass retrievePhenotypeFromModel(Phenotype phenotype) {
        return model.getOntClass(phenotype.getUri().toString());
    }

    public abstract void run();
}
