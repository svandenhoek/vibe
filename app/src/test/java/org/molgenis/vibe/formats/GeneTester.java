package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

public class GeneTester {
    @Test
    public void useValidGeneIdWithPrefix() throws InvalidStringFormatException {
        Gene gene = new Gene("ncbigene:1234");
        testValidGene(gene);
    }

    @Test
    public void useValidGeneIdWithUpperCasePrefix() throws InvalidStringFormatException {
        Gene gene = new Gene("NCBIGENE:1234");
        testValidGene(gene);
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useUriAsIdInput() {
        new Gene("http://identifiers.org/ncbigene/1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidGeneIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Gene("Ncbigene:1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidGeneIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Gene("nCbigene:1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidGeneIdWithoutPrefix() throws InvalidStringFormatException {
        new Gene("1234");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidGeneIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Gene("ncbi:1234");
    }

    @Test
    public void useValidGeneUri() throws InvalidStringFormatException {
        Gene gene = new Gene(URI.create("http://identifiers.org/ncbigene/1234"));
        testValidGene(gene);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void useInvalidGeneUri() throws InvalidStringFormatException {
        Gene gene = new Gene(URI.create("http://identifiers.org/ncbi/1234"));
    }

    public void testValidGene(Gene gene) {
        Assert.assertEquals(gene.getId(), "1234");
        Assert.assertEquals(gene.getFormattedId(), "ncbigene:1234");
        Assert.assertEquals(gene.getUri(), URI.create("http://identifiers.org/ncbigene/1234"));
    }
}
