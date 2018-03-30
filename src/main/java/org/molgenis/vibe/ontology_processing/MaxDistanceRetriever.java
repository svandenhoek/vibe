package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.molgenis.vibe.formats.Phenotype;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class MaxDistanceRetriever extends PhenotypesRetriever {
    private int maxDistance;
    public MaxDistanceRetriever(OntModel model, Set<Phenotype> phenotypes, int maxDistance) {
        super(model, phenotypes);
        this.maxDistance = maxDistance;
    }

    @Override
    public void run() {
        for(Phenotype phenotype:getInputPhenotypes()) {
            Set<OntClass> visited = new HashSet<>();
            traverse(retrievePhenotypeFromModel(phenotype), visited, 0);
        }
    }

    private void traverse(OntClass phenotypeOC, Set<OntClass> visited, int distance) {
        // Converts URI to Phenotype.
        Phenotype currentPhenotype = new Phenotype(URI.create(phenotypeOC.getURI()));

        // Checks if Phenotype is already visited. If so, returns. If not, adds phenotype to collection.
        if(getRetrievedPhenotypes().contains(currentPhenotype)) {
            return;
        } else {
            addRetrievedPhenotype(currentPhenotype);
        }

        // Checks if maximum distance is achieved. If so, returns. If not, continues.
        if(distance >= maxDistance) {
            return;
        } else {
            // Calculates distance for next recursion step.
            int nextDistance = distance + 1;

            // Goes through the parents.
            for (ExtendedIterator<OntClass> it = phenotypeOC.listSuperClasses(true); it.hasNext(); ) {
                traverse(it.next(), visited, nextDistance);
            }

            // Goes through the children.
            for (ExtendedIterator<OntClass> it = phenotypeOC.listSubClasses(true); it.hasNext(); ) {
                traverse(it.next(), visited, nextDistance);
            }
        }

    }
}
