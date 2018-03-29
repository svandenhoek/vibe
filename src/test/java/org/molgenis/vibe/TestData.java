package org.molgenis.vibe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum TestData {
    TTL("disgenet_mini") {
        @Override
        public String[] getFiles() {
            return filterFileArray(super.getFiles(), ".ttl", ".owl");
        }
    },
    TTL_NO_ONTOLOGY("disgenet_mini") {
        @Override
        public String[] getFiles() {
            return filterFileArray(super.getFiles(), ".ttl");
        }
    },
    TDB_MINI_NO_ONTOLOGY("disgenet_mini_tdb_no_ontology"),
    TDB_MINI("disgenet_mini_tdb"),
    TDB_FULL("disgenet_full_tdb");

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
     * (run the TestNGPreprocessing.sh BEFORE running any tests!)
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

    private static String[] filterFileArray(String[] fileNames, String... fileTypeFilter) {
        List<String> filteredFileNames = new ArrayList<>();

        for(int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            for(int j = 0; j < fileTypeFilter.length; j++) {
                if( fileName.endsWith(fileTypeFilter[j]) ) {
                    filteredFileNames.add(fileName);
                }
            }
        }

        return filteredFileNames.toArray(new String[filteredFileNames.size()]);
    }
}
