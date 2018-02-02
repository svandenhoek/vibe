package org.molgenis.vibe.rdf_querying;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SparqlRangeTester {
    @Test
    public void all() {
        Assert.assertEquals(new SparqlRange(0, true).toString(), "*");
    }

    @Test
    public void selfOnly1() {
        Assert.assertEquals(new SparqlRange(0).toString(), "{0}");
    }

    @Test
    public void selfOnly2() {
        Assert.assertEquals(new SparqlRange(0, false).toString(), "{0}");
    }

    @Test
    public void selfTillchildren2Deep() {
        Assert.assertEquals(new SparqlRange(2, false).toString(), "{,2}");
    }

    @Test
    public void children2DeepAndFurther() {
        Assert.assertEquals(new SparqlRange(2, true).toString(), "{2,}");
    }

    @Test
    public void allExceptSelf() {
        Assert.assertEquals(new SparqlRange(1, true).toString(), "+");
    }

    @Test
    public void selfAndDirectChildrenOnly1() {
        Assert.assertEquals(new SparqlRange(0, 1).toString(), "?");
    }

    @Test
    public void selfAndDirectChildrenOnly2() {
        Assert.assertEquals(new SparqlRange(1, false).toString(), "?");
    }
}
