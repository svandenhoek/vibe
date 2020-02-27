package org.molgenis.vibe.ontology_processing;


import org.junit.Assert;
import org.junit.Test;

public class PhenotypesRetrieverFactoryTester {
    @Test
    public void retrieveChildren() {
        Assert.assertEquals(PhenotypesRetrieverFactory.CHILDREN, PhenotypesRetrieverFactory.retrieve("children"));
    }

    @Test
    public void retrieveDistance() {
        Assert.assertEquals(PhenotypesRetrieverFactory.DISTANCE, PhenotypesRetrieverFactory.retrieve("distance"));
    }
}
