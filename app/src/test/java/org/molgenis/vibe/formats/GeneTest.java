package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneTest {
    private static final GeneSymbol symbol = new GeneSymbol("hgnc:ABC");

    @Test
    public void useValidIdWithLowercasePrefix() {
        Gene gene = new Gene("ncbigene:1234", symbol);
        testIfValid(gene);
    }

    @Test
    public void useValidIdWithUppercasePrefix() {
        Gene gene = new Gene("NCBIGENE:1234", symbol);
        testIfValid(gene);
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("Ncbigene:1234", symbol) );
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("nCbigene:1234", symbol) );
    }

    @Test
    public void useValidIdWithInvalidPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("ncbi:1234", symbol) );
    }

    @Test
    public void useValidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("1234", symbol) );
    }

    @Test
    public void useUriAsIdInput() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("http://identifiers.org/ncbigene/1234", symbol) );
    }

    @Test
    public void useValidUri() {
        Gene gene = new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol);
        testIfValid(gene);
    }

    @Test
    public void useInvalidUri() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Gene(URI.create("http://identifiers.org/ncbi/1234"), symbol) );
    }

    @Test
    public void testSort() {
        List<Gene> actualOrder = new ArrayList<>( Arrays.asList(
                new Gene("ncbigene:3", symbol),
                new Gene("ncbigene:8", symbol),
                new Gene("ncbigene:1", symbol)
        ));

        List<Gene> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(2),
                actualOrder.get(0),
                actualOrder.get(1)
        ));

        Collections.sort(actualOrder);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(Gene gene) {
        Assertions.assertAll(
                () -> Assertions.assertEquals("1234", gene.getId()),
                () -> Assertions.assertEquals("ncbigene:1234", gene.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/ncbigene/1234"), gene.getUri())
        );
    }

    @Test
    public void testEqualsIdToEqualId() {
        Assertions.assertTrue(new Gene("ncbigene:1234", symbol).equals(new Gene("ncbigene:1234", symbol)));
    }

    @Test
    public void testEqualsUriToEqualUri() {
        Assertions.assertTrue(new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol).equals(new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol)));
    }

    @Test
    public void testEqualsIdToEqualUri() {
        Assertions.assertTrue(new Gene("ncbigene:1234", symbol).equals(new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol)));
    }

    @Test
    public void testEqualsIdToDifferentId() {
        Assertions.assertFalse(new Gene("ncbigene:1234", symbol).equals(new Gene("ncbigene:5678", symbol)));
    }
}
