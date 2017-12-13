package org.molgenis.vibe.options_digestion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract class to be used for options parsing. Includes some basic validations (such as whether input arguments refer
 * to actual files).
 */
public abstract class OptionsParser {
    /**
     * The DisGeNET RDF data dump (retrievable from http://rdf.disgenet.org/download/).
     */
    private Path disgenetDump;

    /**
     * The rVCF file that needs to be digested.
     */
    private Path rvcfData;

    public Path getDisgenetDump() {
        return disgenetDump;
    }

    /**
     * @param disgenetDump {@link String}
     * @throws InvalidPathException if {@link Paths#get(String, String...)}} fails to convert a {@link String} to {@link Path}
     * @throws IOException see {@link #setDisgenetDump(Path)}
     */
    void setDisgenetDump(String disgenetDump) throws InvalidPathException, IOException {
        setDisgenetDump(Paths.get(disgenetDump));
    }


    /**
     * @param disgenetDump {@link Path}
     * @throws IOException if {@code disgenetDump} is not a readable file.
     */
    void setDisgenetDump(Path disgenetDump) throws IOException {
        if(checkIfPathIsReadableFile(disgenetDump)) {
            this.disgenetDump = disgenetDump;
        } else {
            throw new IOException(disgenetDump.getFileName() + " is not a readable file.");
        }
    }

    public Path getRvcfData() {
        return rvcfData;
    }

    /**
     * @param rvcfData {@link String}
     * @throws InvalidPathException if {@link Paths#get(String, String...)}} fails to convert a {@link String} to {@link Path}
     * @throws IOException see {@link #setRvcfData(Path)}
     */
    void setRvcfData(String rvcfData) throws InvalidPathException, IOException {
        setRvcfData(Paths.get(rvcfData));
    }

    /**
     * @param rvcfData {@link Path}
     * @throws IOException if {@code rvcfData} is not a readable file.
     */
    void setRvcfData(Path rvcfData) throws IOException {
        if(checkIfPathIsReadableFile(rvcfData)) {
            this.rvcfData = rvcfData;
        } else {
            throw new IOException(rvcfData.getFileName() + " is not a readable file.");
        }
    }

    /**
     * Checks if a given {@link Path} is an existing file.
     * @param fileLocation {@link Path}
     * @return {@code boolean} {@code true} if so, otherwise {@code false}
     */
    private boolean checkIfPathIsFile(Path fileLocation)
    {
        return Files.exists(fileLocation) && Files.isRegularFile(fileLocation);
    }

    /**
     * Checks if a given {@link Path} is an existing readable file.
     * @param fileLocation {@link Path}
     * @return {@code boolean} {@code true} if so, otherwise {@code false}
     */
    private boolean checkIfPathIsReadableFile(Path fileLocation) {
        return checkIfPathIsFile(fileLocation) && Files.isReadable(fileLocation);
    }
}

