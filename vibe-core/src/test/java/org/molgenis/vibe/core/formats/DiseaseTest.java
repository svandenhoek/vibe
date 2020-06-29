package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class DiseaseTest {
    @Test
    void useValidIdWithLowercasePrefix() {
        Disease disease = new Disease("umls:C0123456");
        testIfValid(disease);
    }

    @Test
    void useValidIdWithUppercasePrefix() {
        Disease disease = new Disease("UMLS:C0123456");
        testIfValid(disease);
    }

    @Test
    void useValidIdWithSingleUpperCasePrefix1() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Disease("Umls:C0123456") );
    }

    @Test
    void useValidIdWithSingleUpperCasePrefix2() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Disease("uMls:C0123456") );
    }

    @Test
    void useValidIdWithInvalidPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Disease("ulms:C0123456") );
    }

    @Test
    void useValidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Disease("C0123456") );
    }

    @Test
    void useUriAsIdInput() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new Disease("http://linkedlifedata.com/resource/umls/id/C0123456") );
    }

    @Test
    void useValidUri() {
        Disease disease = new Disease(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456"));
        testIfValid(disease);
    }

    @Test
    void useInvalidUri() {
        URI uri = URI.create("http://linkedlifedata.com/resource/umls/C0123456");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Disease(uri) );
    }

    @Test
    void testSort() {
        List<Disease> actualOrder = new ArrayList<>( Arrays.asList(
                new Disease("umls:C0000020"),
                new Disease("umls:C0000003"),
                new Disease("umls:C0000008"),
                new Disease("umls:C0000001")
        ));

        List<Disease> expectedOrder = new ArrayList<>( Arrays.asList(
                actualOrder.get(3),
                actualOrder.get(1),
                actualOrder.get(2),
                actualOrder.get(0)
        ));

        Collections.sort(actualOrder);
        Assertions.assertEquals(expectedOrder, actualOrder);
    }

    private void testIfValid(Disease disease) {
        Assertions.assertAll(
                () -> Assertions.assertEquals("C0123456", disease.getId()),
                () -> Assertions.assertEquals("umls:C0123456", disease.getFormattedId()),
                () -> Assertions.assertEquals(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456"), disease.getUri())
        );
    }

    @Test
    void testEqualsIdToEqualId() {
        Assertions.assertEquals(new Disease("umls:C0123456"), new Disease("umls:C0123456"));
    }

    @Test
    void testEqualsUriToEqualUri() {
        Assertions.assertEquals(new Disease(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456")),
                new Disease(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456")));
    }

    @Test
    void testEqualsIdToEqualUri() {
        Assertions.assertEquals(new Disease("umls:C0123456"),
                new Disease(URI.create("http://linkedlifedata.com/resource/umls/id/C0123456")));
    }

    @Test
    void testEqualsIdToDifferentId() {
        Assertions.assertNotEquals(new Disease("umls:C0123456"),
                new Disease("umls:C9874565"));
    }

    @Test
    void testAllEqualsEqual() {
        Disease disease1 = new Disease("umls:C0123456", "a disease name");
        Disease disease2 = new Disease("umls:C0123456", "a disease name");

        Assertions.assertAll(
                () -> Assertions.assertEquals(disease1, disease2),
                () -> Assertions.assertTrue(disease1.allFieldsEquals(disease2))
        );
    }

    /**
     * While {@link Disease#allFieldsEquals(Object)} should not return {@code false} if {@link Disease#equals(Object)},
     * this test ensures the custom deep equals works correctly for usage in other tests.
     */
    @Test
    void testAllEqualsNotEqual() {
        Disease disease1 = new Disease("umls:C0123456", "a disease name");
        Disease disease2 = new Disease("umls:C0123456", "a different disease name");

        Assertions.assertAll(
                () -> Assertions.assertEquals(disease1, disease2),
                () -> Assertions.assertFalse(disease1.allFieldsEquals(disease2))
        );
    }
}
