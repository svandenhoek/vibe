package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DisgenetAssociationTypeTester {
    @Test
    public void useValidSioWithFullPrefix() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("sio:SIO_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithFullPrefixCaseReversed() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("SIO:sio_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithFullPrefixUpperCase() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("SIO:SIO_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithFullPrefixLowerCase() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("sio:sio_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithPartialPrefix() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("SIO_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithPartialPrefixLowerCase() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("sio_000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test
    public void useValidSioWithWithoutPrefix() throws InvalidStringFormatException {
        DisgenetAssociationType type = DisgenetAssociationType.retrieve("000983");
        Assert.assertEquals(type.getId(), "000983");
        Assert.assertEquals(type.getFormattedId(), "sio:SIO_000983");
    }

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void useNonExistingSio() throws InvalidStringFormatException {
        DisgenetAssociationType.retrieve("999999");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidSioWithInvalidPrefix1() throws InvalidStringFormatException {
        DisgenetAssociationType.retrieve("SII_000983");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useValidSioWithInvalidPrefix2() throws InvalidStringFormatException {
        DisgenetAssociationType.retrieve("sii:SIO_000983");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortWithFullPrefix() throws InvalidStringFormatException {
        DisgenetAssociationType.retrieve("sio:SIO_0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortWithPartialPrefix() throws InvalidStringFormatException {
        DisgenetAssociationType.retrieve("SIO_0012");
    }

    @Test(expectedExceptions = InvalidStringFormatException.class)
    public void useTooShortWithoutPrefix() throws InvalidStringFormatException {
        DisgenetAssociationType.retrieve("0012");
    }
}
