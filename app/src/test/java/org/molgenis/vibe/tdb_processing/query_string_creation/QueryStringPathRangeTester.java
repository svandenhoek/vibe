package org.molgenis.vibe.tdb_processing.query_string_creation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryStringPathRangeTester {
    @Test
    public void all() {
        Assertions.assertEquals("*", new QueryStringPathRange(0, true).toString());
    }

    @Test
    public void selfOnly1() {
        Assertions.assertEquals("{0}", new QueryStringPathRange(0).toString());
    }

    @Test
    public void selfOnly2() {
        Assertions.assertEquals("{0}", new QueryStringPathRange(0, false).toString());
    }

    @Test
    public void selfTillchildren2Deep() {
        Assertions.assertEquals("{,2}", new QueryStringPathRange(2, false).toString());
    }

    @Test
    public void children2DeepAndFurther() {
        Assertions.assertEquals("{2,}", new QueryStringPathRange(2, true).toString());
    }

    @Test
    public void allExceptSelf() {
        Assertions.assertEquals("+", new QueryStringPathRange(1, true).toString());
    }

    @Test
    public void selfAndDirectChildrenOnly1() {
        Assertions.assertEquals("?", new QueryStringPathRange(0, 1).toString());
    }

    @Test
    public void selfAndDirectChildrenOnly2() {
        Assertions.assertEquals("?", new QueryStringPathRange(1, false).toString());
    }
}
