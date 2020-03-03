package org.molgenis.vibe.ontology_processing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PhenotypesRetrieverFactoryTester {
    @Test
    public void retrieveChildren() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.CHILDREN, PhenotypesRetrieverFactory.retrieve("children"));
    }

    @Test
    public void retrieveDistance() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.DISTANCE, PhenotypesRetrieverFactory.retrieve("distance"));
    }
}
