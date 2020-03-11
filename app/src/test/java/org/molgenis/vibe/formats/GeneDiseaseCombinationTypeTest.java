package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

public class GeneDiseaseCombinationTypeTest {
    @Test
    public void useValidSioWithPrefixLowerCase() {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("sio:SIO_000983");
        Assertions.assertAll(
                () -> Assertions.assertEquals("SIO_000983", type.getId()),
                () -> Assertions.assertEquals("sio:SIO_000983", type.getFormattedId())
        );
    }

    @Test
    public void useValidSioWithPrefixUpperCase() {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO:SIO_000983");
        Assertions.assertAll(
                () -> Assertions.assertEquals("SIO_000983", type.getId()),
                () -> Assertions.assertEquals("sio:SIO_000983", type.getFormattedId())
        );
    }

    @Test
    public void useInvalidSioWithPrefixLowerCase1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> GeneDiseaseCombinationType.retrieve("sio:sio_000983") );
    }

    @Test
    public void useInvalidSioWithPrefixUpperCase1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> GeneDiseaseCombinationType.retrieve("SIO:sio_000983") );
    }

    @Test
    public void useInvalidSioWithPrefixLowerCase2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> GeneDiseaseCombinationType.retrieve("sio:SIO_1") );
    }

    @Test
    public void useInvalidSioWithPrefixUpperCase2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> GeneDiseaseCombinationType.retrieve("SIO:SIO_1") );
    }

    @Test
    public void useValidSioWithoutPrefix() {
        GeneDiseaseCombinationType type = GeneDiseaseCombinationType.retrieve("SIO_000983");
        Assertions.assertAll(
                () -> Assertions.assertEquals("SIO_000983", type.getId()),
                () -> Assertions.assertEquals("sio:SIO_000983", type.getFormattedId())
        );
    }

    @Test
    public void useInvalidSioWithoutPrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> GeneDiseaseCombinationType.retrieve("sio_000983") );
    }

    @Test
    public void useInvalidSioWithoutPrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> GeneDiseaseCombinationType.retrieve("SIO_1") );
    }

    @Test
    public void useNonExistingSio() {
        Assertions.assertThrows(EnumConstantNotPresentException.class, () -> GeneDiseaseCombinationType.retrieve("SIO_999999") );
    }
}