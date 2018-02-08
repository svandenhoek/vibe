package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.rdf_processing.querying.SparqlRange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to be used for options parsing. Includes some basic validations (such as whether input arguments refer
 * to actual files).
 */
public abstract class OptionsParser {

    /**
     * Wether the app should run in verbose modus (extra print statements).
     */
    private boolean verbose = false;

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
     * The format of how the data is stored.
     */
    private RdfStorageFormat rdfStorageFormat = RdfStorageFormat.TDB; // individual files model creation not supported

    /**
     * The DisGeNET RDF version.
     */
    private DisgenetRdfVersion disgenetRdfVersion;

    /**
     * The HPO terms to be used within the application.
     */
    private Hpo[] hpoTerms;

    private SparqlRange sparqlRange = new SparqlRange(0);

    public boolean isVerbose() {
        return verbose;
    }

    protected void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void printVerbose(String text) {
        if(verbose) {
            System.out.println(text);
        }
    }

    public RunMode getRunMode() {
        return runMode;
    }

    protected void setRunMode(RunMode runMode) {
        printVerbose("run mode: " + runMode.getDescription());
        this.runMode = runMode;
    }

    protected void setDisgenet(String disgenetDataDir, String disgenetRdfVersion) throws InvalidPathException, IOException {
        setDisgenetDataDir(disgenetDataDir);
        setDisgenetRdfVersion(disgenetRdfVersion);
    }

    protected void setDisgenet(String disgenetDataDir, DisgenetRdfVersion disgenetRdfVersion) throws InvalidPathException, IOException {
        setDisgenetDataDir(disgenetDataDir);
        setDisgenetRdfVersion(disgenetRdfVersion);
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

    public RdfStorageFormat getRdfStorageFormat() {
        return rdfStorageFormat;
    }

//    protected void setRdfStorageFormat(RdfStorageFormat rdfStorageFormat) {
//        this.rdfStorageFormat = rdfStorageFormat;
//    }

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

    public Hpo[] getHpoTerms() {
        return hpoTerms;
    }

    /**
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

    public SparqlRange getSparqlRange() {
        return sparqlRange;
    }

    public void setSparqlRange(SparqlRange sparqlRange) {
        this.sparqlRange = sparqlRange;
    }

    /**
     * Checks whether the set variables adhere to the selected {@link RunMode}.
     * @return {@code true} if available variables adhere to {@link RunMode}, {@code false} if not
     */
    protected boolean checkConfig() {
        if(runMode.equals(RunMode.NONE)) {
            return true;
        }
        if(disgenetDataDir == null || disgenetRdfVersion == null) {
            return false;
        }
        switch (runMode) {
            case GET_GENES_WITH_SINGLE_HPO:
                if(hpoTerms != null & hpoTerms.length == 1) {
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

