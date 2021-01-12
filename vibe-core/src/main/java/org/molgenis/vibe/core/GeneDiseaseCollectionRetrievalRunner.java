package org.molgenis.vibe.core;

import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.io.input.ModelReader;
import org.molgenis.vibe.core.io.input.TripleStoreDbReader;
import org.molgenis.vibe.core.database_processing.GenesForPhenotypeRetriever;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

public class GeneDiseaseCollectionRetrievalRunner implements Callable<GeneDiseaseCollection> {
    private Path vibeTdb;
    private Set<Phenotype> phenotypes;

    public GeneDiseaseCollectionRetrievalRunner(Path vibeTdb, Set<Phenotype> phenotypes) {
        this.vibeTdb = requireNonNull(vibeTdb);
        this.phenotypes = phenotypes;
    }

    @Override
    public GeneDiseaseCollection call() throws IOException {
        try ( ModelReader disgenetReader = new TripleStoreDbReader(vibeTdb) ) {
            // Retrieve from TDB.
            GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(
                    disgenetReader, phenotypes
            );
            genesForPhenotypeRetriever.run();

            // Return results.
            return genesForPhenotypeRetriever.getGeneDiseaseCollection();
        }
    }
}
