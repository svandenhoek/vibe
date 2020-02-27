package org.molgenis.vibe.formats;

import org.junit.Assert;
import org.junit.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneSymbolTester {
    @Test
    public void useValidIdWithLowercasePrefix() throws InvalidStringFormatException {
        GeneSymbol symbol = new GeneSymbol("hgnc:AB-123");
        testIfValid(symbol);
    }

    @Test
    public void useValidIdWithUppercasePrefix() throws InvalidStringFormatException {
        GeneSymbol symbol = new GeneSymbol("HGNC:AB-123");
        testIfValid(symbol);
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix1() throws InvalidStringFormatException {
        new GeneSymbol("Hgnc:AB-123");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithSingleUpperCasePrefix2() throws InvalidStringFormatException {
        new GeneSymbol("hGnc:AB-123");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithInvalidPrefix() throws InvalidStringFormatException {
        new GeneSymbol("hg:AB-123");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useValidIdWithoutPrefix() throws InvalidStringFormatException {
        new GeneSymbol("AB-123");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void useUriAsIdInput() throws InvalidStringFormatException {
        new GeneSymbol("http://identifiers.org/hgnc.symbol/AB-123");
    }

    @Test
    public void useValidUri() {
        GeneSymbol symbol = new GeneSymbol(URI.create("http://identifiers.org/hgnc.symbol/AB-123"));
        testIfValid(symbol);
    }

    @Test(expected = IllegalArgumentException.class)
    public void useInvalidUri() {
        new GeneSymbol(URI.create("http://identifiers.org/hgnc/AB-123"));
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
        Assert.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(GeneSymbol symbol) {
        Assert.assertEquals("AB-123", symbol.getId());
        Assert.assertEquals("hgnc:AB-123", symbol.getFormattedId());
        Assert.assertEquals(URI.create("http://identifiers.org/hgnc.symbol/AB-123"), symbol.getUri());
    }
}
