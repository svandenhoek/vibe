package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DisgenetRdfVersionTester {
    private static final DisgenetRdfVersion rdfV5 = DisgenetRdfVersion.V5;
    private static final DisgenetRdfVersion rdfUnsupported = DisgenetRdfVersion.UNSUPPORTED;

    @Test
    public void testVersionRetriever1() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieveVersion("5");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever2() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieveVersion("v5");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever3() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieveVersion("V5");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever4() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieveVersion("5.0");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever5() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieveVersion("v5.0");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever6() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieveVersion("50");

        Assert.assertEquals(actual, rdfUnsupported);
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void testVersionRetriever7() throws InvalidStringFormatException {
        DisgenetRdfVersion.retrieveVersion("v");
    }
}
