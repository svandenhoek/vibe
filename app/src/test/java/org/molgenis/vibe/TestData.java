package org.molgenis.vibe;

import java.io.File;

public enum TestData {
    HPO_OWL("") {
        @Override
        public String[] getFiles() {
            return new String[]{getDir() + "hp.owl"};
        }
    },
    TDB("tdb/"),
    NON_EXISTING("") {
        @Override
        public String[] getFiles() {
            return new String[]{super.getDir() + "myNonExistingFile.txt"};
        }

        @Override
        public String getDir() {
            return super.getDir() + "nonExistingDir/";
        }
    },
    EXISTING_TSV("") {
        @Override
        public String[] getFiles() {
            return new String[]{super.getDir() + "output.tsv"};
        }
    };

    /**
     * ClassLoader object to view test resource files. Test files can be retrieved using {@code getResource()}, where an
     * empty {@link String} will refer to the folder {@code target/test-classes}.
     */
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private String dir;

    TestData(String dir) {
        try {
            this.dir = classLoader.getResource(dir).getFile();
        } catch(NullPointerException e) {
            this.dir = null;
        }
    }

    /**
     * Returns full path to where the test directory is located.
     * @return {@link String}
     * @exception NullPointerException if directories were not present in the resources folder before running the tests
     */
    @SuppressWarnings("unchecked")
    public String getDir() {
        //noinspection ConstantConditions
        return dir;
    }

    public String[] getFiles() {
        String[] fileNames = new File(getDir()).list();
        for(int i = 0; i < fileNames.length; i++) {
            fileNames[i] = getDir() + "/" + fileNames[i];
        }
        return fileNames;
    }
}
