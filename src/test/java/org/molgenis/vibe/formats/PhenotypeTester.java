package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

public class PhenotypeTester {
    @Test
    public void useValidPhenotypeIdWithPrefix() throws InvalidStringFormatException {
        Phenotype phenotype = new Phenotype("hp:0012345");
        Assert.assertEquals(phenotype.getId(), "0012345");
        Assert.assertEquals(phenotype.getFormattedId(), "hp:0012345");
        Assert.assertEquals(phenotype.getUri(), URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
    }

    @Test
    public void useValidPhenotypeIdWithUpperCasePrefix() throws InvalidStringFormatException {
        Phenotype phenotype = new Phenotype("HP:0012345");
        Assert.assertEquals(phenotype.getId(), "0012345");
        Assert.assertEquals(phenotype.getFormattedId(), "hp:0012345");
        Assert.assertEquals(phenotype.getUri(), URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
    }

    @Test
    public void useValidPhenotypeUri() throws InvalidStringFormatException {
        Phenotype phenotype = new Phenotype(URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
        Assert.assertEquals(phenotype.getId(), "0012345");
        Assert.assertEquals(phenotype.getFormattedId(), "hp:0012345");
        Assert.assertEquals(phenotype.getUri(), URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useUriAsIdInput() {
        new Phenotype("http://purl.obolibrary.org/obo/HP_0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidPhenotypeIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Phenotype("Hp:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidPhenotypeIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Phenotype("hP:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidPhenotypeIdWithoutPrefix() throws InvalidStringFormatException {
        Phenotype phenotype = new Phenotype("0012345");
        Assert.assertEquals(phenotype.getId(), "0012345");
        Assert.assertEquals(phenotype.getFormattedId(), "hp:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidPhenotypeIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Phenotype("hpo:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortPhenotypeIdWithPrefix() throws InvalidStringFormatException {
        new Phenotype("hp:0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortPhenotypeIdWithoutPrefix() throws InvalidStringFormatException {
        new Phenotype("0012");
    }
}
