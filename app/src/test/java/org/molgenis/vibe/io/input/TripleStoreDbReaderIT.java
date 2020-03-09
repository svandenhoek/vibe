package org.molgenis.vibe.io.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.TestData;

import java.io.IOException;

public class TripleStoreDbReaderIT {
    private static String tdbDir;

    @BeforeAll
    public static void beforeAll() {
        tdbDir = TestData.TDB.getFullPath();
    }

    @Test
    public void checkIfModelIsNotEmpty() throws IOException {
        TripleStoreDbReader tdbReader = new TripleStoreDbReader(tdbDir);
        Assertions.assertEquals(false, tdbReader.getModel().isEmpty());
    }
}
