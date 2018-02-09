package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HpoTester {
    @Test
    public void useValidHpoIdWithPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hp:0012345");
        Assert.assertEquals(hpo.getId(), "0012345");
        Assert.assertEquals(hpo.getFormattedId(), "hp:0012345");
    }

    @Test
    public void useValidHpoIdWithUpperCasePrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("HP:0012345");
        Assert.assertEquals(hpo.getId(), "0012345");
        Assert.assertEquals(hpo.getFormattedId(), "hp:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidHpoIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("Hp:0012345");
        Assert.assertEquals(hpo.getId(), "0012345");
        Assert.assertEquals(hpo.getFormattedId(), "hp:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidHpoIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hP:0012345");
        Assert.assertEquals(hpo.getId(), "0012345");
        Assert.assertEquals(hpo.getFormattedId(), "hp:0012345");
    }

    @Test
    public void useValidHpoIdWithoutPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("0012345");
        Assert.assertEquals(hpo.getId(), "0012345");
        Assert.assertEquals(hpo.getFormattedId(), "hp:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidHpoIdWithInvalidPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hpo:0012345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortHpoIdWithPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hp:0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortHpoIdWithoutPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("0012");
    }
}
