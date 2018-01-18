package org.molgenis.vibe;

public enum TestFile {
    GDA1_RDF("gda.ttl"),
    GENE_RDF("gene.ttl"),
    DISEASE_DISEASE_RDF("disease-disease.ttl"),
    PHENOTYPE_RDF("phenotype.ttl"),
    PDA_RDF("pda.ttl"),
    ONTOLOGY("sio-release.owl");

    /**
     * ClassLoader object to view test resource files. Test files can be retrieved using {@code getResource()}, where an
     * empty {@link String} will refer to the folder {@code target/test-classes}.
     */
    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private String fileName;

    TestFile(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Checks if all test files are present.
     * @return {@code true} if all required test files are present, {@code false} if not.
     */
    public static boolean checkIfAllFilesArePresent() {
        for(TestFile value : TestFile.values()) {
            if(classLoader.getResource(value.getFileName()) == null) {
                return false;
            }
        }
        return true;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Returns full path to where the test file is located.
     * @return {@link String}
     * @exception NullPointerException If additional test files were not present in the resources folder before running
     * the tests (run the TestNGPreprocessing.sh BEFORE running any tests!)
     */
    @SuppressWarnings("unchecked")
    public String getFilePath() {
        //noinspection ConstantConditions
        return classLoader.getResource(fileName).getFile();
    }
}
