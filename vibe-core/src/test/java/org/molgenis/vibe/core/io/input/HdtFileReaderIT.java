package org.molgenis.vibe.core.io.input;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.TestData;

import java.io.IOException;

public class HdtFileReaderIT {
    private static HdtFileReader reader;

    @BeforeAll
    static void beforeAll() throws IOException {
        reader = new HdtFileReader(TestData.HDT.getFullPathString());
    }

    @AfterAll
    static void afterAll() {
        reader.close();
    }

    @Test
    void checkIfModelIsNotEmpty() {
        Assertions.assertFalse(reader.getModel().isEmpty());
    }
}
