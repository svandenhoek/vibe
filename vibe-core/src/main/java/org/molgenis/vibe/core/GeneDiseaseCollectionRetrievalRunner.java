package org.molgenis.vibe.core;

import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.io.input.ModelReader;
import org.molgenis.vibe.core.database_processing.GenesForPhenotypeRetriever;
import org.molgenis.vibe.core.io.input.VibeDatabase;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

public class GeneDiseaseCollectionRetrievalRunner implements Callable<GeneDiseaseCollection> {
    private VibeDatabase vibeDatabase;
    private Set<Phenotype> phenotypes;

    public GeneDiseaseCollectionRetrievalRunner(VibeDatabase vibeDatabase, Set<Phenotype> phenotypes) {
        this.vibeDatabase = requireNonNull(vibeDatabase);
        this.phenotypes = phenotypes;
    }

    @Override
    public GeneDiseaseCollection call() throws IOException {
        try ( ModelReader modelReader = vibeDatabase.getModelReader() ) {
            // Retrieve from database.
            GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(
                    modelReader, phenotypes
            );
            genesForPhenotypeRetriever.run();

            // Return results.
            return genesForPhenotypeRetriever.getGeneDiseaseCollection();
        }
    }
}
