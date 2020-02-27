package org.molgenis.vibe.io.input;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.molgenis.vibe.TestData;

import java.io.IOException;

public class TripleStoreDbReaderTester {
    private static String tdbDir;

    @BeforeClass
    public static void beforeClass() {
        tdbDir = TestData.TDB.getDir();
    }

    @Test
    public void checkIfModelIsNotEmpty() throws IOException {
        TripleStoreDbReader tdbReader = new TripleStoreDbReader(tdbDir);
        Assert.assertEquals(false, tdbReader.getModel().isEmpty());
    }
}
