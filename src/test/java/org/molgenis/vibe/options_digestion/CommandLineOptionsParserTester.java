package org.molgenis.vibe.options_digestion;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.exceptions.OptionsException;
import org.molgenis.vibe.formats.Phenotype;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class CommandLineOptionsParserTester {

    @Test
    public void noArguments() throws IOException, ParseException {
        String[] args = new String[]{};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.NONE);
    }

    @Test(expectedExceptions = UnrecognizedOptionException.class)
    public void unknownArgument() throws IOException, ParseException {
        String[] args = new String[]{"--zyxi"};
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

    @Test(expectedExceptions = OptionsException.class)
    public void missingOntologyFile() throws IOException, ParseException {
        String[] args = new String[]{"-nd", "0", "-d", TestData.TDB_MINI.getDir(), "-o", "/path/to/output/file"};
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = OptionsException.class)
    public void missingOntologyMaxDistance() throws IOException, ParseException {
        String[] args = new String[]{"-n", TestData.ONTOLOGY_FILE.getFiles()[0], "-d", TestData.TDB_MINI.getDir(), "-o", "/path/to/output/file"};
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = OptionsException.class)
    public void missingDisgenetDatabase() throws IOException, ParseException {
        String[] args = new String[]{"-n", TestData.ONTOLOGY_FILE.getFiles()[0], "-nd", "0", "-o", "/path/to/output/file", "-p", "hp:1234567"};
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = OptionsException.class)
    public void missingOutputFile() throws IOException, ParseException {
        String[] args = new String[]{"-n", TestData.ONTOLOGY_FILE.getFiles()[0], "-nd", "0", "-d", TestData.TDB_MINI.getDir(), "-p", "hp:1234567"};
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = OptionsException.class)
    public void missingPhenotype() throws IOException, ParseException {
        String[] args = new String[]{"-n", TestData.ONTOLOGY_FILE.getFiles()[0], "-nd", "0", "-d", TestData.TDB_MINI.getDir(), "-o", "/path/to/output/file"};
        new CommandLineOptionsParser(args);
    }

    @Test
    public void getGenesUsingSinglePhenotype() throws IOException, ParseException {
        String[] args = new String[]{"-n", TestData.ONTOLOGY_FILE.getFiles()[0], "-nd", "0", "-d", TestData.TDB_MINI.getDir(), "-o", "/path/to/output/file", "-p", "hp:1234567"};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.GET_DISGENET_GENE_GDAS_FOR_PHENOTYPES);
        Assert.assertEquals(appOptions.getHpoOntology().toString(), TestData.ONTOLOGY_FILE.getFiles()[0]);
        Assert.assertEquals(appOptions.getOntologyMaxDistance(), 0);
        Assert.assertEquals(appOptions.getDisgenetDataDir().toString(), TestData.TDB_MINI.getDir());
        Assert.assertEquals(appOptions.getPhenotypes().size(), 1);
        Assert.assertTrue(appOptions.getPhenotypes().contains(new Phenotype("hp:1234567")));
    }

    @Test
    public void getGenesUsingMultiplePhenotypes() throws IOException, ParseException {
        String[] args = new String[]{"-n", TestData.ONTOLOGY_FILE.getFiles()[0], "-nd", "0", "-d", TestData.TDB_MINI.getDir(), "-o", "/path/to/output/file", "-p", "hp:1234567", "-p", "hp:7654321"};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.GET_DISGENET_GENE_GDAS_FOR_PHENOTYPES);
        Assert.assertEquals(appOptions.getHpoOntology().toString(), TestData.ONTOLOGY_FILE.getFiles()[0]);
        Assert.assertEquals(appOptions.getOntologyMaxDistance(), 0);
        Assert.assertEquals(appOptions.getDisgenetDataDir().toString(), TestData.TDB_MINI.getDir());
        Assert.assertEquals(appOptions.getPhenotypes().size(), 2);
        Assert.assertTrue(appOptions.getPhenotypes().contains(new Phenotype("hp:1234567")));
        Assert.assertTrue(appOptions.getPhenotypes().contains(new Phenotype("hp:7654321")));
    }
}
