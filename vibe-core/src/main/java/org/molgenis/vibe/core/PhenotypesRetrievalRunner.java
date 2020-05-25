package org.molgenis.vibe.core;

import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.core.io.input.OntologyModelFilesReader;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetriever;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * Class containing all required logic for retrieving related {@link Phenotype}{@code s} for the input
 * {@link Phenotype}{@code s}.
 */
public class PhenotypesRetrievalRunner implements Callable<PhenotypeNetworkCollection> {
    private Path hpoOntologyFile;
    private PhenotypesRetrieverFactory phenotypesRetrieverFactory;
    private Collection<Phenotype> phenotypes;
    private Integer maxDistance;

    private void setMaxDistance(Integer maxDistance) {
        requireNonNull(maxDistance);
        if(maxDistance < 0) {
            throw new InvalidParameterException("maxDistance must be >= 0: " + maxDistance);
        }
        this.maxDistance = maxDistance;
    }

    public PhenotypesRetrievalRunner(Path hpoOntologyFile, PhenotypesRetrieverFactory phenotypesRetrieverFactory, Collection<Phenotype> phenotypes, Integer maxDistance) {
        this.hpoOntologyFile = requireNonNull(hpoOntologyFile);
        this.phenotypesRetrieverFactory = requireNonNull(phenotypesRetrieverFactory);
        this.phenotypes = requireNonNull(phenotypes);
        setMaxDistance(maxDistance);
    }

    public PhenotypeNetworkCollection call() {
        try ( OntologyModelFilesReader ontologyReader = new OntologyModelFilesReader(hpoOntologyFile.toString()) ) {
            // Retrieve from model.
            PhenotypesRetriever hpoRetriever = phenotypesRetrieverFactory.create(
                    ontologyReader.getModel(), phenotypes, maxDistance
            );
            hpoRetriever.run();

            // Returns results.
            return hpoRetriever.getPhenotypeNetworkCollection();
        }
    }
}
