package org.molgenis.vibe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public enum TestFilesDir {
    TTL("disgenet_mini") {
        @Override
        public String[] getFiles() {
            String[] fileNames = super.getFiles();
            List<String> filteredFileNames = new ArrayList<>();
            for(int i = 0; i < fileNames.length; i++) {
                String fileName = fileNames[i];
                if(fileName.endsWith(".ttl") || fileName.endsWith(".owl")) {
                    filteredFileNames.add(fileName);
                }
            }

            return filteredFileNames.toArray(new String[filteredFileNames.size()]);
//            return Arrays.stream(super.getFiles()).filter(x -> x.endsWith(".ttl") || x.endsWith(".owl")).toArray(String[]::new);
        }
    },
    TDB_MINI("disgenet_mini_tdb"),
    TDB_FULL("disgenet_full_tdb");

    /**
     * ClassLoader object to view test resource files. Test files can be retrieved using {@code getResource()}, where an
     * empty {@link String} will refer to the folder {@code target/test-classes}.
     */
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    private String dir;

    TestFilesDir(String dir) {
        this.dir = classLoader.getResource(dir).getFile();
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
}
