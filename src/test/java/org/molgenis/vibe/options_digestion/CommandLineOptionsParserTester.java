package org.molgenis.vibe.options_digestion;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.lang3.ArrayUtils;
import org.molgenis.vibe.TestData;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class CommandLineOptionsParserTester {
    private final String[] VALID_TDB = new String[]{"-t", TestData.TDB_MINI.getDir()};
    private final String[] INVALID_TDB_DIR = new String[]{"-t", TestData.NON_EXISTING.getDir()};
    private final String[] INVALID_TDB_FILE = new String[]{"-t", TestData.NON_EXISTING.getFiles()[0]};

    private final String[] VALID_ONTOLOGY = new String[]{"-w", TestData.ONTOLOGY_FILE.getFiles()[0]};
    private final String[] INVALID_ONTOLOGY_FILE = new String[]{"-w", TestData.NON_EXISTING.getFiles()[0]};
    private final String[] INVALID_ONTOLOGY_DIR = new String[]{"-w", TestData.NON_EXISTING.getDir()};

    private final String[] HPO_ALGORITHM_1 = new String[]{"-n", "children"};
    private final String[] HPO_ALGORITHM_2 = new String[]{"-n", "distance"};
    private final String[] HPO_ALGORITHM_INVALID = new String[]{"-n", "myCustomName"};

    private final String[] MAX_DISTANCE = new String[]{"-m", "3"};

    private final String[] SINGLE_HPO = new String[]{"-p", "hp:0123456"};
    private final String[] TWO_HPOS = new String[]{"-p", "hp:0123456", "-p", "hp:6543210"};

    private final String[] NON_EXISTING_OUTPUT_FILE = new String[]{"-o", TestData.NON_EXISTING.getFiles()[0]};

    private final String[] GENE_SORTING_1 = new String[]{"-s", "gda_max"};
    private final String[] GENE_SORTING_2 = new String[]{"-s", "dsi"};
    private final String[] GENE_SORTING_3 = new String[]{"-s", "dpi"};
    private final String[] GENE_SORTING_INVALID = new String[]{"-s", "myCustomName"};

    @Test
    public void noArguments() throws IOException, ParseException {
        String[] args = new String[]{};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.NONE);
    }

    @Test(expectedExceptions = UnrecognizedOptionException.class)
    public void unknownArgument() throws IOException, ParseException {
        String[] args = new String[]{"--zyxi"};
        testWithErrorPrint(args);
    }

    @Test
    public void helpMessage() throws IOException, ParseException {
        String[] args = new String[]{"-h"};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.NONE);
    }

    @Test(groups = {"noTest"})
    public void showHelpMessage() {
        CommandLineOptionsParser.printHelpMessage();
    }

    @Test
    public void validSingleHpoWithHpoAlgorithm1() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validSingleHpoWithHpoAlgorithm2() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_2, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void validSingleHpoWithInvalidHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_INVALID, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validSingleHpoWithSortAlgorithm1() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, GENE_SORTING_1, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validSingleHpoWithSortAlgorithm2() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, GENE_SORTING_2, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validSingleHpoWithSortAlgorithm3() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, GENE_SORTING_3, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void validSingleHpoWithInvalidSortAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, GENE_SORTING_INVALID, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validSingleHpoWithoutOntology() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test
    public void validTwoHpos() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, TWO_HPOS, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingTdb() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void noOntologyWithHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_1, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void noOntologyWithHpoMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void withOntologyMissingHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void withOntologyMissingHpoMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void withOntologyMissingHpoAlgorithmAndHpoMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingPhenotype() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidTdbDir() throws IOException, ParseException {
        String[] args = stringArraysMerger(INVALID_TDB_DIR, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidTdbFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(INVALID_TDB_FILE, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidOntologyFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_FILE, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidOntologyDir() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_DIR, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        testWithErrorPrint(args);
    }

    private String[] stringArraysMerger(String[]... arrays) {
        String[] fullArray = arrays[0];
        for(int i = 1; i < arrays.length; i++) {
            fullArray = ArrayUtils.addAll(fullArray, arrays[i]);
        }
        return fullArray;
    }

    private void testWithErrorPrint(String[] args) throws IOException, ParseException {
        try {
            new CommandLineOptionsParser(args);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        new CommandLineOptionsParser(args);
    }
}
