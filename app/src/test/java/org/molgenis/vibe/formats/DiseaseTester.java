package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

public class DiseaseTester {
    @Test
    public void useValidIdWithLowercasePrefix() throws InvalidStringFormatException {
        Disease disease = new Disease("umls:C0123456");
        testIfValid(disease);
    }

    @Test
    public void useValidIdWithUppercasePrefix() throws InvalidStringFormatException {
        Disease disease = new Disease("UMLS:C0123456");
        testIfValid(disease);
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Disease("Umls:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Disease("uMls:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Disease("ulms:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidIdWithoutPrefix() throws InvalidStringFormatException {
        new Disease("C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useUriAsIdInput() throws InvalidStringFormatException {
        new Disease("http://linkedlifedata.com/resource/umls/id/C0123456");
    }

    @Test
    public void useValidUri() {
        Disease disease = new Disease(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456"));
        testIfValid(disease);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void useInvalidUri() {
        new Disease(URI.create("http://linkedlifedata.com/resource/umls/C0123456"));
    }

    private void testIfValid(Disease disease) {
        Assert.assertEquals(disease.getId(), "C0123456");
        Assert.assertEquals(disease.getFormattedId(), "umls:C0123456");
        Assert.assertEquals(disease.getUri(), URI.create("http://linkedlifedata.com/resource/umls/id/C0123456"));
    }
}
