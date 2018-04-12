package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;
import org.molgenis.vibe.formats.PhenotypeNetworkCollection;

import java.net.URI;
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
    private PhenotypeNetworkCollection phenotypeNetworkCollection = new PhenotypeNetworkCollection();

    public Collection<Phenotype> getInputPhenotypes() {
        return inputPhenotypes;
    }

    public PhenotypeNetworkCollection getPhenotypeNetworkCollection() {
        return phenotypeNetworkCollection;
    }

    public PhenotypesRetriever(OntModel model, Collection<Phenotype> inputPhenotypes) {
        this.model = requireNonNull(model);
        this.inputPhenotypes = requireNonNull(inputPhenotypes);
    }

    protected OntClass retrievePhenotypeFromModel(Phenotype phenotype) {
        return model.getOntClass(phenotype.getUri().toString());
    }

    public abstract void run();

    /**
     * Checks whether an URI is skippable. This is based on the fact that while the URI is an expected result, it is not
     * used within the application. Otherwise these URIs could cause an error because they would be treated as an unexpected
     * result.
     *
     * @param phenotypeOC the {@link OntClass} representing a {@link Phenotype} that needs to be checked
     * @return {@code true} if {@link OntClass} can be skipped (expected non-valid {@link Phenotype#uri}), {@code false}
     * if not (can either be a valid or an unexpected non-valid {@link Phenotype#uri})
     */
    protected boolean skippableUri(OntClass phenotypeOC) {
        String uriString = phenotypeOC.getURI();
        if(uriString.startsWith("http://purl.obolibrary.org/obo/UPHENO_")) {
            return true;
        }

        return false;
    }

    /**
     * Adds a {@link OntClass} representing a {@link Phenotype} to a {@link PhenotypeNetwork}.
     * @param phenotypeOC what needs to be added to the {@code network}
     * @param network where the {@code phenotypeOC} needs to be added to
     * @param distance the number to be used as {@code distance} when adding the {@code phenotypeOC} to the {@code network}
     */
    protected void addPhenotypeToNetwork(OntClass phenotypeOC, PhenotypeNetwork network, int distance) {
        // Converts URI to Phenotype and tries to add it to the network.
        Phenotype currentPhenotype = new Phenotype(URI.create(phenotypeOC.getURI()));
        network.add(currentPhenotype, distance);
    }
}
