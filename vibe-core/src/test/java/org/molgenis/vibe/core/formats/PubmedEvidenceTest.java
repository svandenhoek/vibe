package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PubmedEvidenceTest {
    @Test
    public void useValidIdWithLowercasePrefix() {
        PubmedEvidence pubmedEvidence = new PubmedEvidence("pmid:1234", 2000);
        testIfValid(pubmedEvidence);
    }

    @Test
    public void useValidIdWithUppercasePrefix() {
        PubmedEvidence pubmedEvidence = new PubmedEvidence("PMID:1234", 2000);
        testIfValid(pubmedEvidence);
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new PubmedEvidence("Pmid:1234", 2000) );
    }

    @Test
    public void useValidIdWithSingleUpperCasePrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new PubmedEvidence("pMid:1234", 2000) );
    }

    @Test
    public void useValidIdWithInvalidPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new PubmedEvidence("pm:1234", 2000) );
    }

    @Test
    public void useValidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new PubmedEvidence("1234", 2000) );
    }

    @Test
    public void useUriAsIdInput() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new PubmedEvidence("http://identifiers.org/pubmed/1234", 2000) );
    }

    @Test
    public void useValidUri() {
        PubmedEvidence pubmedEvidence = new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1234"), 2000);
        testIfValid(pubmedEvidence);
    }

    @Test
    public void useInvalidUri() {
        URI uri = URI.create("http://identifiers.org/pm/1234");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PubmedEvidence(uri, 2000) );
    }

    @Test
    public void testSortId() {
        List<PubmedEvidence> actualOrder = new ArrayList<>( Arrays.asList(
                new PubmedEvidence("pmid:20", 2002),
                new PubmedEvidence("pmid:3", 2004),
                new PubmedEvidence("pmid:8", 2001),
                new PubmedEvidence("pmid:1", 2003)
        ));

        List<PubmedEvidence> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(3),
                actualOrder.get(1),
                actualOrder.get(2),
                actualOrder.get(0)
        ));

        Collections.sort(actualOrder);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void testSortYear() {
        List<PubmedEvidence> actualOrder = new ArrayList<>( Arrays.asList(
                new PubmedEvidence("pmid:20", 2002),
                new PubmedEvidence("pmid:3", 2004),
                new PubmedEvidence("pmid:8", 2001),
                new PubmedEvidence("pmid:1", 2003)
        ));

        List<PubmedEvidence> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(1),
                actualOrder.get(3),
                actualOrder.get(0),
                actualOrder.get(2)
        ));

        Collections.sort(actualOrder, PubmedEvidence.RELEASE_YEAR_COMPARATOR);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(PubmedEvidence pubmedEvidence) {
        Assertions.assertAll(
                () -> Assertions.assertEquals("1234", pubmedEvidence.getId()),
                () -> Assertions.assertEquals("pmid:1234", pubmedEvidence.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://identifiers.org/pubmed/1234"), pubmedEvidence.getUri())
        );
    }

    @Test
    public void testEqualsIdToEqualId() {
        Assertions.assertEquals(new PubmedEvidence("pmid:1234", 2000), new PubmedEvidence("pmid:1234", 2000));
    }

    @Test
    public void testEqualsUriToEqualUri() {
        Assertions.assertEquals(new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1234"), 2000),
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1234"), 2000));
    }

    @Test
    public void testEqualsIdToEqualUri() {
        Assertions.assertEquals(new PubmedEvidence("pmid:1234", 2000),
                new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1234"), 2000));
    }

    @Test
    public void testEqualsIdToDifferentId() {
        Assertions.assertNotEquals(new PubmedEvidence("pmid:1234", 2000),
                new PubmedEvidence("pmid:5678", 2000));
    }

    @Test
    public void testAllEqualsEqual() {
        PubmedEvidence pubmedEvidence1 = new PubmedEvidence("pmid:42", 2000);
        PubmedEvidence pubmedEvidence2 = new PubmedEvidence("pmid:42", 2000);

        Assertions.assertAll(
                () -> Assertions.assertEquals(pubmedEvidence1, pubmedEvidence2),
                () -> Assertions.assertTrue(pubmedEvidence1.allFieldsEquals(pubmedEvidence2))
        );
    }

    /**
     * While {@link PubmedEvidence#allFieldsEquals(Object)} should not return {@code false} if {@link PubmedEvidence#equals(Object)},
     * this test ensures the custom deep equals works correctly for usage in other tests.
     */
    @Test
    public void testAllEqualsNotEqual() {
        PubmedEvidence pubmedEvidence1 = new PubmedEvidence("pmid:42", 2000);
        PubmedEvidence pubmedEvidence2 = new PubmedEvidence("pmid:42", 2020);

        Assertions.assertAll(
                () -> Assertions.assertEquals(pubmedEvidence1, pubmedEvidence2),
                () -> Assertions.assertFalse(pubmedEvidence1.allFieldsEquals(pubmedEvidence2))
        );
    }
}
