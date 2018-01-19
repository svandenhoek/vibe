package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Hpo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class to be used for options parsing. Includes some basic validations (such as whether input arguments refer
 * to actual files).
 */
public abstract class OptionsParser {

    /**
     * Defines the exact operation to be done by the application. Default is set to {@link RunMode#NONE}.
     */
    private RunMode runMode = RunMode.NONE;

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
     * All files required to create a SPARQL searchable DisGeNET model based on the {@code disgenetDataDir} and the files
     * defined by the specific {@code disgenetRdfVersion}.
     */
    private Path[] disgenetDataFiles;

    /**
     * The HPO terms to be used within the application.
     */
    private Hpo[] hpoTerms;

    /**
     * The rVCF file that needs to be digested.
     */
    private Path rvcfData;

    public RunMode getRunMode() {
        return runMode;
    }

    protected void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    protected void setDisgenet(String disgenetDataDir, String disgenetRdfVersion) throws InvalidPathException, IOException {
        setDisgenetDataDir(disgenetDataDir);
        setDisgenetRdfVersion(disgenetRdfVersion);
        setDisgenetDataFiles(this.disgenetDataDir, this.disgenetRdfVersion);
    }

    protected void setDisgenet(String disgenetDataDir, DisgenetRdfVersion disgenetRdfVersion) throws InvalidPathException, IOException {
        setDisgenetDataDir(disgenetDataDir);
        setDisgenetRdfVersion(disgenetRdfVersion);
        setDisgenetDataFiles(this.disgenetDataDir, this.disgenetRdfVersion);
    }

    public Path getDisgenetDataDir() {
        return disgenetDataDir;
    }

    /**
     * @param disgenetDataDir a {@link String} containing the path to the directory containing the data required to
     *                       create a model from the DisGeNET data
     * @throws InvalidPathException if {@link Paths#get(String, String...)}} fails to convert a {@link String} to {@link Path}
     * @throws IOException see {@link #setDisgenetDataDir(Path)}
     */
    private void setDisgenetDataDir (String disgenetDataDir) throws InvalidPathException, IOException {
        setDisgenetDataDir(Paths.get(disgenetDataDir));
    }

    /**
     * @param disgenetDataDir a {@link Path} containing the path to the directory containing the data required to
     *                       create a model from the DisGeNET data
     * @throws IOException if {@code disgenetDump} is not a readable file
     */
    private void setDisgenetDataDir(Path disgenetDataDir) throws IOException {
        if(checkIfPathIsDir(disgenetDataDir)) {
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
    private void setDisgenetRdfVersion(String disgenetRdfVersion) throws InvalidStringFormatException {
        this.disgenetRdfVersion = DisgenetRdfVersion.retrieveVersion(disgenetRdfVersion);
    }

    /**
     * @param disgenetRdfVersion an {@code int} defining the DisGeNET RDF version
     * @see DisgenetRdfVersion#retrieveVersion(int)
     */
    private void setDisgenetRdfVersion(int disgenetRdfVersion) {
        this.disgenetRdfVersion = DisgenetRdfVersion.retrieveVersion(disgenetRdfVersion);
    }

    /**
     * @param disgenetRdfVersion a {@link DisgenetRdfVersion}
     */
    private void setDisgenetRdfVersion(DisgenetRdfVersion disgenetRdfVersion) {
        this.disgenetRdfVersion = disgenetRdfVersion;
    }

    public Path[] getDisgenetDataFiles() {
        return disgenetDataFiles;
    }

    private void setDisgenetDataFiles(Path disgenetDataDir, DisgenetRdfVersion disgenetRdfVersion) throws IOException {
        Path[] paths = disgenetRdfVersion.getRequiredFilePaths(disgenetDataDir);
        List<Path> invalidPaths = getInvalidPaths(paths);
        if(invalidPaths.size() == 0) {
            this.disgenetDataFiles = paths;
        } else {
            String listString = invalidPaths.stream().map(Path::getFileName).map(Path::toString)
                    .collect(Collectors.joining(", "));
            throw new IOException(listString + " are not readable/missing.");
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
    protected void setRvcfData(String rvcfData) throws InvalidPathException, IOException {
        setRvcfData(Paths.get(rvcfData));
    }

    /**
     * @param rvcfData {@link Path}
     * @throws IOException if {@code rvcfData} is not a readable file
     */
    protected void setRvcfData(Path rvcfData) throws IOException {
        if(checkIfPathIsReadableFile(rvcfData)) {
            this.rvcfData = rvcfData;
        } else {
            throw new IOException(rvcfData.getFileName() + " is not a readable file.");
        }
    }

    public Hpo[] getHpoTerms() {
        return hpoTerms;
    }

    /**
     *
     * @param hpoTerms {@link String}{@code []}
     * @throws InvalidStringFormatException if any of the {@code hpoTerms} failed to be converted into a {@link Hpo} using
     * {@link Hpo#Hpo(String)}
     */
    protected void setHpoTerms(String[] hpoTerms) throws InvalidStringFormatException {
        Hpo[] hpos = new Hpo[hpoTerms.length];
        for(int i=0; i < hpoTerms.length; i++) {
            hpos[i] = new Hpo(hpoTerms[i]);
        }
        this.hpoTerms = hpos;
    }

    /**
     * Given an array containing {@link Path}{@code s}, returns a {@link List} containing any {@link Path}{@code s} that
     * return false according to {@link #checkIfPathIsReadableFile(Path)}. If all files adhere to this, returns an empty
     * {@link List}.
     * @param paths an array with {@link Path}{@code s} that should be checked whether they adhere to {@link #checkIfPathIsReadableFile(Path)}
     * @return a list with all {@link Path}{@code s} not adhering to {@link #checkIfPathIsDir(Path)} (empty if all files adhere to this)
     */
    private List<Path> getInvalidPaths(Path[] paths) {
        List<Path> invalidPaths = new ArrayList<>();
        for(int i=0; i < paths.length; i++) {
            if( !checkIfPathIsReadableFile(paths[i]) ) {
                invalidPaths.add(paths[i]);
            }
        }
        return invalidPaths;
    }

    /**
     * Checks whether the set variables adhere to the selected {@link RunMode}.
     * @return {@code true} if available variables adhere to {@link RunMode}, {@code false} if not
     */
    protected boolean checkConfig() {
        switch (runMode) {
            case NONE:
                return true;
            case GET_GENES_WITH_SINGLE_HPO:
                if(disgenetDataDir != null && disgenetRdfVersion != null && disgenetDataFiles != null) {
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * Checks if a given {@link Path} is an existing readable file.
     * @param path {@link Path}
     * @return {@code boolean} {@code true} if so, otherwise {@code false}
     */
    private boolean checkIfPathIsReadableFile(Path path) {
        return Files.isReadable(path) && Files.isRegularFile(path);
    }

    /**
     * Checks if a given {@link Path} is a directory.
     * @param path {@link Path}
     * @return {@code boolean} {@code true} if so, otherwise {@code false}
     */
    private boolean checkIfPathIsDir(Path path) {
        return Files.isDirectory(path);
    }
}

