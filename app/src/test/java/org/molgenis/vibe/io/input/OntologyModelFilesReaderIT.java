package org.molgenis.vibe.io.input;

import org.apache.jena.riot.RiotException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.TestData;

public class OntologyModelFilesReaderIT {
    private OntologyModelFilesReader reader;

    @AfterEach
    public void afterEach() {
        if(reader != null) {
            reader.close();
        }
    }

    @Test
    public void testValidModel() {
        reader = new OntologyModelFilesReader(TestData.HPO_OWL.getFullPath());
        Assertions.assertEquals(false, reader.getModel().isEmpty());
    }

    @Test
    public void testInvalidFileFormat() {
        Assertions.assertThrows(RiotException.class, () -> new OntologyModelFilesReader(TestData.EXISTING_TSV.getFullPath()));
    }

    @Test
    public void testInvalidFileUsingCorrectFormat() {
        Assertions.assertThrows(RiotException.class, () -> new OntologyModelFilesReader(TestData.FAKE_HPO_OWL.getFullPath()));
    }
}
