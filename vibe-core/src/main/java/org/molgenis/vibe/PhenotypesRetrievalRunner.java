package org.molgenis.vibe;

import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.io.input.OntologyModelFilesReader;
import org.molgenis.vibe.ontology_processing.PhenotypesRetriever;
import org.molgenis.vibe.ontology_processing.PhenotypesRetrieverFactory;

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

    public void setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory phenotypesRetrieverFactory) {
        this.phenotypesRetrieverFactory = requireNonNull(phenotypesRetrieverFactory);
    }

    public void setPhenotypes(Collection<Phenotype> phenotypes) {
        this.phenotypes = requireNonNull(phenotypes);
    }

    public void setMaxDistance(Integer maxDistance) {
        if(maxDistance < 0) {
            throw new InvalidParameterException("maxDistance must be >= 0: " + maxDistance);
        }
        this.maxDistance = maxDistance;
    }

    public PhenotypesRetrievalRunner(Path hpoOntologyFile, PhenotypesRetrieverFactory phenotypesRetrieverFactory, Collection<Phenotype> phenotypes, Integer maxDistance) {
        this.hpoOntologyFile = requireNonNull(hpoOntologyFile);
        setPhenotypesRetrieverFactory(phenotypesRetrieverFactory);
        setPhenotypes(phenotypes);
        setMaxDistance(maxDistance);
    }

    public PhenotypeNetworkCollection call() {
        OntologyModelFilesReader ontologyReader = null;
        try {
            // Load model.
            ontologyReader = new OntologyModelFilesReader(hpoOntologyFile.toString());

            // Retrieve from model.
            PhenotypesRetriever hpoRetriever = phenotypesRetrieverFactory.create(
                    ontologyReader.getModel(), phenotypes, maxDistance
            );
            hpoRetriever.run();

            // Returns results.
            return hpoRetriever.getPhenotypeNetworkCollection();
        } finally {
            // Close model.
            if (ontologyReader != null) {
                ontologyReader.close();
            }
        }
    }
}
