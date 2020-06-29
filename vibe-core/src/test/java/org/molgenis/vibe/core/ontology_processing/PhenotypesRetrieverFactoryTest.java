package org.molgenis.vibe.core.ontology_processing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PhenotypesRetrieverFactoryTest {
    @Test
    void retrieveChildren() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.CHILDREN, PhenotypesRetrieverFactory.retrieve("children"));
    }

    @Test
    void retrieveDistance() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.DISTANCE, PhenotypesRetrieverFactory.retrieve("distance"));
    }
}
