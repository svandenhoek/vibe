package org.molgenis.vibe.io.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.TestData;

public class OntologyModelFilesReaderTester {
    private static String hpoFile;
    
    @BeforeAll
    public static void beforeAll() {
        hpoFile = TestData.HPO_OWL.getFullPath();
    }
    
    @Test
    public void checkIfModelIsNotEmpty() {
        OntologyModelFilesReader ontologyReader = new OntologyModelFilesReader(hpoFile);
        Assertions.assertEquals(false, ontologyReader.getModel().isEmpty());
    }
}
