package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;

import java.util.Collection;

/**
 * {@link PhenotypesRetriever} implementation that retrieves {@link Phenotype}{@code s} based on a maximum distance from
 * the {@code source}.
 */
public class MaxDistanceRetriever extends PhenotypesRetriever {
    public MaxDistanceRetriever(OntModel model, Collection<Phenotype> inputPhenotypes, int maxDistance) {
        super(model, inputPhenotypes, maxDistance);
    }

    @Override
    public void run() {
        for(Phenotype phenotype:getInputPhenotypes()) {
            PhenotypeNetwork network = new PhenotypeNetwork(phenotype);
            traverse(retrievePhenotypeFromModel(phenotype), network, 0);
            getPhenotypeNetworkCollection().add(network);
        }
    }

    /**
     * Traverses the {@link OntModel}.
     * @param phenotypeOC current item being traversed
     * @param network stores the {@link Phenotype}{@code s} based on traversal
     * @param distance current distance from the {@code network source} (see {@link PhenotypeNetwork#getSource()})
     */
    private void traverse(OntClass phenotypeOC, PhenotypeNetwork network, int distance) {
        // Skips certain URIs.
        if(skippableUri(phenotypeOC)) {
            return;
        }

        addPhenotypeToNetwork(phenotypeOC, network, distance);

        // Checks if maximum distance is achieved. If so, returns. If not, continues.
        if(distance >= getMaxDistance()) {
            return;
        } else {
            // Calculates distance for next recursion step.
            int nextDistance = distance + 1;

            // Goes through the parents.
            for (ExtendedIterator<OntClass> it = phenotypeOC.listSuperClasses(true); it.hasNext(); ) {
                traverse(it.next(), network, nextDistance);
            }

            // Goes through the children.
            for (ExtendedIterator<OntClass> it = phenotypeOC.listSubClasses(true); it.hasNext(); ) {
                traverse(it.next(), network, nextDistance);
            }
        }
    }
}
