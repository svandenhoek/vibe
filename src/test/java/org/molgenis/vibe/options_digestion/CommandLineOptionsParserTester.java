package org.molgenis.vibe.options_digestion;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.molgenis.vibe.TestFilesDir;
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

    @Test(expectedExceptions = IOException.class)
    public void missingDisgenetDatabase() throws IOException, ParseException {
        String[] args = new String[]{"-p", "hp:1234567", "-o", "/path/to/output/file"};
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingOutputFile() throws IOException, ParseException {
        String[] args = new String[]{"-d", TestFilesDir.TDB_MINI.getDir(), "-p", "hp:1234567"};
        new CommandLineOptionsParser(args);
    }

    @Test(expectedExceptions = IOException.class)
    public void missingPhenotype() throws IOException, ParseException {
        String[] args = new String[]{"-d", TestFilesDir.TDB_MINI.getDir(), "-o", "/path/to/output/file"};
        new CommandLineOptionsParser(args);
    }

    /**
     * Future implementations should support multiple phenotypes (depending on run mode).
     * @throws IOException
     * @throws ParseException
     */
    @Test(expectedExceptions = IOException.class)
    public void multiplePhenotypes() throws IOException, ParseException {
        String[] args = new String[]{"-d", TestFilesDir.TDB_MINI.getDir(), "-p", "hp:1234567", "-p", "hp:7654321", "-o", "/path/to/output/file"};
        new CommandLineOptionsParser(args);
    }

    @Test
    public void getGenesUsingSinglePhenotype() throws IOException, ParseException {
        String[] args = new String[]{"-d", TestFilesDir.TDB_MINI.getDir(), "-p", "hp:1234567", "-o", "/path/to/output/file"};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.GET_GENES_USING_SINGLE_PHENOTYPE);
        Assert.assertEquals(appOptions.getDisgenetDataDir().toString(), TestFilesDir.TDB_MINI.getDir());
        Assert.assertEquals(appOptions.getPhenotypes().size(), 1);
        Assert.assertTrue(appOptions.getPhenotypes().contains(new Phenotype("hp:1234567")));
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
