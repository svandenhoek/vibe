package org.molgenis.vibe.core.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.formats.PhenotypeNetwork;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MaxDistanceRetriever extends PhenotypesRetriever {
    public MaxDistanceRetriever(OntModel model, Collection<Phenotype> inputPhenotypes, int maxDistance) {
        super(model, inputPhenotypes, maxDistance);
    }

    @Override
    public void run() {
        for(Phenotype phenotype:getInputPhenotypes()) {
            PhenotypeNetwork network = new PhenotypeNetwork(phenotype);

            // previousPhenotypeOCs is an empty Set
            // currentPhenotypeOCs is a single inputPhenotype
            Set<OntClass> startOC = new HashSet<>();
            startOC.add(retrievePhenotypeFromModel(phenotype));
            traverse(new HashSet<>(), startOC, network, 0);

            getPhenotypeNetworkCollection().add(network);
        }
    }

    /**
     * Traverses the {@link OntModel}.
     * @param previousPhenotypeOCs all {@link Phenotype}{@code s} for {@code distance - 1}
     * @param currentPhenotypeOCs all {@link Phenotype}{@code s} for the current {@code distance}
     * @param network stores the {@link Phenotype}{@code s} based on traversal
     * @param distance current distance from the {@code network source} (see {@link PhenotypeNetwork#getSource()})
     */
    private void traverse(Set<OntClass> previousPhenotypeOCs, Set<OntClass> currentPhenotypeOCs, PhenotypeNetwork network, int distance) {
        // For storing phenotypes with a distance + 1
        Set<OntClass> nextPhenotypeOCs = new HashSet<>();

        for(OntClass phenotypeOC : currentPhenotypeOCs) {
            // Checks if URI is of a known exception.
            if (skippableUri(phenotypeOC)) {
                continue;
            }

            // Adds current phenotype to the network.
            addPhenotypeToNetwork(phenotypeOC, network, distance);

            // Looks further if maxDistance is not reached yet.
            if (distance < getMaxDistance()) {
                // Adds all the parent and child phenotypes to the "distance + 1" set.
                nextPhenotypeOCs.addAll(phenotypeOC.listSuperClasses().toSet());
                nextPhenotypeOCs.addAll(phenotypeOC.listSubClasses().toSet());
            }
        }

        // Removes all "distance - 1" phenotypes from the "distance + 1" set.
        nextPhenotypeOCs.removeAll(previousPhenotypeOCs);

        // If there are any phenotypes remaining with "distance + 1", continues.
        // Note that if the max distance is reached, no phenotypes were ever added to this set.
        if(nextPhenotypeOCs.size() > 0) {
            traverse(currentPhenotypeOCs, nextPhenotypeOCs, network, distance + 1);
        }
    }
}
