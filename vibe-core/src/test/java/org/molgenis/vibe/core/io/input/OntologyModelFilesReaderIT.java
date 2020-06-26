package org.molgenis.vibe.core.io.input;

import org.apache.jena.riot.RiotException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.TestData;

import java.nio.file.Path;

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
        reader = new OntologyModelFilesReader(TestData.HPO_OWL.getFullPathString());
        Assertions.assertEquals(false, reader.getModel().isEmpty());
    }

    @Test
    public void testInvalidFileFormat() {
        String inputFileString = TestData.EXISTING_TSV.getFullPathString();
        Assertions.assertThrows(RiotException.class, () -> new OntologyModelFilesReader(inputFileString));
    }

    @Test
    public void testInvalidFileUsingCorrectFormat() {
        String inputFileString = TestData.FAKE_HPO_OWL.getFullPathString();
        Assertions.assertThrows(RiotException.class, () -> new OntologyModelFilesReader(inputFileString));
    }
}
