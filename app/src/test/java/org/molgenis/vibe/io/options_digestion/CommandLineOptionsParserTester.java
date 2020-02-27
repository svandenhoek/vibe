package org.molgenis.vibe.io.options_digestion;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.RunMode;
import org.molgenis.vibe.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.io.output.target.FileOutputWriter;
import org.molgenis.vibe.io.output.target.StdoutOutputWriter;

import java.io.IOException;
import java.nio.file.Paths;

public class CommandLineOptionsParserTester {
    private final String[] HELP = new String[]{"-h"};
    private final String[] VERBOSE = new String[]{"-v"};
    private final String[] SIMPLIFIED_OUT = new String[]{"-l"};
    private final String[] URIS_OUT = new String[]{"-u"};

    private final String[] VALID_TDB = new String[]{"-t", TestData.TDB.getDir()};
    private final String[] INVALID_TDB_DIR = new String[]{"-t", TestData.NON_EXISTING.getDir()};
    private final String[] INVALID_TDB_FILE = new String[]{"-t", TestData.NON_EXISTING.getFiles()[0]};

    private final String[] VALID_ONTOLOGY = new String[]{"-w", TestData.HPO_OWL.getFiles()[0]};
    private final String[] INVALID_ONTOLOGY_FILE = new String[]{"-w", TestData.NON_EXISTING.getFiles()[0]};
    private final String[] INVALID_ONTOLOGY_DIR = new String[]{"-w", TestData.NON_EXISTING.getDir()};

    private final String[] HPO_ALGORITHM_1 = new String[]{"-n", "children"};
    private final String[] HPO_ALGORITHM_2 = new String[]{"-n", "distance"};
    private final String[] HPO_ALGORITHM_INVALID = new String[]{"-n", "myCustomName"};

    private final String[] VALID_DISTANCE = new String[]{"-m", "3"};
    private final String[] INVALID_DISTANCE_NUMBER = new String[]{"-m", "-1"};
    private final String[] INVALID_DISTANCE_STRING = new String[]{"-m", "three"};

    private final String[] VALID_HPO_SINGLE = new String[]{"-p", "hp:0123456"};
    private final String[] VALID_HPO_MULTIPLE = new String[]{"-p", "hp:0123456", "-p", "hp:6543210"};
    private final String[] INVALID_HPO = new String[]{"-p", "hp:0123", "-p", "hp:6543"};

    private final String[] VALID_OUTPUT_FILE = new String[]{"-o", TestData.NON_EXISTING.getFiles()[0]};
    private final String[] INVALID_OUTPUT_FILE = new String[]{"-o", TestData.EXISTING_TSV.getFiles()[0]};

    @Test
    public void helpMessage() throws IOException, ParseException {
        String[] args = stringArraysMerger(HELP);
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(RunMode.NONE, appOptions.getRunMode());
    }

    @Test
    public void noArguments() throws IOException, ParseException {
        String[] args = stringArraysMerger(new String[]{""});
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(RunMode.NONE, appOptions.getRunMode());
    }

    @Test
    public void validSingleHpoWithoutOntologyUsingDefaultOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        CommandLineOptionsParser cmdParser = testWithErrorPrint(args);

        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES, cmdParser.getRunMode());
        Assert.assertEquals(false, cmdParser.isVerbose());
        Assert.assertEquals(FileOutputWriter.class, cmdParser.getOutputWriter().getClass());
        Assert.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), cmdParser.getOutputWriter().target());
        Assert.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, cmdParser.getGenePrioritizedOutputFormatWriterFactory());
    }

    @Test
    public void validSingleHpoWithoutOntologyUsingDefaultOutputFileWithVerbose() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_HPO_SINGLE, VALID_OUTPUT_FILE, VERBOSE);
        CommandLineOptionsParser cmdParser = new CommandLineOptionsParser(args);

        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES, cmdParser.getRunMode());
        Assert.assertEquals(true, cmdParser.isVerbose());
        Assert.assertEquals(FileOutputWriter.class, cmdParser.getOutputWriter().getClass());
        Assert.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), cmdParser.getOutputWriter().target());
        Assert.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, cmdParser.getGenePrioritizedOutputFormatWriterFactory());
    }

    @Test
    public void validSingleHpoWithoutOntologyUsingUriOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_HPO_SINGLE, VALID_OUTPUT_FILE, URIS_OUT);
        CommandLineOptionsParser cmdParser = testWithErrorPrint(args);

        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES, cmdParser.getRunMode());
        Assert.assertEquals(FileOutputWriter.class, cmdParser.getOutputWriter().getClass());
        Assert.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), cmdParser.getOutputWriter().target());
        Assert.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_URI, cmdParser.getGenePrioritizedOutputFormatWriterFactory());
    }

    @Test
    public void validSingleHpoWithoutOntologyUsingSimplifiedOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_HPO_SINGLE, VALID_OUTPUT_FILE, SIMPLIFIED_OUT);
        CommandLineOptionsParser cmdParser = testWithErrorPrint(args);

        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES, cmdParser.getRunMode());
        Assert.assertEquals(FileOutputWriter.class, cmdParser.getOutputWriter().getClass());
        Assert.assertEquals(new FileOutputWriter(Paths.get(VALID_OUTPUT_FILE[1])).target(), cmdParser.getOutputWriter().target());
        Assert.assertEquals(GenePrioritizedOutputFormatWriterFactory.SIMPLE, cmdParser.getGenePrioritizedOutputFormatWriterFactory());
    }

    @Test
    public void validSingleHpoWithoutOntologyUsingStdoutOutput() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_HPO_SINGLE);
        CommandLineOptionsParser cmdParser = testWithErrorPrint(args);

        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES, cmdParser.getRunMode());
        Assert.assertEquals(StdoutOutputWriter.class, cmdParser.getOutputWriter().getClass());
        Assert.assertEquals(new StdoutOutputWriter().target(), cmdParser.getOutputWriter().target()); // StdoutOutputWriter().target() == "stdout"
        Assert.assertEquals(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID, cmdParser.getGenePrioritizedOutputFormatWriterFactory());
    }

    @Test
    public void validSingleHpoWithHpoAlgorithm1() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        CommandLineOptionsParser cmdParser = testWithErrorPrint(args);
        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES, cmdParser.getRunMode());
    }

    @Test
    public void validSingleHpoWithHpoAlgorithm2() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_2, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validTwoHpos() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB,  VALID_HPO_MULTIPLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void withOntologyMissingHpoAlgorithmAndHpoMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        CommandLineOptionsParser cmdParser = testWithErrorPrint(args);
        Assert.assertEquals(RunMode.GENES_FOR_PHENOTYPES, cmdParser.getRunMode());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void unknownArgument() throws IOException, ParseException {
        String[] args = new String[]{"--zyxi"};
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void validSingleHpoWithoutOntologyUsingInvalidOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_HPO_SINGLE, INVALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void validSingleHpoWithInvalidHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_INVALID, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void validSingleHpoWithInvalidHpoAlgorithmDistanceNumber() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_INVALID, INVALID_DISTANCE_NUMBER, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void validSingleHpoWithInvalidHpoAlgorithmDistanceString() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_INVALID, INVALID_DISTANCE_STRING, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void missingTdb() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void noOntologyWithHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_1, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void noOntologyWithHpoMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void withOntologyAndDistanceMissingHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void withOntologyAndAlgorithmMissingHpoMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void withOntologyAlgorithmAndDistanceButNoOntologyFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_1, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void missingPhenotype() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void invalidPhenotype() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_HPO, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void invalidTdbDir() throws IOException, ParseException {
        String[] args = stringArraysMerger(INVALID_TDB_DIR, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void invalidTdbFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(INVALID_TDB_FILE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void invalidOntologyFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_FILE, HPO_ALGORITHM_1, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expected = IOException.class)
    public void invalidOntologyDir() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_DIR, HPO_ALGORITHM_1, VALID_DISTANCE, VALID_HPO_SINGLE, VALID_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    private String[] stringArraysMerger(String[]... arrays) {
        String[] fullArray = arrays[0];
        for(int i = 1; i < arrays.length; i++) {
            fullArray = ArrayUtils.addAll(fullArray, arrays[i]);
        }
        return fullArray;
    }

    private CommandLineOptionsParser testWithErrorPrint(String[] args) throws IOException, ParseException {
        try {
            return(new CommandLineOptionsParser(args));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
