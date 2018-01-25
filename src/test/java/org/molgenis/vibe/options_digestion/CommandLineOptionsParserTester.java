package org.molgenis.vibe.options_digestion;

import org.apache.commons.cli.ParseException;
import org.molgenis.vibe.TestFile;
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

    @Test
    public void helpMessage() throws IOException, ParseException {
        String[] args = new String[]{"-h"};
        CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

        Assert.assertEquals(appOptions.getRunMode(), RunMode.NONE);
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
