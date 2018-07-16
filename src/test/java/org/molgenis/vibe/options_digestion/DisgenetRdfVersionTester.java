package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DisgenetRdfVersionTester {
    private static final DisgenetRdfVersion rdfV5 = DisgenetRdfVersion.V5;

    @Test
    public void testVersionRetriever1() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieve("5");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever2() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieve("v5");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever3() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieve("V5");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever4() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieve("5.0");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test
    public void testVersionRetriever5() throws InvalidStringFormatException {
        DisgenetRdfVersion actual = DisgenetRdfVersion.retrieve("v5.0");

        Assert.assertEquals(actual, rdfV5);
    }

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void testVersionRetriever6() throws InvalidStringFormatException {
        DisgenetRdfVersion.retrieve("50");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void testVersionRetriever7() throws InvalidStringFormatException {
        DisgenetRdfVersion.retrieve("v");
    }
}
