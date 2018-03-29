package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DiseaseTester {
    @Test
    public void useValidDiseaseIdWithPrefix() throws InvalidStringFormatException {
        Disease disease = new Disease("umls:C0123456");
        Assert.assertEquals(disease.getId(), "C0123456");
        Assert.assertEquals(disease.getFormattedId(), "umls:C0123456");
    }

    @Test
    public void useValidDiseaseIdWithUpperCasePrefix() throws InvalidStringFormatException {
        Disease disease = new Disease("UMLS:C0123456");
        Assert.assertEquals(disease.getId(), "C0123456");
        Assert.assertEquals(disease.getFormattedId(), "umls:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidDiseaseIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Disease("Umls:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidDiseaseIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Disease("uMls:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidDiseaseIdWithoutPrefix() throws InvalidStringFormatException {
        Disease disease = new Disease("C0123456");
        Assert.assertEquals(disease.getId(), "C0123456");
        Assert.assertEquals(disease.getFormattedId(), "umls:C0123456");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidDiseaseIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Disease("uml:C0123456");
    }
}
