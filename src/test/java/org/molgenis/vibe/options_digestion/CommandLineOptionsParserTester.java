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


    private final String[] HPO_ALGORITHM_1 = new String[]{"-c"};
    private final String[] HPO_ALGORITHM_2 = new String[]{"-d"};
    private final String[] BOTH_HPO_ALGORITHMS = new String[]{"-c", "-d"};

    private final String[] MAX_DISTANCE = new String[]{"-m", "3"};

    private final String[] SINGLE_HPO = new String[]{"-p", "hp:0123456"};
    private final String[] TWO_HPOS = new String[]{"-p", "hp:0123456", "-p", "hp:6543210"};

    private final String[] NON_EXISTING_OUTPUT_FILE = new String[]{"-o", TestData.NON_EXISTING.getFiles()[0]};


    @Test
    public void noArguments() throws IOException, ParseException {
        String[] args = new String[]{};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.NONE);
    }

    @Test(expectedExceptions = UnrecognizedOptionException.class)
    public void unknownArgument() throws IOException, ParseException {
        String[] args = new String[]{"--zyxi"};
        printError(args);
        new CommandLineOptionsParser(args);
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
    public void validSingleHpo() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        new CommandLineOptionsParser(args);
    }

    @Test
    public void validSingleHpoWithoutOntology() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        new CommandLineOptionsParser(args);
    }

    @Test
    public void validTwoHpos() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, TWO_HPOS, NON_EXISTING_OUTPUT_FILE);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingAllButOneArgument() throws IOException, ParseException {
        String[] args = HPO_ALGORITHM_1;
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingTdb() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingOntologyWithAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, HPO_ALGORITHM_1, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingOntologyWithMaxDistance() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingRelatedHpoAlgorithm() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingMaxDistanceRelatedHpo() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingPhenotype() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingOutputFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void hasBothExclusiveArguments() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, VALID_ONTOLOGY, BOTH_HPO_ALGORITHMS, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidTdbDir() throws IOException, ParseException {
        System.out.println(TestData.NON_EXISTING.getDir());
        System.out.println(TestData.NON_EXISTING.getFiles()[0]);
        String[] args = stringArraysMerger(INVALID_TDB_DIR, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidTdbFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(INVALID_TDB_FILE, VALID_ONTOLOGY, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidOntologyFile() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_FILE, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void invalidOntologyDir() throws IOException, ParseException {
        String[] args = stringArraysMerger(VALID_TDB, INVALID_ONTOLOGY_DIR, HPO_ALGORITHM_1, MAX_DISTANCE, SINGLE_HPO, NON_EXISTING_OUTPUT_FILE);
        printError(args);
        new CommandLineOptionsParser(args);
    }

    private String[] stringArraysMerger(String[]... arrays) {
        String[] fullArray = arrays[0];
        for(int i = 1; i < arrays.length; i++) {
            fullArray = ArrayUtils.addAll(fullArray, arrays[i]);
        }
        return fullArray;
    }

    private void printError(String[] args) throws IOException, ParseException {
        try {
            new CommandLineOptionsParser(args);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        new CommandLineOptionsParser(args);
    }
}
