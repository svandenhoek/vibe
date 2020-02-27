package org.molgenis.vibe.formats;

import org.junit.Assert;
import org.junit.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new Disease("Umls:C0123456");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new Disease("uMls:C0123456");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithInvalidPrefix() throws InvalidStringFormatException {
        new Disease("ulms:C0123456");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithoutPrefix() throws InvalidStringFormatException {
        new Disease("C0123456");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useUriAsIdInput() throws InvalidStringFormatException {
        new Disease("http://linkedlifedata.com/resource/umls/id/C0123456");
    }

    @Test
    public void useValidUri() {
        Disease disease = new Disease(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456"));
        testIfValid(disease);
    }

    @Test(expected = IllegalArgumentException.class)
    public void useInvalidUri() {
        new Disease(URI.create("http://linkedlifedata.com/resource/umls/C0123456"));
    }

    @Test
    public void testSort() {
        List<Disease> actualOrder = new ArrayList<>( Arrays.asList(
                new Disease("umls:C3"),
                new Disease("umls:C8"),
                new Disease("umls:C1")
        ));

        List<Disease> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(2),
                actualOrder.get(0),
                actualOrder.get(1)
        ));

        Collections.sort(actualOrder);
        Assert.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(Disease disease) {
        Assert.assertEquals("C0123456", disease.getId());
        Assert.assertEquals("umls:C0123456", disease.getFormattedId());
        Assert.assertEquals(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456"), disease.getUri());
    }
}
