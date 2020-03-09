package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.ontology_processing.PhenotypesRetrieverFactory;

public class EnumTypeDefinerTest {
    @Test
    public void phenotypesRetrieverFactoryChildren() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.CHILDREN, EnumTypeDefiner.retrieve("children", PhenotypesRetrieverFactory.class));
    }

    @Test
    public void phenotypesRetrieverFactoryDistance() {
        Assertions.assertEquals(PhenotypesRetrieverFactory.DISTANCE, EnumTypeDefiner.retrieve("distance", PhenotypesRetrieverFactory.class));
    }

    @Test
    public void phenotypesRetrieverFactoryNonExistent() {
        Assertions.assertThrows(EnumConstantNotPresentException.class, () -> EnumTypeDefiner.retrieve("iDoNotExist", PhenotypesRetrieverFactory.class) );
    }

    @Test
    public void enumDefinerDoubleNull() {
        Assertions.assertThrows(EnumConstantNotPresentException.class, () -> EnumTypeDefiner.retrieve(null, PhenotypesRetrieverFactory.class) );
    }

    @Test
    public void enumDefinerNullClass() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> EnumTypeDefiner.retrieve("distance", null) );
    }

    @Test
    public void enumDefinerDoubleNullId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> EnumTypeDefiner.retrieve(null, null) );
    }
}
