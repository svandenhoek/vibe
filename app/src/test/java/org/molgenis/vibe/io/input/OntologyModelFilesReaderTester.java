package org.molgenis.vibe.io.input;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.vibe.TestData;

public class OntologyModelFilesReaderTester {
    private static String hpoFile;
    
    @BeforeClass
    public static void beforeClass() {
        hpoFile = TestData.HPO_OWL.getFiles()[0];
    }
    
    @Test
    public void checkIfModelIsNotEmpty() {
        OntologyModelFilesReader ontologyReader = new OntologyModelFilesReader(hpoFile);
        Assert.assertEquals(false, ontologyReader.getModel().isEmpty());
    }
}
