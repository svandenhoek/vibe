package org.molgenis.vibe.core;

import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.io.input.ModelReader;
import org.molgenis.vibe.core.io.input.TripleStoreDbReader;
import org.molgenis.vibe.core.tdb_processing.GenesForPhenotypeRetriever;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

public class GeneDiseaseCollectionRetrievalRunner implements Callable<GeneDiseaseCollection>, Closeable {
    private Path vibeTdb;
    private Set<Phenotype> phenotypes;
    private ModelReader disgenetReader;

    public void setPhenotypes(Set<Phenotype> phenotypes) {
        this.phenotypes = requireNonNull(phenotypes);
    }

    public GeneDiseaseCollectionRetrievalRunner(Path vibeTdb, Set<Phenotype> phenotypes) {
        this.vibeTdb = requireNonNull(vibeTdb);
        setPhenotypes(phenotypes);
    }

    @Override
    public GeneDiseaseCollection call() throws IOException {
        // Load TDB.
        if(disgenetReader == null) {
            disgenetReader = new TripleStoreDbReader(vibeTdb);
        }

        // Retrieve from TDB.
        GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(
                disgenetReader, phenotypes
        );
        genesForPhenotypeRetriever.run();

        return genesForPhenotypeRetriever.getGeneDiseaseCollection();
    }

    @Override
    public void close() {
        if(disgenetReader != null) {
            disgenetReader.close();
        }
    }
}
