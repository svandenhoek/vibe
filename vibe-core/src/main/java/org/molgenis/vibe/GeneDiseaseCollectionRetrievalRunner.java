package org.molgenis.vibe;

import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.input.ModelReader;
import org.molgenis.vibe.io.input.TripleStoreDbReader;
import org.molgenis.vibe.tdb_processing.GenesForPhenotypeRetriever;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

public class GeneDiseaseCollectionRetrievalRunner implements Callable<GeneDiseaseCollection> {
    private Path vibeTdb;
    private Set<Phenotype> phenotypes;

    public void setPhenotypes(Set<Phenotype> phenotypes) {
        this.phenotypes = requireNonNull(phenotypes);
    }

    public GeneDiseaseCollectionRetrievalRunner(Path vibeTdb, Set<Phenotype> phenotypes) {
        this.vibeTdb = requireNonNull(vibeTdb);
        setPhenotypes(phenotypes);
    }

    @Override
    public GeneDiseaseCollection call() throws IOException {
        ModelReader disgenetReader = null;
        try {
            // Load TDB.
            disgenetReader = new TripleStoreDbReader(vibeTdb);

            // Retrieve from TDB.
            GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(
                    disgenetReader, phenotypes
            );
            genesForPhenotypeRetriever.run();

            return genesForPhenotypeRetriever.getGeneDiseaseCollection();
        } finally {
            // Close TDB.
            if(disgenetReader != null) {
                disgenetReader.close();
            }
        }
    }
}
