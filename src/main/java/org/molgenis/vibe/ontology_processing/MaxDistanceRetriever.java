package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;

import java.net.URI;
import java.util.Collection;

/**
 * {@link PhenotypesRetriever} implementation that retrieves {@link Phenotype}{@code s} based on a maximum distance from
 * the {@code source}.
 */
public class MaxDistanceRetriever extends PhenotypesRetriever {
    /**
     * The maximum distance.
     */
    private int maxDistance;

    public MaxDistanceRetriever(OntModel model, Collection<Phenotype> phenotypes, int maxDistance) {
        super(model, phenotypes);
        this.maxDistance = maxDistance;
    }

    @Override
    public void run() {
        for(Phenotype phenotype:getInputPhenotypes()) {
            PhenotypeNetwork network = new PhenotypeNetwork(phenotype);
            traverse(retrievePhenotypeFromModel(phenotype), network, 0);
            addRetrievedPhenotypeNetworks(network);
        }
    }

    /**
     * Traverses the {@link OntModel}.
     * @param phenotypeOC current item being traversed
     * @param network stores the {@link Phenotype}{@code s} based on traversal
     * @param distance current distance from the {@code network source} (see {@link PhenotypeNetwork#getSource()})
     */
    private void traverse(OntClass phenotypeOC, PhenotypeNetwork network, int distance) {
        // Converts URI to Phenotype.
        Phenotype currentPhenotype = new Phenotype(URI.create(phenotypeOC.getURI()));

        // For distance 0, ignores checking/adding phenotype to network (as a Phenotype at position 0 should always be present).
        if(distance > 0) {
            // Checks if Phenotype is already visited. If not, adds it and continues. If already visited, checks whether
            // this was using a higher distance value. If so, re-adds the phenotype (now with a lower distance value) and
            // continues (as this lower distance means additional phenotypes need to be looked for and other already
            // known phenotypes might need a distance adjustment as well).
            if (network.contains(currentPhenotype) && network.getDistance(currentPhenotype) <= distance) {
                return;
            } else {
                network.add(currentPhenotype, distance);
            }
        }

        // Checks if maximum distance is achieved. If so, returns. If not, continues.
        if(distance >= maxDistance) {
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
