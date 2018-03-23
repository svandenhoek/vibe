package org.molgenis.vibe.options_digestion;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Phenotype;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * The phenotype(s) to be used within the application.
     */
    private Set<Phenotype> phenotypes = new HashSet<>();

    /**
     * The file to write the output to.
     */
    private Path outputFile;

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

    public Set<Phenotype> getPhenotypes() {
        return phenotypes;
    }

    /**
     * @param phenotypes {@link String}{@code []}
     * @throws InvalidStringFormatException if any of the {@code phenotypes} failed to be converted into a {@link Phenotype} using
     * {@link Phenotype#Phenotype(String)}
     */
    protected void setPhenotypes(String[] phenotypes) throws InvalidStringFormatException {
        this.phenotypes = new HashSet<>();
        addPhenotypes(phenotypes);
    }

    /**
     * @param phenotypes {@link String}{@code []}
     * @throws InvalidStringFormatException if any of the {@code phenotypes} failed to be converted into a {@link Phenotype} using
     * {@link Phenotype#Phenotype(String)}
     */
    protected void addPhenotypes(String[] phenotypes) throws InvalidStringFormatException {
        for(int i=0; i < phenotypes.length; i++) {
            addPhenotype(phenotypes[i]);
        }
    }

    /**
     * @param phenotype {@link String}
     * @throws InvalidStringFormatException if the {@code phenotype} failed to be converted into a {@link Phenotype} using
     * {@link Phenotype#Phenotype(String)}
     */
    protected void addPhenotype(String phenotype) throws InvalidStringFormatException {
        addPhenotype(new Phenotype(phenotype));
    }

    protected void addPhenotype(Phenotype phenotype) {
        phenotypes.add(phenotype);
    }

    public Path getOutputFile() {
        return outputFile;
    }
    protected void setOutputFile(String outputFile) throws FileAlreadyExistsException {
        setOutputFile(Paths.get(outputFile));
    }

    protected void setOutputFile(Path outputFile) throws FileAlreadyExistsException {
        if(checkIfPathIsReadableFile(outputFile)) {
            throw new FileAlreadyExistsException(outputFile.getFileName() + " already exists.");
        }
        this.outputFile = outputFile;
    }

    /**
     * Checks whether the set variables adhere to the selected {@link RunMode}.
     * @return {@code true} if available variables adhere to {@link RunMode}, {@code false} if not
     */
    protected void checkConfig() throws IOException {
        List<String> errors = new ArrayList<>();

        // With RunMode.NONE there are no requirements.
        if(!runMode.equals(RunMode.NONE)) {
            // Otherwise check if DisGeNET data is set.
            if (disgenetDataDir == null || disgenetRdfVersion == null) {
                errors.add("DisGeNET dataset not set.");
            }
            // Check if an output file was given.
            if (outputFile == null) {
                errors.add("No output file was given.");
            }
            // Check config specific settings.
            switch (runMode) {
                case GET_GENES_USING_SINGLE_PHENOTYPE:
                    if (phenotypes.size() == 0) {
                        errors.add("No phenotype was given.");
                    } else {
                        if(phenotypes.size() > 1) {
                            errors.add("More than 1 phenotype was given.");
                        }
                    }
            }
        }

        if(errors.size() > 0) {
            throw new IOException(StringUtils.join(errors, System.lineSeparator()));
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

