package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * For one or more {@link Phenotype}{@code s} within an {@link OntModel}, retrieves other {@link Phenotype}{@code s} that are linked to it.
 */
public abstract class PhenotypesRetriever {
    /**
     * The model that stores the {@link Phenotype}{@code s}.
     */
    private OntModel model;

    /**
     * The input {@link Phenotype}{@code s} for which linked {@link Phenotype}{@code s} need to be found.
     */
    private Collection<Phenotype> inputPhenotypes;

    /**
     * The found linked {@link Phenotype}{@code s} for the {@code inputPhenotypes} (stored per {@code inputPhenotype}).
     */
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
