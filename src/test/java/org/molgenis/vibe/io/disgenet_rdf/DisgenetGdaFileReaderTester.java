package org.molgenis.vibe.io.disgenet_rdf;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DisgenetGdaFileReaderTester extends Tester {
    private DisgenetGdaFileReader reader;

    @BeforeTest
    public void initialize() {
        reader = new DisgenetGdaFileReader();
        reader.read(getClassLoader().getResource("gda_SIO_001347.ttl").getFile());
    }

    @Test()
    public void test() {
    }
}
