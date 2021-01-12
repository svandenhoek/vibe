package org.molgenis.vibe.core.io.input;

import org.apache.jena.riot.RiotException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.TestData;

class OntologyModelFilesReaderIT {
    private OntologyModelFilesReader reader;

    @AfterEach
    void afterEach() {
        if(reader != null) {
            reader.close();
        }
    }

    @Test
    void testValidModel() {
        reader = new OntologyModelFilesReader(TestData.HPO_OWL.getFullPathString());
        Assertions.assertEquals(false, reader.getModel().isEmpty());
    }

    @Test
    void testInvalidFileFormat() {
        String inputFileString = TestData.EXISTING_TSV.getFullPathString();
        Assertions.assertThrows(RiotException.class, () -> new OntologyModelFilesReader(inputFileString));
    }

    @Test
    void testInvalidFileUsingCorrectFormat() {
        String inputFileString = TestData.FAKE_HPO_OWL.getFullPathString();
        Assertions.assertThrows(RiotException.class, () -> new OntologyModelFilesReader(inputFileString));
    }
}
