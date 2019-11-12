package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.io.output.GenePrioritizedFileOutputWriterFactory;
import org.molgenis.vibe.ontology_processing.PhenotypesRetrieverFactory;
import org.molgenis.vibe.query_output_digestion.prioritization.GenePrioritizerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class to be used for options parsing. Includes some basic validations (such as whether input arguments refer
 * to actual files).
 *
 * After processing, implementations should ALWAYS call {@link #checkConfig()} for validation to prevent unexpected errors
 * further into the application.
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
     * Path to the Human Phenotype Oontology .owl file.
     */
    private Path hpoOntology;

    /**
     * Path to the directory storing the main database to use.
     */
    private Path database;

    /**
     * The phenotype(s) to be used within the application.
     */
    private Set<Phenotype> phenotypes = new HashSet<>();

    /**
     * The path to write the output to.
     */
    private Path outputFile;

    /**
     * Defines the {@link org.molgenis.vibe.io.output.FileOutputWriter} to be used.
     */
    private GenePrioritizedFileOutputWriterFactory genePrioritizedFileOutputWriterFactory;

    /**
     * Defines the {@link org.molgenis.vibe.ontology_processing.PhenotypesRetriever} to be used.
     */
    private PhenotypesRetrieverFactory phenotypesRetrieverFactory;

    /**
     * If set, defines the maximum distance to be used for finding Phenotypes within the HPO ontology connected to the
     * input Phenotypes.
     */
    private Integer ontologyMaxDistance;

    /**
     * Sets the gene prioritizer to be used.
     */
    private GenePrioritizerFactory genePrioritizerFactory;

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
        this.runMode = runMode;
    }

    public Path getHpoOntology() {
        return hpoOntology;
    }

    protected void setHpoOntology(String hpoOntology) throws InvalidPathException, IOException {
        setHpoOntology(Paths.get(hpoOntology));
    }

    protected void setHpoOntology(Path hpoOntology) throws IOException {
        if(checkIfPathIsReadableFile(hpoOntology)) {
            this.hpoOntology = hpoOntology;
        } else {
            throw new IOException(hpoOntology.getFileName() + " is not a readable file.");
        }
    }

    public Path getDatabase() {
        return database;
    }

    protected void setDatabase(String database) throws InvalidPathException, IOException {
        setDatabase(Paths.get(database));
    }

    protected void setDatabase(Path database) throws IOException {
        if(checkIfPathIsDir(database)) {
            this.database = database;
        } else {
            throw new IOException(database.getFileName() + " is not a directory.");
        }
    }

    public Set<Phenotype> getPhenotypes() {
        return phenotypes;
    }

    protected void setPhenotypes(Set<Phenotype> phenotypes) {
        this.phenotypes = phenotypes;
    }

    protected void addPhenotypes(Set<Phenotype> phenotypes) {
        this.phenotypes.addAll(phenotypes);
    }

    protected void addPhenotype(Phenotype phenotype) {
        phenotypes.add(phenotype);
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

    public Path getOutputFile() {
        return outputFile;
    }
    protected void setOutputFile(String outputFile) throws InvalidPathException, FileAlreadyExistsException {
        setOutputFile(Paths.get(outputFile));
    }

    protected void setOutputFile(Path outputFile) throws FileAlreadyExistsException {
        if(checkIfPathIsReadableFile(outputFile)) {
            throw new FileAlreadyExistsException(outputFile.getFileName() + " already exists.");
        }
        this.outputFile = outputFile;
    }

    public GenePrioritizedFileOutputWriterFactory getGenePrioritizedFileOutputWriterFactory() {
        return genePrioritizedFileOutputWriterFactory;
    }

    protected void setGenePrioritizedFileOutputWriterFactory(GenePrioritizedFileOutputWriterFactory genePrioritizedFileOutputWriterFactory) {
        this.genePrioritizedFileOutputWriterFactory = genePrioritizedFileOutputWriterFactory;
    }

    public PhenotypesRetrieverFactory getPhenotypesRetrieverFactory() {
        return phenotypesRetrieverFactory;
    }

    protected void setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory phenotypesRetrieverFactory) {
        this.phenotypesRetrieverFactory = phenotypesRetrieverFactory;
    }

    /**
     * @param name the {@link String} describing the {@link org.molgenis.vibe.ontology_processing.PhenotypesRetriever}
     *             to be used
     * @throws EnumConstantNotPresentException if {@code name} is not an accepted possibility.
     */
    protected void setPhenotypesRetrieverFactory(String name) throws EnumConstantNotPresentException {
        this.phenotypesRetrieverFactory = PhenotypesRetrieverFactory.retrieve(name);
    }

    public int getOntologyMaxDistance() {
        return ontologyMaxDistance;
    }

    protected void setOntologyMaxDistance(String ontologyMaxDistance) throws NumberFormatException {
        setOntologyMaxDistance(Integer.parseInt(ontologyMaxDistance));
    }

    protected void setOntologyMaxDistance(int ontologyMaxDistance) {
        this.ontologyMaxDistance = ontologyMaxDistance;
    }

    public GenePrioritizerFactory getGenePrioritizerFactory() {
        return genePrioritizerFactory;
    }

    protected void setGenePrioritizerFactory(GenePrioritizerFactory genePrioritizerFactory) {
        this.genePrioritizerFactory = genePrioritizerFactory;
    }

    /**
     * @param name the {@link String} describing the {@link org.molgenis.vibe.query_output_digestion.prioritization.GenePrioritizer}
     *             to be used
     * @throws EnumConstantNotPresentException if {@code name} is not an accepted possibility.
     */
    protected void setGenePrioritizerFactory(String name) throws EnumConstantNotPresentException {
        this.genePrioritizerFactory = GenePrioritizerFactory.retrieve(name);
    }

    /**
     * Checks whether the set variables adhere to the selected {@link RunMode}. Can be used after processing of
     * user input if variables are set correctly (based on the specified {@link RunMode}.
     * @return {@code true} if available variables adhere to {@link RunMode}, {@code false} if not
     */
    protected boolean checkConfig() {
        // With RunMode.NONE there are no requirements.
        if(!runMode.equals(RunMode.NONE)) {
            // Check if DisGeNET data is set.
            if (database == null) {
                return false;
            }
            // Check if an output file was given.
            if (outputFile == null) {
                return false;
            }
            // Checks if an output factory was given.
            if (genePrioritizedFileOutputWriterFactory == null) {
                return false;
            }
            // Checks whether a gene prioritizer was selected.
            if(genePrioritizerFactory == null) {
                return false;
            }
            // Check config specific settings are set.
            switch (runMode) {
                // Additional checks if related HPOs need to be retrieved.
                case GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES:
                    // Check if a factory for related HPO retrieval was set.
                    if(phenotypesRetrieverFactory == null) {
                        return false;
                    }
                    // Check if HPO ontology data is set.
                    if (hpoOntology == null) {
                        return false;
                    }
                    // Check if a max distance for related HPO retrieval was set.
                    if(ontologyMaxDistance == null) {
                        return false;
                    }
                    // NO BREAK: continues!!!

                // Checks for if no associated phenotypes need to be retrieved.
                case GENES_FOR_PHENOTYPES:
                    // Check if there are any input phenotypes.
                    if (phenotypes.size() == 0) {
                        return false;
                    }
            }
        }

        return true;
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

