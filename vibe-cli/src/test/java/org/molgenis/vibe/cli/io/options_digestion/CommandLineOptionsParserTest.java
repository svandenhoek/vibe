package org.molgenis.vibe.cli.io.options_digestion;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.cli.RunMode;
import org.molgenis.vibe.cli.TestData;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.cli.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.cli.io.output.target.FileOutputWriter;
import org.molgenis.vibe.cli.io.output.target.StdoutOutputWriter;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class CommandLineOptionsParserTest {
    private final String[] HELP = new String[]{"-h"};
    private final String[] VERSION = new String[]{"-v"};
    private final String[] DEBUG = new String[]{"-d"};
    private final String[] FORCE_OVERWRITE = new String[]{"-f"};
    private final String[] SIMPLIFIED_OUT = new String[]{"-l"};
    private final String[] URIS_OUT = new String[]{"-u"};

    private final String[] VALID_TDB = new String[]{"-t", TestData.TDB.getFullPathString()};
    private final String[] INVALID_TDB_DIR = new String[]{"-t", TestData.NON_EXISTING_DIR.getFullPathString()};
    private final String[] INVALID_TDB_FILE = new String[]{"-t", TestData.NON_EXISTING_FILE.getFullPathString()};

    private final String[] VALID_ONTOLOGY = new String[]{"-w", TestData.HPO_OWL.getFullPathString()};
    private final String[] INVALID_ONTOLOGY_FILE = new String[]{"-w", TestData.NON_EXISTING_FILE.getFullPathString()};
    private final String[] INVALID_ONTOLOGY_DIR = new String[]{"-w", TestData.NON_EXISTING_DIR.getFullPathString()};

    private final String[] HPO_ALGORITHM_CHILDREN = new String[]{"-n", "children"};
    private final String[] HPO_ALGORITHM_DISTANCE = new String[]{"-n", "distance"};
    private final String[] HPO_ALGORITHM_INVALID = new String[]{"-n", "myCustomName"};

    private final String[] VALID_DISTANCE = new String[]{"-m", "3"};
    private final String[] INVALID_DISTANCE_NUMBER = new String[]{"-m", "-1"};
    private final String[] INVALID_DISTANCE_STRING = new String[]{"-m", "three"};

    private final String[] VALID_HPO_SINGLE = new String[]{"-p", "hp:0123456"};
    private final String[] VALID_HPO_MULTIPLE = new String[]{"-p", "hp:0123456", "-p", "hp:6543210"};
    private final String[] INVALID_HPO = new String[]{"-p", "hp:0123", "-p", "hp:6543"};

    private final Set<Phenotype> VALID_HPO_SINGLE_SET = new HashSet<>(Arrays.asList(new Phenotype[]{
            new Phenotype("hp:0123456")
    }));
    private final Set<Phenotype> VALID_HPO_MULTIPLE_SET = new HashSet<>(Arrays.asList(new Phenotype[]{
            new Phenotype("hp:0123456"),
            new Phenotype("hp:6543210")
    }));

    private final String[] OUTPUT_FILE_NEW = new String[]{"-o", TestData.NON_EXISTING_FILE.getFullPathString()};
    private final String[] OUTPUT_FILE_EXISTING = new String[]{"-o", TestData.EXISTING_TSV.getFullPathString()};

    @Test
    void helpMessage() throws IOException, ParseException {
        String[] args = stringArraysMerger(HELP);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(RunMode.NONE, vibeOptions.getRunMode());
    }

    @Test
    void versionMessage() throws IOException, ParseException {
        String[] args = stringArraysMerger(VERSION);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(RunMode.NONE, vibeOptions.getRunMode());
    }

    @Test
    void noArguments() throws IOException, ParseException {
        String[] args = stringArraysMerger(new String[]{""});
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(RunMode.NONE, vibeOptions.getRunMode());
    }

    @Test
    void validSingleHpoWithoutOntologyTraversalUsingDefaultOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(VALID_HPO_SINGLE_SET, vibeOptions.getPhenotypes()),
                () -> Assertions.assertEquals(false, vibeOptions.isVerbose()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(OUTPUT_FILE_NEW[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    void validSingleHpoWithoutOntologyTraversalUsingDefaultOutputFileWithVerbose() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_NEW, DEBUG);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(true, vibeOptions.isVerbose()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(OUTPUT_FILE_NEW[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    void validSingleHpoWithoutOntologyTraversalUsingUriOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_NEW, URIS_OUT);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(OUTPUT_FILE_NEW[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_URI, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    void validSingleHpoWithoutOntologyTraversalUsingSimplifiedOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_NEW, SIMPLIFIED_OUT);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(OUTPUT_FILE_NEW[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.SIMPLE, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    void validSingleHpoWithoutOntologyTraversalUsingStdoutOutput() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(StdoutOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new StdoutOutputWriter().target(), vibeOptions.getOutputWriter().target()), // StdoutOutputWriter().target() == "stdout"
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    void validSingleHpoWithHpoAlgorithmChildren() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(PhenotypesRetrieverFactory.CHILDREN, vibeOptions.getPhenotypesRetrieverFactory())
        );
    }

    @Test
    void validSingleHpoWithHpoAlgorithmDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_DISTANCE, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(PhenotypesRetrieverFactory.DISTANCE, vibeOptions.getPhenotypesRetrieverFactory())
        );
    }

    @Test
    void validTwoHpos() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_MULTIPLE, OUTPUT_FILE_NEW);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(VALID_HPO_MULTIPLE_SET, vibeOptions.getPhenotypes());
    }

    @Test
    void unknownArgument() {
        String[] args = new String[]{"--zyxi"};
        Assertions.assertThrows(UnrecognizedOptionException.class, () -> CommandLineOptionsParser.parse(args) );
    }

    @Test
    void validSingleHpoWithoutOntologyTraversalUsingExstingOutputFileWithOverwrite() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_EXISTING, FORCE_OVERWRITE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(VALID_HPO_SINGLE_SET, vibeOptions.getPhenotypes()),
                () -> Assertions.assertEquals(false, vibeOptions.isVerbose()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(OUTPUT_FILE_EXISTING[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingExstingOutputFileWithoutOverwrite() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_EXISTING);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.EXISTING_TSV.getName() + " already exists.", exception.getMessage());
    }

    @Test
    void validSingleHpoWithInvalidHpoAlgorithm() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_INVALID, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(HPO_ALGORITHM_INVALID[1] + " is not a valid HPO retrieval algorithm.", exception.getMessage());
    }

    @Test
    void validSingleHpoWithInvalidHpoAlgorithmDistanceNumber() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, INVALID_DISTANCE_NUMBER, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(INVALID_DISTANCE_NUMBER[1] + " is not a valid HPO retrieval algorithm distance (must be a number >= 0).", exception.getMessage());
    }

    @Test
    void validSingleHpoWithInvalidHpoAlgorithmDistanceString() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, INVALID_DISTANCE_STRING, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(INVALID_DISTANCE_STRING[1] + " is not a valid HPO retrieval algorithm distance (must be a number >= 0).", exception.getMessage());
    }

    @Test
    void missingTdbAndHpoOntology() {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -t, -w", exception.getMessage());
    }

    @Test
    void missingTdb() {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE, VALID_ONTOLOGY);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -t", exception.getMessage());
    }

    @Test
    void missingOntology() {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE, VALID_TDB);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w", exception.getMessage());
    }

    @Test
    void noOntologyWithHpoAlgorithm() {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_CHILDREN, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w, -m", exception.getMessage());
    }

    @Test
    void noOntologyWithHpoMaxDistance() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w, -n", exception.getMessage());
    }

    @Test
    void withOntologyAndDistanceMissingHpoAlgorithm() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -n", exception.getMessage());
    }

    @Test
    void withOntologyAndAlgorithmMissingHpoMaxDistance() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -m", exception.getMessage());
    }

    @Test
    void withOntologyAlgorithmAndDistanceButNoOntologyFile() {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w", exception.getMessage());
    }

    @Test
    void missingPhenotype() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -p", exception.getMessage());
    }

    @Test
    void invalidPhenotype() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, INVALID_HPO, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(INVALID_HPO[1] + " does not adhere the required format: ^(hp|HP):([0-9]{7})$", exception.getMessage());
    }

    @Test
    void invalidTdbDir() {
        String[] args = stringArraysMerger(INVALID_TDB_DIR, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_DIR.getName() + " is not a directory.", exception.getMessage());
    }

    @Test
    void invalidTdbFile() {
        String[] args = stringArraysMerger(INVALID_TDB_FILE, VALID_ONTOLOGY, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_FILE.getName() + " is not a directory.", exception.getMessage());
    }

    @Test
    void invalidOntologyFile() {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_FILE, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_FILE.getName() + " is not a readable file.", exception.getMessage());
    }

    @Test
    void invalidOntologyDir() {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_DIR, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, OUTPUT_FILE_NEW);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_DIR.getName() + " is not a readable file.", exception.getMessage());
    }

    private String[] stringArraysMerger(String[]... arrays) {
        String[] fullArray = arrays[0];
        for(int i = 1; i < arrays.length; i++) {
            fullArray = ArrayUtils.addAll(fullArray, arrays[i]);
        }
        return fullArray;
    }
}
