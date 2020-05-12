package org.molgenis.vibe.core;

public enum TestData {
    HPO_OWL {
        @Override
        public String getName() {
            return "hp.owl";
        }
    },
    FAKE_HPO_OWL {
        @Override
        public String getName() {
            return "fake_hp.owl";
        }
    },
    TDB {
        @Override
        public String getName() {
            return "tdb";
        }
    },
    EXISTING_TSV {
        @Override
        public String getName() {
            return "output.tsv";
        }
    };

    /**
     * ClassLoader object to view test resource files. Test files can be retrieved using {@code getResource()}, where an
     * empty {@link String} will refer to the folder {@code target/test-classes}.
     */
    protected final String mainDir = Thread.currentThread().getContextClassLoader().getResource("").getFile();

    /**
     * Returns the full path of the test resource.
     * @return {@link String}
     */
    public String getFullPath() {
        return mainDir + getName();
    }

    /**
     * Returns the name of the test resource.
     * @return {@link String}
     */
    public abstract String getName();
}
