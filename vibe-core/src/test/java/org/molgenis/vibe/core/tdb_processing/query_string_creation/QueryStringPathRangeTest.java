package org.molgenis.vibe.core.tdb_processing.query_string_creation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class QueryStringPathRangeTest {
    @Test
    void all() {
        Assertions.assertEquals("*", new QueryStringPathRange(0, true).toString());
    }

    @Test
    void selfOnly1() {
        Assertions.assertEquals("{0}", new QueryStringPathRange(0).toString());
    }

    @Test
    void selfOnly2() {
        Assertions.assertEquals("{0}", new QueryStringPathRange(0, false).toString());
    }

    @Test
    void selfTillchildren2Deep() {
        Assertions.assertEquals("{,2}", new QueryStringPathRange(2, false).toString());
    }

    @Test
    void children2DeepAndFurther() {
        Assertions.assertEquals("{2,}", new QueryStringPathRange(2, true).toString());
    }

    @Test
    void allExceptSelf() {
        Assertions.assertEquals("+", new QueryStringPathRange(1, true).toString());
    }

    @Test
    void selfAndDirectChildrenOnly1() {
        Assertions.assertEquals("?", new QueryStringPathRange(0, 1).toString());
    }

    @Test
    void selfAndDirectChildrenOnly2() {
        Assertions.assertEquals("?", new QueryStringPathRange(1, false).toString());
    }
}
