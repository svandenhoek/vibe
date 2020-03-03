package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PhenotypeTester {
    @Test
    public void useValidIdWithLowercasePrefix() {
        Phenotype phenotype = new Phenotype("hp:0012345");
        testIfValid(phenotype);
    }

    @Test
    public void useValidIdWithUppercasePrefix() {
        Phenotype phenotype = new Phenotype("HP:0012345");
        testIfValid(phenotype);
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("Hp:0012345") );
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("hP:0012345") );
    }

    @Test
    public void useValidIdWithInvalidPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("ph:0012345") );
    }

    @Test
    public void useValidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("0012345") );
    }

    @Test
    public void useUriAsIdInput() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("http://purl.obolibrary.org/obo/HP_0012345") );
    }

    @Test
    public void useValidUri() {
        Phenotype phenotype = new Phenotype(URI.create("http://purl.obolibrary.org/obo/HP_0012345"));
        testIfValid(phenotype);
    }

    @Test
    public void useInvalidUri() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Phenotype(URI.create("http://purl.obolibrary.org/obo/id/HP_0012345")) );
    }

    @Test
    public void testSort() {
        List<Phenotype> actualOrder = new ArrayList<>( Arrays.asList(
                new Phenotype("hp:0000003"),
                new Phenotype("hp:0000008"),
                new Phenotype("hp:0000001")
        ));

        List<Phenotype> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(2),
                actualOrder.get(0),
                actualOrder.get(1)
        ));

        Collections.sort(actualOrder);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(Phenotype phenotype) {
        Assertions.assertAll(
                () -> Assertions.assertEquals("0012345", phenotype.getId()),
                () -> Assertions.assertEquals("hp:0012345", phenotype.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://purl.obolibrary.org/obo/HP_0012345"), phenotype.getUri())
        );
    }

    // Specific to Phenotypes as number of digits must be an exact number.
    @Test
    public void useTooShortPhenotypeIdWithPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("hp:0012") );
    }

    @Test
    public void useTooShortPhenotypeIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Phenotype("0012") );
    }
}
