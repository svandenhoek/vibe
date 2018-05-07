package org.molgenis.vibe.ontology_processing;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PhenotypesRetrieverFactoryTester {
    @Test
    public void retrieveChildren() {
        Assert.assertEquals(PhenotypesRetrieverFactory.retrieve("children"), PhenotypesRetrieverFactory.CHILDREN);
    }

    @Test
    public void retrieveDistance() {
        Assert.assertEquals(PhenotypesRetrieverFactory.retrieve("distance"), PhenotypesRetrieverFactory.DISTANCE);
    }
}
