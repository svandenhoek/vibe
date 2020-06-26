package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneSymbolTest {
    @Test
    public void useValidIdWithLowercasePrefix() {
        GeneSymbol symbol = new GeneSymbol("hgnc:AB-123");
        testIfValid(symbol);
    }

    @Test
    public void useValidIdWithUppercasePrefix() {
        GeneSymbol symbol = new GeneSymbol("HGNC:AB-123");
        testIfValid(symbol);
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new GeneSymbol("Hgnc:AB-123") );
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new GeneSymbol("hGnc:AB-123") );
    }

    @Test
    public void useValidIdWithInvalidPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new GeneSymbol("hg:AB-123") );
    }

    @Test
    public void useValidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new GeneSymbol("AB-123") );
    }

    @Test
    public void useUriAsIdInput() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new GeneSymbol("http://identifiers.org/hgnc.symbol/AB-123") );
    }

    @Test
    public void useValidUri() {
        GeneSymbol symbol = new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/AB-123"));
        testIfValid(symbol);
    }

    @Test
    public void useInvalidUri() {
        URI uri = URI.create("http://identifiers.org/hgnc/AB-123");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new GeneSymbol(uri) );
    }

    @Test
    public void testSort() {
        List<GeneSymbol> actualOrder = new ArrayList<>( Arrays.asList(
                new GeneSymbol("hgnc:KP3145"),
                new GeneSymbol("hgnc:AB-123"),
                new GeneSymbol("hgnc:CS44")
        ));

        List<GeneSymbol> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(1),
                actualOrder.get(2),
                actualOrder.get(0)
        ));

        Collections.sort(actualOrder);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(GeneSymbol symbol) {
        Assertions.assertAll(
                () -> Assertions.assertEquals("AB-123", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:AB-123", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/AB-123"), symbol.getUri())
        );
    }

    @Test
    public void testEqualsIdToEqualId() {
        Assertions.assertEquals(new GeneSymbol("hgnc:AB-123"), new GeneSymbol("hgnc:AB-123"));
    }

    @Test
    public void testEqualsUriToEqualUri() {
        Assertions.assertEquals(new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/AB-123")),
                new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/AB-123")));
    }

    @Test
    public void testEqualsIdToEqualUri() {
        Assertions.assertEquals(new GeneSymbol("hgnc:AB-123"),
                new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/AB-123")));
    }

    @Test
    public void testEqualsIdToDifferentId() {
        Assertions.assertNotEquals(new GeneSymbol("hgnc:AB-123"), new GeneSymbol("hgnc:CD-456"));
    }

    @Test
    public void useValidIdWithAtInNameCreatedThroughId() {
        GeneSymbol symbol = new GeneSymbol("hgnc:SAA@");

        Assertions.assertAll(
                () -> Assertions.assertEquals("SAA@", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:SAA@", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/SAA@"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdWithAtInNameCreatedThroughUri() {
        GeneSymbol symbol = new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/SAA@"));

        Assertions.assertAll(
                () -> Assertions.assertEquals("SAA@", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:SAA@", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/SAA@"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdContainingOrfInNameCreatedThroughId() {
        GeneSymbol symbol = new GeneSymbol("hgnc:C12orf65");

        Assertions.assertAll(
                () -> Assertions.assertEquals("C12orf65", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:C12orf65", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/C12orf65"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdContainingOrfInNameCreatedThroughUri() {
        GeneSymbol symbol = new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/C12orf65"));

        Assertions.assertAll(
                () -> Assertions.assertEquals("C12orf65", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:C12orf65", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/C12orf65"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdContainingSlashInNameCreatedThroughId() {
        GeneSymbol symbol = new GeneSymbol("hgnc:THRA1/BTR");

        Assertions.assertAll(
                () -> Assertions.assertEquals("THRA1/BTR", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:THRA1/BTR", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/THRA1/BTR"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdContainingSlashInNameCreatedThroughUri() {
        GeneSymbol symbol = new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/THRA1/BTR"));

        Assertions.assertAll(
                () -> Assertions.assertEquals("THRA1/BTR", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:THRA1/BTR", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/THRA1/BTR"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdContainingDotInNameCreatedThroughId() {
        GeneSymbol symbol = new GeneSymbol("hgnc:GS1-600G8.3");

        Assertions.assertAll(
                () -> Assertions.assertEquals("GS1-600G8.3", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:GS1-600G8.3", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/GS1-600G8.3"), symbol.getUri())
        );
    }

    @Test
    public void useValidIdContainingDotInNameCreatedThroughUri() {
        GeneSymbol symbol = new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/GS1-600G8.3"));

        Assertions.assertAll(
                () -> Assertions.assertEquals("GS1-600G8.3", symbol.getId()),
                () -> Assertions.assertEquals("hgnc:GS1-600G8.3", symbol.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/GS1-600G8.3"), symbol.getUri())
        );
    }
}
