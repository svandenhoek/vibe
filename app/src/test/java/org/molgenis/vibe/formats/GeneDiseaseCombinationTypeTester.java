package org.molgenis.vibe.formats;

import org.junit.Assert;
import org.junit.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

public class GeneDiseaseCombinationTypeTester {
    @Test
    public void useValidSioWithPrefixLowerCase() {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("sio:SIO_000983");
        Assert.assertEquals("SIO_000983", type.getId());
        Assert.assertEquals("sio:SIO_000983", type.getFormattedId());
    }

    @Test
    public void useValidSioWithPrefixUpperCase() {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO:SIO_000983");
        Assert.assertEquals("SIO_000983", type.getId());
        Assert.assertEquals("sio:SIO_000983", type.getFormattedId());
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useInvalidSioWithPrefixLowerCase1() {
        GeneDiseaseCombinationType.retrieve("sio:sio_000983");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useInvalidSioWithPrefixUpperCase1() {
        GeneDiseaseCombinationType.retrieve("SIO:sio_000983");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useInvalidSioWithPrefixLowerCase2() {
        GeneDiseaseCombinationType.retrieve("sio:SIO_1");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useInvalidSioWithPrefixUpperCase2() {
        GeneDiseaseCombinationType.retrieve("SIO:SIO_1");
    }

    @Test
    public void useValidSioWithoutPrefix() {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO_000983");
        Assert.assertEquals("SIO_000983", type.getId());
        Assert.assertEquals("sio:SIO_000983", type.getFormattedId());
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useInvalidSioWithoutPrefix1() {
        GeneDiseaseCombinationType.retrieve("sio_000983");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useInvalidSioWithoutPrefix2() {
        GeneDiseaseCombinationType.retrieve("SIO_1");
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void useNonExistingSio() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("SIO_999999");
    }
}