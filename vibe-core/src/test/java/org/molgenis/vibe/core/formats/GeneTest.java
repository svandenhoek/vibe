package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class GeneTest {
    private static final GeneSymbol symbol = new GeneSymbol("hgnc:ABC");

    @Test
    void useValidIdWithLowercasePrefix() {
        Gene gene = new Gene("ncbigene:1234", symbol);
        testIfValid(gene);
    }

    @Test
    void useValidIdWithUppercasePrefix() {
        Gene gene = new Gene("NCBIGENE:1234", symbol);
        testIfValid(gene);
    }

    @Test
    void useValidIdWithSingleUpperCasePrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("Ncbigene:1234", symbol) );
    }

    @Test
    void useValidIdWithSingleUpperCasePrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("nCbigene:1234", symbol) );
    }

    @Test
    void useValidIdWithInvalidPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("ncbi:1234", symbol) );
    }

    @Test
    void useValidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("1234", symbol) );
    }

    @Test
    void useUriAsIdInput() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Gene("http://identifiers.org/ncbigene/1234", symbol) );
    }

    @Test
    void useValidUri() {
        Gene gene = new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol);
        testIfValid(gene);
    }

    @Test
    void useInvalidUri() {
        URI uri = URI.create("http://identifiers.org/ncbi/1234");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Gene(uri, symbol) );
    }

    @Test
    void testSort() {
        List<Gene> actualOrder = new ArrayList<>( Arrays.asList(
                new Gene("ncbigene:20", symbol),
                new Gene("ncbigene:3", symbol),
                new Gene("ncbigene:8", symbol),
                new Gene("ncbigene:1", symbol)
        ));

        List<Gene> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(3),
                actualOrder.get(1),
                actualOrder.get(2),
                actualOrder.get(0)
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
    void testEqualsIdToEqualId() {
        Assertions.assertEquals(new Gene("ncbigene:1234", symbol), new Gene("ncbigene:1234", symbol));
    }

    @Test
    void testEqualsUriToEqualUri() {
        Assertions.assertEquals(new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol),
                new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol));
    }

    @Test
    void testEqualsIdToEqualUri() {
        Assertions.assertEquals(new Gene("ncbigene:1234", symbol),
                new Gene(URI.create("http://identifiers.org/ncbigene/1234"), symbol));
    }

    @Test
    void testEqualsIdToDifferentId() {
        Assertions.assertNotEquals(new Gene("ncbigene:1234", symbol), new Gene("ncbigene:5678", symbol));
    }

    @Test
    void testAllEqualsEqual() {
        Gene gene1 = new Gene("ncbigene:42", new GeneSymbol("hgnc:ABC"));
        Gene gene2 = new Gene("ncbigene:42", new GeneSymbol("hgnc:ABC"));

        Assertions.assertAll(
                () -> Assertions.assertEquals(gene1, gene2),
                () -> Assertions.assertTrue(gene1.allFieldsEquals(gene2))
        );
    }

    /**
     * While {@link Gene#allFieldsEquals(Object)} should not return {@code false} if {@link Gene#equals(Object)},
     * this test ensures the custom deep equals works correctly for usage in other tests.
     */
    @Test
    void testAllEqualsNotEqual() {
        Gene gene1 = new Gene("ncbigene:42", new GeneSymbol("hgnc:ABC"));
        Gene gene2 = new Gene("ncbigene:42", new GeneSymbol("hgnc:DEF"));

        Assertions.assertAll(
                () -> Assertions.assertEquals(gene1, gene2),
                () -> Assertions.assertFalse(gene1.allFieldsEquals(gene2))
        );
    }
}
