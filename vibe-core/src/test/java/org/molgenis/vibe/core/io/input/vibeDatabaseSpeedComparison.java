package org.molgenis.vibe.core.io.input;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.molgenis.vibe.core.TestData;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.database_processing.GenesForPhenotypeRetriever;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Speed comparison, should only be triggered manually.
 */
@Disabled
@Execution(ExecutionMode.SAME_THREAD)
public class vibeDatabaseSpeedComparison {
    private Stopwatch stopwatch = Stopwatch.createUnstarted();
    private Set<Phenotype> phenotypes1 = new HashSet<> (Arrays.asList(new Phenotype("hp:0008438")));
    private Set<Phenotype> phenotypes2 = new HashSet<> (Arrays.asList(new Phenotype("hp:0001004"),
            new Phenotype("hp:0001511"), new Phenotype("hp:0001639"), new Phenotype("hp:0001640"),
            new Phenotype("hp:0001643"), new Phenotype("hp:0001653"), new Phenotype("hp:0001712"),
            new Phenotype("hp:0002721"), new Phenotype("hp:0004331")));

    @BeforeEach
    void beforeEach() {
        stopwatch.reset().start();
    }

    @Test
    void runphenotypes1WithTtl() {
        try(ModelReader modelReader = new ModelFilesReader(TestData.TTL.getFullPathString(), Lang.TTL)) {
            run(modelReader, phenotypes1);
        }
    }

    @Test
    void runphenotypes1WithTdb() throws IOException {
        try(ModelReader modelReader = new TripleStoreDbReader(TestData.TDB.getFullPathString())) {
            run(modelReader, phenotypes1);
        }
    }

    @Test
    void runphenotypes1WithHdt() throws IOException {
        try(ModelReader modelReader = new HdtFileReader(TestData.HDT.getFullPathString())) {
            run(modelReader, phenotypes1);
        }
    }

    @Test
    void runphenotypes2WithTdb() throws IOException {
        try(ModelReader modelReader = new TripleStoreDbReader(TestData.TDB.getFullPathString())) {
            run(modelReader, phenotypes2);
        }
    }

    @Test
    void runphenotypes2WithHdt() throws IOException {
        try(ModelReader modelReader = new HdtFileReader(TestData.HDT.getFullPathString())) {
            run(modelReader, phenotypes2);
        }
    }

    private void run(ModelReader modelReader, Set<Phenotype> phenotypes) {
        System.out.println("database loaded: " + stopwatch.toString());

        GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(modelReader, phenotypes);
        genesForPhenotypeRetriever.run();

        System.out.println("query finished: " + stopwatch.toString());
        System.out.println("hashcode: " + genesForPhenotypeRetriever.getGeneDiseaseCollection().hashCode());
        System.out.println("size: " + genesForPhenotypeRetriever.getGeneDiseaseCollection().size());
    }
}
