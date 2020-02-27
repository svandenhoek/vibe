package org.molgenis.vibe.formats;

import org.junit.Assert;
import org.junit.Test;
import org.molgenis.vibe.ontology_processing.PhenotypesRetrieverFactory;

public class EnumTypeDefinerTester {
    @Test
    public void phenotypesRetrieverFactoryChildren() {
        Assert.assertEquals(PhenotypesRetrieverFactory.CHILDREN, EnumTypeDefiner.retrieve("children", PhenotypesRetrieverFactory.class));
    }

    @Test
    public void phenotypesRetrieverFactoryDistance() {
        Assert.assertEquals(PhenotypesRetrieverFactory.DISTANCE, EnumTypeDefiner.retrieve("distance", PhenotypesRetrieverFactory.class));
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void phenotypesRetrieverFactoryNonExistent() {
        EnumTypeDefiner.retrieve("iDoNotExist", PhenotypesRetrieverFactory.class);
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void enumDefinerDoubleNull() {
        EnumTypeDefiner.retrieve(null, PhenotypesRetrieverFactory.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void enumDefinerNullClass() {
        EnumTypeDefiner.retrieve("distance", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void enumDefinerDoubleNullId() {
        EnumTypeDefiner.retrieve(null, null);
    }
}
