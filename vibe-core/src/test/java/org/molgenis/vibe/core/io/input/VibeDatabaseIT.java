package org.molgenis.vibe.core.io.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.TestData;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.database_processing.GenesForPhenotypeRetriever;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Disabled for now until decided if future releases will still support TDB as well.
 */
@Disabled
public class VibeDatabaseIT {
    @Test
    void checkOutputIsEqualAmongDatabases() throws IOException {
        Set<Phenotype> phenotypes = new HashSet<>(Arrays.asList(new Phenotype("hp:0000003")));
        GeneDiseaseCollection tdbCollection;
        GeneDiseaseCollection hdtCollection;

        // Run query.
        try(ModelReader modelReader = new TripleStoreDbReader(TestData.TDB.getFullPathString())) {
            tdbCollection = run(modelReader, phenotypes);
        }
        try(ModelReader modelReader = new HdtFileReader(TestData.HDT.getFullPathString())) {
            hdtCollection = run(modelReader, phenotypes);
        }

        // Assert output among different databases is equal.
        Assertions.assertTrue(tdbCollection.equals(hdtCollection));
        Assertions.assertTrue(tdbCollection.allFieldsEquals(hdtCollection));
    }

    private GeneDiseaseCollection run(ModelReader modelReader, Set<Phenotype> phenotypes) {
        GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(modelReader, phenotypes);
        genesForPhenotypeRetriever.run();
        return(genesForPhenotypeRetriever.getGeneDiseaseCollection());
    }
}
