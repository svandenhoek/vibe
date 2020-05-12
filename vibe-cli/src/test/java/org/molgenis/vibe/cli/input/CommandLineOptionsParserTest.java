package org.molgenis.vibe.cli.input;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.cli.RunMode;
import org.molgenis.vibe.cli.TestData;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.cli.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.cli.output.target.FileOutputWriter;
import org.molgenis.vibe.cli.output.target.StdoutOutputWriter;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommandLineOptionsParserTest {
    private final String[] HELP = new String[]{"-h"};
    private final String[] VERBOSE = new String[]{"-v"};
    private final String[] SIMPLIFIED_OUT = new String[]{"-l"};
    private final String[] URIS_OUT = new String[]{"-u"};

    private final String[] VALID_TDB = new String[]{"-t", TestData.TDB.getFullPath()};
    private final String[] INVALID_TDB_DIR = new String[]{"-t", TestData.NON_EXISTING_DIR.getFullPath()};
    private final String[] INVALID_TDB_FILE = new String[]{"-t", TestData.NON_EXISTING_FILE.getFullPath()};

    private final String[] VALID_ONTOLOGY = new String[]{"-w", TestData.HPO_OWL.getFullPath()};
    private final String[] INVALID_ONTOLOGY_FILE = new String[]{"-w", TestData.NON_EXISTING_FILE.getFullPath()};
    private final String[] INVALID_ONTOLOGY_DIR = new String[]{"-w", TestData.NON_EXISTING_DIR.getFullPath()};

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

    private final String[] VALID_OUTPUT_FILE = new String[]{"-o", TestData.NON_EXISTING_FILE.getFullPath()};
    private final String[] INVALID_OUTPUT_FILE = new String[]{"-o", TestData.EXISTING_TSV.getFullPath()};

    @Test
    public void helpMessage() throws IOException, ParseException {
        String[] args = stringArraysMerger(HELP);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(RunMode.NONE, vibeOptions.getRunMode());
    }

    @Test
    public void noArguments() throws IOException, ParseException {
        String[] args = stringArraysMerger(new String[]{""});
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(RunMode.NONE, vibeOptions.getRunMode());
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingDefaultOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(VALID_HPO_SINGLE_SET, vibeOptions.getPhenotypes()),
                () -> Assertions.assertEquals(false, vibeOptions.isVerbose()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingDefaultOutputFileWithVerbose() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE, VERBOSE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(true, vibeOptions.isVerbose()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingUriOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE, URIS_OUT);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_URI, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingSimplifiedOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE, SIMPLIFIED_OUT);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(FileOutputWriter.class, vibeOptions.getOutputWriter().getClass()),
                () -> Assertions.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), vibeOptions.getOutputWriter().target()),
                () -> Assertions.assertEquals(GenePrioritizedOutputFormatWriterFactory.SIMPLE, vibeOptions.getGenePrioritizedOutputFormatWriterFactory())
        );
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingStdoutOutput() throws IOException, ParseException {
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
    public void validSingleHpoWithHpoAlgorithmChildren() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(PhenotypesRetrieverFactory.CHILDREN, vibeOptions.getPhenotypesRetrieverFactory())
        );
    }

    @Test
    public void validSingleHpoWithHpoAlgorithmDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_DISTANCE, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertAll(
                () -> Assertions.assertEquals(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES, vibeOptions.getRunMode()),
                () -> Assertions.assertEquals(PhenotypesRetrieverFactory.DISTANCE, vibeOptions.getPhenotypesRetrieverFactory())
        );
    }

    @Test
    public void validTwoHpos() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_MULTIPLE, VALID_OUTPUT_FILE);
        VibeOptions vibeOptions = CommandLineOptionsParser.parse(args);

        Assertions.assertEquals(VALID_HPO_MULTIPLE_SET, vibeOptions.getPhenotypes());
    }

    @Test
    public void unknownArgument() {
        String[] args = new String[]{"--zyxi"};
        Assertions.assertThrows(UnrecognizedOptionException.class, () -> CommandLineOptionsParser.parse(args) );
    }

    @Test
    public void validSingleHpoWithoutOntologyTraversalUsingInvalidOutputFile() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, INVALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.EXISTING_TSV.getName() + " already exists.", exception.getMessage());

    }

    @Test
    public void validSingleHpoWithInvalidHpoAlgorithm() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_INVALID, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(HPO_ALGORITHM_INVALID[1] + " is not a valid HPO retrieval algorithm.", exception.getMessage());
    }

    @Test
    public void validSingleHpoWithInvalidHpoAlgorithmDistanceNumber() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, INVALID_DISTANCE_NUMBER, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(INVALID_DISTANCE_NUMBER[1] + " is not a valid HPO retrieval algorithm distance (must be a number >= 0).", exception.getMessage());
    }

    @Test
    public void validSingleHpoWithInvalidHpoAlgorithmDistanceString() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, INVALID_DISTANCE_STRING, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(INVALID_DISTANCE_STRING[1] + " is not a valid HPO retrieval algorithm distance (must be a number >= 0).", exception.getMessage());
    }

    @Test
    public void missingTdbAndHpoOntology() {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -t, -w", exception.getMessage());
    }

    @Test
    public void missingTdb() {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE, VALID_ONTOLOGY);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -t", exception.getMessage());
    }

    @Test
    public void missingOntology() {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE, VALID_TDB);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w", exception.getMessage());
    }

    @Test
    public void noOntologyWithHpoAlgorithm() {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_CHILDREN, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w, -m", exception.getMessage());
    }

    @Test
    public void noOntologyWithHpoMaxDistance() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w, -n", exception.getMessage());
    }

    @Test
    public void withOntologyAndDistanceMissingHpoAlgorithm() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -n", exception.getMessage());
    }

    @Test
    public void withOntologyAndAlgorithmMissingHpoMaxDistance() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_CHILDREN, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -m", exception.getMessage());
    }

    @Test
    public void withOntologyAlgorithmAndDistanceButNoOntologyFile() {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -w", exception.getMessage());
    }

    @Test
    public void missingPhenotype() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals("Missing arguments: -p", exception.getMessage());
    }

    @Test
    public void invalidPhenotype() {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, INVALID_HPO, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(INVALID_HPO[1] + " does not adhere the required format: ^(hp|HP):([0-9]{7})$", exception.getMessage());
    }

    @Test
    public void invalidTdbDir() {
        String[] args = stringArraysMerger(INVALID_TDB_DIR, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_DIR.getName() + " is not a directory.", exception.getMessage());
    }

    @Test
    public void invalidTdbFile() {
        String[] args = stringArraysMerger(INVALID_TDB_FILE, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_FILE.getName() + " is not a directory.", exception.getMessage());
    }

    @Test
    public void invalidOntologyFile() {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_FILE, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

        Exception exception = Assertions.assertThrows(IOException.class, () -> CommandLineOptionsParser.parse(args) );
        Assertions.assertEquals(TestData.NON_EXISTING_FILE.getName() + " is not a readable file.", exception.getMessage());
    }

    @Test
    public void invalidOntologyDir() {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_DIR, HPO_ALGORITHM_CHILDREN, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);

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
