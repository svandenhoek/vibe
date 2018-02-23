package org.molgenis.vibe.options_digestion;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.formats.Hpo;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class CommandLineOptionsParserTester {

    @Test
    public void noArguments() throws IOException, ParseException {
        String[] args = new String[]{};
        new CommandLineOptionsParser(args);
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

    @Test
    public void getGenesWIthSingleHpoId() throws IOException, ParseException {
        String[] args = new String[]{"-d", TestFilesDir.TDB_MINI.getDir(), "-p", "hp:1234567"};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.GET_GENES_WITH_SINGLE_HPO);
        Assert.assertEquals(appOptions.getDisgenetDataDir().toString(), TestFilesDir.TDB_MINI.getDir());
        Assert.assertEquals(appOptions.getHpos().size(), 1);
        Assert.assertTrue(appOptions.getHpos().contains(new Hpo("hp:1234567")));
    }

//    @Test
//    public void allFilesPresent() throws IOException, ParseException {
//        String[] args = new String[]{"-d", TestFile.DIR_REQUIRED_FILES_FULL.getFilePath()};
//        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);
//
//        Assert.assertEquals(appOptions.getDisgenetDataDir().toString(), TestFile.DIR_REQUIRED_FILES_FULL.getFilePath());
//        Assert.assertEquals(appOptions.getDisgenetRdfVersion(), DisgenetRdfVersion.V5);
//    }
//
//    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "disease-phenotype.ttl, gda_SIO_001342.ttl, pda.ttl, sio-release.owl are not readable/missing.")
//    public void filesMissing() throws IOException, ParseException {
//        String[] args = new String[]{"-d", TestFile.DIR_REQUIRED_FILES_MISSING.getFilePath()};
//        new CommandLineOptionsParser(args);
//    }
}
