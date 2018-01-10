package org.molgenis.vibe.io.disgenet_rdf;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DisgenetGdaFileReaderTester {
    private DisgenetGdaFileReader reader;

    /**
     * ClassLoader object to view test resource files. Test files can be retrieved using {@code getResource()}, where an
     * empty {@link String} will refer to the folder {@code target/test-classes}.
     */
    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    @BeforeTest
    public void initialize() {
        reader = new DisgenetGdaFileReader();
        reader.read(classLoader.getResource("gda_SIO_001347.ttl").getFile());
    }

    @Test()
    public void test() {
    }
}
