package org.molgenis.vibe.io.disgenet_rdf;

import org.molgenis.vibe.TestFile;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DisgenetGdaFileReaderTester {
    private DisgenetGdaFileReader reader;

    @BeforeTest
    public void initialize() {
        reader = new DisgenetGdaFileReader();
        reader.read(TestFile.GDA1_RDF.getFilePath());
    }

    @Test()
    public void test() {
    }
}
