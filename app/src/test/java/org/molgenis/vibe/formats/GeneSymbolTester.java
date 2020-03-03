package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneSymbolTester {
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> new GeneSymbol(URI.create("http://identifiers.org/hgnc/AB-123")) );
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
}
