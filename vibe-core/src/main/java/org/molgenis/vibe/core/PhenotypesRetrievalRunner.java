package org.molgenis.vibe.core;

import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.core.io.input.OntologyModelFilesReader;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetriever;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

/**
 * Class containing all required logic for retrieving related {@link Phenotype}{@code s} for the input
 * {@link Phenotype}{@code s}.
 */
public class PhenotypesRetrievalRunner implements Callable<PhenotypeNetworkCollection>, Closeable {
    private Path hpoOntologyFile;
    private PhenotypesRetrieverFactory phenotypesRetrieverFactory;
    private Collection<Phenotype> phenotypes;
    private Integer maxDistance;

    private OntologyModelFilesReader ontologyReader;

    public void setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory phenotypesRetrieverFactory) {
        this.phenotypesRetrieverFactory = requireNonNull(phenotypesRetrieverFactory);
    }

    public void setPhenotypes(Collection<Phenotype> phenotypes) {
        this.phenotypes = requireNonNull(phenotypes);
    }

    public void setMaxDistance(Integer maxDistance) {
        requireNonNull(maxDistance);
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
        // Load model.
        if(ontologyReader == null) {
            ontologyReader = new OntologyModelFilesReader(hpoOntologyFile.toString());
        }

        // Retrieve from model.
        PhenotypesRetriever hpoRetriever = phenotypesRetrieverFactory.create(
                ontologyReader.getModel(), phenotypes, maxDistance
        );
        hpoRetriever.run();

        // Returns results.
        return hpoRetriever.getPhenotypeNetworkCollection();
    }

    @Override
    public void close() {
        if (ontologyReader != null) {
            ontologyReader.close();
        }
    }
}
