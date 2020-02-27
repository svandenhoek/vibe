package org.molgenis.vibe.tdb_processing.query_string_creation;

import org.junit.Assert;
import org.junit.Test;

public class QueryStringPathRangeTester {
    @Test
    public void all() {
        Assert.assertEquals("*", new QueryStringPathRange(0, true).toString());
    }

    @Test
    public void selfOnly1() {
        Assert.assertEquals("{0}", new QueryStringPathRange(0).toString());
    }

    @Test
    public void selfOnly2() {
        Assert.assertEquals("{0}", new QueryStringPathRange(0, false).toString());
    }

    @Test
    public void selfTillchildren2Deep() {
        Assert.assertEquals("{,2}", new QueryStringPathRange(2, false).toString());
    }

    @Test
    public void children2DeepAndFurther() {
        Assert.assertEquals("{2,}", new QueryStringPathRange(2, true).toString());
    }

    @Test
    public void allExceptSelf() {
        Assert.assertEquals("+", new QueryStringPathRange(1, true).toString());
    }

    @Test
    public void selfAndDirectChildrenOnly1() {
        Assert.assertEquals("?", new QueryStringPathRange(0, 1).toString());
    }

    @Test
    public void selfAndDirectChildrenOnly2() {
        Assert.assertEquals("?", new QueryStringPathRange(1, false).toString());
    }
}
