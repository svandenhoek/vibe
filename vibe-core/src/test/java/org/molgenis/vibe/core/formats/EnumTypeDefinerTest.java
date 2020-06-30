package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

class EnumTypeDefinerTest {
    @Test
    void phenotypesRetrieverFactoryChildren() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.CHILDREN, EnumTypeDefiner.retrieve("children", PhenotypesRetrieverFactory.class));
    }

    @Test
    void phenotypesRetrieverFactoryDistance() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.DISTANCE, EnumTypeDefiner.retrieve("distance", PhenotypesRetrieverFactory.class));
    }

    @Test
    void phenotypesRetrieverFactoryNonExistent() {
        Assertions.assertThrows(EnumConstantNotPresentException.class, () -> EnumTypeDefiner.retrieve("iDoNotExist", PhenotypesRetrieverFactory.class) );
    }

    @Test
    void enumDefinerDoubleNull() {
        Assertions.assertThrows(EnumConstantNotPresentException.class, () -> EnumTypeDefiner.retrieve(null, PhenotypesRetrieverFactory.class) );
    }

    @Test
    void enumDefinerNullClass() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> EnumTypeDefiner.retrieve("distance", null) );
    }

    @Test
    void enumDefinerDoubleNullId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> EnumTypeDefiner.retrieve(null, null) );
    }
}
