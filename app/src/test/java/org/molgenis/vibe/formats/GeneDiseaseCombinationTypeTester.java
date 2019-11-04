package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.GeneDiseaseCombinationType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GeneDiseaseCombinationTypeTester {
    @Test
    public void useValidSioWithFullPrefix() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("sio:SIO_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithFullPrefixCaseReversed() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO:sio_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithFullPrefixUpperCase() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO:SIO_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithFullPrefixLowerCase() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("sio:sio_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithPartialPrefix() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithPartialPrefixLowerCase() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("sio_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithWithoutPrefix() throws InvalidStringFormatException {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void useNonExistingSio() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("999999");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidSioWithInvalidPrefix1() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("SII_000983");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidSioWithInvalidPrefix2() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("sii:SIO_000983");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortWithFullPrefix() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("sio:SIO_0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortWithPartialPrefix() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("SIO_0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortWithoutPrefix() throws InvalidStringFormatException {
        GeneDiseaseCombinationType.retrieve("0012");
    }
}
