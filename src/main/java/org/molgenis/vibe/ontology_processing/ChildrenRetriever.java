package org.molgenis.vibe.ontology_processing;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetwork;

import java.util.Collection;

public class ChildrenRetriever extends PhenotypesRetriever {
    public ChildrenRetriever(OntModel model, Collection<Phenotype> inputPhenotypes, int maxDistance) {
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

    private void traverse(OntClass phenotypeOC, PhenotypeNetwork network, int distance) {
        addPhenotypeToNetwork(phenotypeOC, network, distance);

        int nextDistance = distance + 1;
        if(nextDistance <= getMaxDistance()) {
            for (ExtendedIterator<OntClass> it = phenotypeOC.listSubClasses(); it.hasNext(); ) {
                OntClass nextOC = it.next();
                traverse(nextOC, network, nextDistance);
            }
        }
    }
}
