package org.molgenis.vibe.core.io.input;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.molgenis.vibe.core.TestData;

import java.io.IOException;

@Execution(ExecutionMode.SAME_THREAD)
class TripleStoreDbReaderIT {
    private static TripleStoreDbReader tdbReader;

    @BeforeAll
    static void beforeAll() throws IOException {
        tdbReader = new TripleStoreDbReader(TestData.TDB.getFullPathString());
    }

    @AfterAll
    static void afterAll() {
        if(tdbReader != null) {
            tdbReader.close();
        }
    }

    @Test
    void checkIfModelIsNotEmpty() throws IOException {
        Assertions.assertEquals(false, tdbReader.getModel().isEmpty());
    }
}
