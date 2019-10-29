package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

public class GeneTester {
    @Test
    public void useValidIdWithLowercasePrefix() throws InvalidStringFormatException {
        Gene gene = new Gene("ncbigene:1234");
        testIfValid(gene);
    }

    @Test
    public void useValidIdWithUppercasePrefix() throws InvalidStringFormatException {
        Gene gene = new Gene("NCBIGENE:1234");
        testIfValid(gene);
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Gene("Ncbigene:1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Gene("nCbigene:1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Gene("ncbi:1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithoutPrefix() throws InvalidStringFormatException {
        new Gene("1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useUriAsIdInput() throws InvalidStringFormatException {
        new Gene("http://identifiers.org/ncbigene/1234");
    }

    @Test
    public void useValidUri() {
        Gene gene = new Gene(URI.create("http://identifiers.org/ncbigene/1234"));
        testIfValid(gene);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void useInvalidUri() {
        Gene gene = new Gene(URI.create("http://identifiers.org/ncbi/1234"));
    }

    private void testIfValid(Gene gene) {
        Assert.assertEquals(gene.getId(), "1234");
        Assert.assertEquals(gene.getFormattedId(), "ncbigene:1234");
        Assert.assertEquals(gene.getUri(), URI.create("http://identifiers.org/ncbigene/1234"));
    }
}
