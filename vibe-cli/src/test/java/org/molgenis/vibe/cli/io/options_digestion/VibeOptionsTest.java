package org.molgenis.vibe.cli.io.options_digestion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.cli.RunMode;
import org.molgenis.vibe.cli.TestData;
import org.molgenis.vibe.cli.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class VibeOptionsTest {
    private VibeOptions vibeOptions;

    private final String VALID_TDB = TestData.TDB.getFullPathString();
    private final String INVALID_TDB_DIR = TestData.NON_EXISTING_DIR.getFullPathString();
    private final String INVALID_TDB_FILE = TestData.NON_EXISTING_FILE.getFullPathString();

    private final String VALID_ONTOLOGY = TestData.HPO_OWL.getFullPathString();
    private final String INVALID_ONTOLOGY_FILE = TestData.NON_EXISTING_FILE.getFullPathString();
    private final String INVALID_ONTOLOGY_DIR = TestData.NON_EXISTING_DIR.getFullPathString();

    private final Integer VALID_DISTANCE = 3;
    private final Integer INVALID_DISTANCE_NUMBER = -1;

    private final Set<Phenotype> VALID_HPO_SINGLE = new HashSet<>(Arrays.asList(new Phenotype[]{
            new Phenotype("hp:0123456")
    }));

    @BeforeEach
    void beforeEach() {
        vibeOptions = new VibeOptions();
    }

    @Test
    void validRunModeNone() {
        vibeOptions.setRunMode(RunMode.NONE);

        vibeOptions.validate();
    }

    @Test
    void validRunModeGenesForPhenotypes() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        Assertions.assertTrue(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesMissingTdb() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        Assertions.assertFalse(vibeOptions.validate());

    }

    @Test
    void genesForPhenotypesMissingHpoOwl() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesMissingPhenotypes() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesMissingOutputWriter() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesMissingGenePrioritizationOutputFormat() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesMissingOutputRelatedInfo() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void validGenesForPhenotypesWithRelatedPhenotypes() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        vibeOptions.setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory.CHILDREN);
        vibeOptions.setOntologyMaxDistance(VALID_DISTANCE);

        Assertions.assertTrue(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesWithRelatedPhenotypesMissingAlgorithm() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        vibeOptions.setOntologyMaxDistance(VALID_DISTANCE);

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void genesForPhenotypesWithRelatedPhenotypesMissingMaxDistance() throws IOException {
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES);
        vibeOptions.setVibeTdb(VALID_TDB);
        vibeOptions.setHpoOntology(VALID_ONTOLOGY);
        vibeOptions.setPhenotypes(VALID_HPO_SINGLE);
        vibeOptions.setStdoutOutputWriter();
        vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);

        vibeOptions.setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory.CHILDREN);

        Assertions.assertFalse(vibeOptions.validate());
    }

    @Test
    void invalidMaxDistance() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> vibeOptions.setOntologyMaxDistance(INVALID_DISTANCE_NUMBER) );
    }

    @Test
    void invalidTdbDir() {
        Assertions.assertThrows(IOException.class, () -> vibeOptions.setVibeTdb(INVALID_TDB_DIR) );
    }

    @Test
    void invalidTdbFile() {
        Assertions.assertThrows(IOException.class, () -> vibeOptions.setVibeTdb(INVALID_TDB_FILE) );
    }

    @Test
    void invalidHpoFile() {
        Assertions.assertThrows(IOException.class, () -> vibeOptions.setHpoOntology(INVALID_ONTOLOGY_FILE) );
    }

    @Test
    void invalidHpoDir() {
        Assertions.assertThrows(IOException.class, () -> vibeOptions.setHpoOntology(INVALID_ONTOLOGY_DIR) );
    }


}
