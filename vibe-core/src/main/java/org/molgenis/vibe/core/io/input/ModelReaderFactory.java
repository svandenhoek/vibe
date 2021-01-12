package org.molgenis.vibe.core.io.input;

import java.io.IOException;
import java.nio.file.Path;

public enum ModelReaderFactory {
    TDB {
        @Override
        public ModelReader readDatabase(Path file) throws IOException {
            return new TripleStoreDbReader(file);
        }
    },
    HDT {
        @Override
        public ModelReader readDatabase(Path file) throws IOException {
            return new HdtFileReader(file);
        }
    };

    public abstract ModelReader readDatabase(Path file) throws IOException;
}
