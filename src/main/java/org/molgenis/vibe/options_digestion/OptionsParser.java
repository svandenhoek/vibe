package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

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
     * Path to the directory storing all required files for creating a SPARQL searchable DisGeNET model
     * (retrievable from http://rdf.disgenet.org/download/ and http://semanticscience.org/ontology/sio.owl).
     */
    private Path disgenetDataDir;

    /**
     * The DisGeNET RDF version.
     */
    private DisgenetRdfVersion disgenetRdfVersion;

    /**
     * The rVCF file that needs to be digested.
     */
    private Path rvcfData;

    public Path getDisgenetDataDir() {
        return disgenetDataDir;
    }

    /**
     * @param disgenetDataDir a {@link String} containing the path to the directory containing the data required to
     *                       create a model from the DisGeNET data
     * @throws InvalidPathException if {@link Paths#get(String, String...)}} fails to convert a {@link String} to {@link Path}
     * @throws IOException see {@link #setDisgenetDataDir(Path)}
     */
    void setDisgenetDataDir (String disgenetDataDir) throws InvalidPathException, IOException {
        setDisgenetDataDir(Paths.get(disgenetDataDir));
    }

    /**
     * @param disgenetDataDir a {@link Path} containing the path to the directory containing the data required to
     *                       create a model from the DisGeNET data
     * @throws IOException if {@code disgenetDump} is not a readable file
     */
    void setDisgenetDataDir(Path disgenetDataDir) throws IOException {
        if(checkIfPathIsReadableFile(disgenetDataDir)) {
            this.disgenetDataDir = disgenetDataDir;
        } else {
            throw new IOException(disgenetDataDir.getFileName() + " is not a readable file.");
        }
    }

    public DisgenetRdfVersion getDisgenetRdfVersion() {
        return disgenetRdfVersion;
    }

    /**
     * @param disgenetRdfVersion a {@link String} defining the DisGeNET RDF version
     * @throws InvalidStringFormatException see {@link DisgenetRdfVersion#retrieveVersion(String)}
     * @see DisgenetRdfVersion#retrieveVersion(String)
     */
    void setDisgenetRdfVersion(String disgenetRdfVersion) throws InvalidStringFormatException {
        this.disgenetRdfVersion = DisgenetRdfVersion.retrieveVersion(disgenetRdfVersion);
    }

    /**
     * @param disgenetRdfVersion an {@code int} defining the DisGeNET RDF version
     * @see DisgenetRdfVersion#retrieveVersion(int)
     */
    void setDisgenetRdfVersion(int disgenetRdfVersion) {
        this.disgenetRdfVersion = DisgenetRdfVersion.retrieveVersion(disgenetRdfVersion);
    }

    /**
     * @param disgenetRdfVersion a {@link DisgenetRdfVersion}
     */
    void setDisgenetRdfVersion(DisgenetRdfVersion disgenetRdfVersion) {
        this.disgenetRdfVersion = disgenetRdfVersion;
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
     * @throws IOException if {@code rvcfData} is not a readable file
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

