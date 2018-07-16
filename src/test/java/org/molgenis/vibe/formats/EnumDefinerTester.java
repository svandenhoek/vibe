package org.molgenis.vibe.formats;

import org.molgenis.vibe.ontology_processing.PhenotypesRetrieverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EnumDefinerTester {
    @Test
    public void phenotypesRetrieverFactoryChildren() {
        Assert.assertEquals(EnumTypeDefiner.retrieve("children", PhenotypesRetrieverFactory.class), PhenotypesRetrieverFactory.CHILDREN);
    }

    @Test
    public void phenotypesRetrieverFactoryDistance() {
        Assert.assertEquals(EnumTypeDefiner.retrieve("distance", PhenotypesRetrieverFactory.class), PhenotypesRetrieverFactory.DISTANCE);
    }

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void phenotypesRetrieverFactoryNonExistent() {
        EnumTypeDefiner.retrieve("iDoNotExist", PhenotypesRetrieverFactory.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void enumDefinerDoubleNullId() {
        EnumTypeDefiner.retrieve(null, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void enumDefinerNullClass() {
        EnumTypeDefiner.retrieve("distance", null);
    }

    @Test(expectedExceptions = EnumConstantNotPresentException.class)
    public void enumDefinerDoubleNull() {
        EnumTypeDefiner.retrieve(null, PhenotypesRetrieverFactory.class);
    }
}
