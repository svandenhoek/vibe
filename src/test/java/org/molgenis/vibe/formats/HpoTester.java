package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HpoTester {
    @Test
    public void useValidHpoIdWithPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hp:1234567");
        Assert.assertEquals(hpo.getId(), 1234567);
        Assert.assertEquals(hpo.getFormattedId(), "hp:1234567");
    }

    @Test
    public void useValidHpoIdWithoutPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("1234567");
        Assert.assertEquals(hpo.getId(), 1234567);
        Assert.assertEquals(hpo.getFormattedId(), "hp:1234567");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidHpoIdWithInvalidPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hpo:1234567");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortHpoIdWithPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("hp:12345");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortHpoIdWithoutPrefix() throws InvalidStringFormatException {
        Hpo hpo = new Hpo("12345");
    }
}
