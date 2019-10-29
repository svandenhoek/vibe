package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

public class PhenotypeTester {
    @Test
    public void useValidIdWithLowercasePrefix() throws InvalidStringFormatException {
        Phenotype phenotype = new Phenotype("hp:0012345");
        testIfValid(phenotype);
    }

    @Test
    public void useValidIdWithUppercasePrefix() throws InvalidStringFormatException {
        Phenotype phenotype = new Phenotype("HP:0012345");
        testIfValid(phenotype);
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Gene("Hp:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Gene("hP:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Gene("ph:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithoutPrefix() throws InvalidStringFormatException {
        new Gene("0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useUriAsIdInput() throws InvalidStringFormatException {
        new Gene("http://purl.obolibrary.org/obo/HP_0012345");
    }

    @Test
    public void useValidUri() {
        Phenotype phenotype = new Phenotype(URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
        testIfValid(phenotype);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void useInvalidUri() {
        new Gene(URI.create("http://purl.obolibrary.org/obo/id/HP_0012345"));
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortPhenotypeIdWithPrefix() throws InvalidStringFormatException {
        new Phenotype("hp:0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortPhenotypeIdWithoutPrefix() throws InvalidStringFormatException {
        new Phenotype("0012");
    }

    private void testIfValid(Phenotype phenotype) {
        Assert.assertEquals(phenotype.getId(), "0012345");
        Assert.assertEquals(phenotype.getFormattedId(), "hp:0012345");
        Assert.assertEquals(phenotype.getUri(), URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
    }
}
