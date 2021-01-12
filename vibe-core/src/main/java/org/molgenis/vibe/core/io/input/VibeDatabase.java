package org.molgenis.vibe.core.io.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 * A vibe database that can be accessed through retrieving the {@link ModelReader}. Also validates the database &
 * accessibility on the file system before it is actually loaded through the {@link ModelReader}.
 */
public class VibeDatabase {
    private Path dbPath;
    private ModelReaderFactory modelReaderFactory;

    public VibeDatabase(Path dbPath, ModelReaderFactory modelReaderFactory) throws IOException {
        this.dbPath = requireNonNull(dbPath);
        this.modelReaderFactory = requireNonNull(modelReaderFactory);
        validate();
    }

    public ModelReader getModelReader() throws IOException {
        return modelReaderFactory.readDatabase(dbPath);
    }

    private void validate() throws IOException {
        switch (modelReaderFactory) {
            case TDB:
                if( !(Files.isDirectory(dbPath) && Files.isReadable(dbPath)) ) {
                    throw new IOException("Not a valid TDB.");
                }
                break;
            case HDT:
                // Checks if it is a readable HDT file.
                if(!(Files.isRegularFile(dbPath) &&
                        Files.isReadable(dbPath) &&
                        dbPath.toString().toLowerCase().endsWith(".hdt"))) {
                    throw new IOException("Invalid database. Please check if it is a readable .hdt file.");
                }

                // If directory is not writable, pre-made index file is required.
                if(!Files.isWritable(dbPath.getParent())) {
                    // Name is index file belonging to HDT database file.
                    Path indexFile = Paths.get(dbPath.toString() + ".index.v1-1");
                    if(!(Files.isRegularFile(indexFile) && Files.isReadable(indexFile))) {
                        throw new IOException("Read-only directories require pre-made index file.");
                    }
                }
                break;
        }
    }
}
