package org.molgenis.vibe.cli.io.options_digestion;

import org.molgenis.vibe.cli.RunMode;
import org.molgenis.vibe.cli.io.output.target.FileOutputWriter;
import org.molgenis.vibe.cli.io.output.target.StdoutOutputWriter;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.cli.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.cli.io.output.target.OutputWriter;
import org.molgenis.vibe.core.io.input.ModelReaderFactory;
import org.molgenis.vibe.core.io.input.VibeDatabase;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class to be used for options parsing. Includes some basic validations (such as whether input arguments refer
 * to actual files).
 */
public class VibeOptions {
    /**
     * The selected mode to use.
     */
    private RunMode runMode;

    /**
     * Wether the app should run in verbose modus (extra print statements).
     */
    private boolean verbose = false;

    /**
     * Path to the Human Phenotype Oontology .owl file.
     */
    private Path hpoOntology;

    /**
     * The vibe main database.
     */
    private VibeDatabase vibeDatabase;

    /**
     * The phenotype(s) to be used within the application.
     */
    private Set<Phenotype> phenotypes = new HashSet<>();

    /**
     * Defines the {@link org.molgenis.vibe.core.ontology_processing.PhenotypesRetriever} to be used.
     */
    private PhenotypesRetrieverFactory phenotypesRetrieverFactory;

    /**
     * If set, defines the maximum distance to be used for finding Phenotypes within the HPO ontology connected to the
     * input Phenotypes.
     */
    private Integer ontologyMaxDistance;

    /**
     * Defines the gene-prioritised output format to be used.
     */
    private GenePrioritizedOutputFormatWriterFactory genePrioritizedOutputFormatWriterFactory;

    /**
     * Defines the output target to be used.
     */
    private OutputWriter outputWriter;

    public RunMode getRunMode() {
        return runMode;
    }

    void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    public boolean isVerbose() {
        return verbose;
    }

    void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void printVerbose(String text) {
        if(verbose) {
            System.out.println(text);
        }
    }

    public Path getHpoOntology() {
        return hpoOntology;
    }

    void setHpoOntology(String hpoOntology) throws InvalidPathException, IOException {
        setHpoOntology(Paths.get(hpoOntology));
    }

    void setHpoOntology(Path hpoOntology) throws IOException {
        if(checkIfPathIsReadableFile(hpoOntology)) {
            this.hpoOntology = hpoOntology;
        } else {
            throw new IOException(hpoOntology.getFileName() + " is not a readable file.");
        }
    }

    public VibeDatabase getVibeDatabase() {
        return vibeDatabase;
    }

    void setVibeDatabase(String databasePath) throws IOException {
        setVibeDatabase(Paths.get(databasePath));
    }

    void setVibeDatabase(Path databasePath) throws IOException {
        // VibeDatabase uses its own internal checks for whether the data is accessible.
        this.vibeDatabase = new VibeDatabase(databasePath, ModelReaderFactory.HDT);
    }

    public Set<Phenotype> getPhenotypes() {
        return phenotypes;
    }

    void setPhenotypes(Set<Phenotype> phenotypes) {
        this.phenotypes = phenotypes;
    }

    void addPhenotypes(Set<Phenotype> phenotypes) {
        this.phenotypes.addAll(phenotypes);
    }

    void addPhenotype(Phenotype phenotype) {
        phenotypes.add(phenotype);
    }

    public PhenotypesRetrieverFactory getPhenotypesRetrieverFactory() {
        return phenotypesRetrieverFactory;
    }

    /**
     *
     * @param name the {@link String} describing the {@link org.molgenis.vibe.core.ontology_processing.PhenotypesRetriever}
     *             to be used
     * @throws EnumConstantNotPresentException if {@code name} is not an accepted possibility.
     */
    void setPhenotypesRetrieverFactory(String name) throws EnumConstantNotPresentException {
        setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory.retrieve(name));
    }

    void setPhenotypesRetrieverFactory(PhenotypesRetrieverFactory phenotypesRetrieverFactory) {
        this.phenotypesRetrieverFactory = phenotypesRetrieverFactory;
    }

    public Integer getOntologyMaxDistance() {
        return ontologyMaxDistance;
    }

    /**
     * @throws NumberFormatException if {@code ontologyMaxDistance} could not be parsed to an {@link Integer}
     */
    void setOntologyMaxDistance(String ontologyMaxDistance) throws NumberFormatException {
        setOntologyMaxDistance(Integer.parseInt(ontologyMaxDistance));
    }

    /**
     * @@throws {@link IllegalArgumentException} if {@code ontologyMaxDistance < 0}
     */
    void setOntologyMaxDistance(Integer ontologyMaxDistance) {
        if (ontologyMaxDistance >= 0) {
            this.ontologyMaxDistance = ontologyMaxDistance;
        } else {
            throw new IllegalArgumentException("value must be >= 0.");
        }
    }

    public GenePrioritizedOutputFormatWriterFactory getGenePrioritizedOutputFormatWriterFactory() {
        return genePrioritizedOutputFormatWriterFactory;
    }

    void setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory genePrioritizedOutputFormatWriterFactory) {
        this.genePrioritizedOutputFormatWriterFactory = genePrioritizedOutputFormatWriterFactory;
    }

    /**
     * @param phenotypes {@link String}{@code []}
     * @throws InvalidStringFormatException if any of the {@code phenotypes} failed to be converted into a
     * {@link Phenotype} using {@link Phenotype#Phenotype(String)}
     */
    void setPhenotypes(String[] phenotypes) throws InvalidStringFormatException {
        this.phenotypes = new HashSet<>();
        addPhenotypes(phenotypes);
    }

    /**
     * @param phenotypes {@link String}{@code []}
     * @throws InvalidStringFormatException if any of the {@code phenotypes} failed to be converted into a
     * {@link Phenotype} using {@link Phenotype#Phenotype(String)}
     */
    void addPhenotypes(String[] phenotypes) throws InvalidStringFormatException {
        for(int i=0; i < phenotypes.length; i++) {
            addPhenotype(phenotypes[i]);
        }
    }

    /**
     * @param phenotype {@link String}
     * @throws InvalidStringFormatException if the {@code phenotype} failed to be converted into a {@link Phenotype}
     * using {@link Phenotype#Phenotype(String)}
     */
    void addPhenotype(String phenotype) throws InvalidStringFormatException {
        addPhenotype(new Phenotype(phenotype));
    }

    public OutputWriter getOutputWriter() {
        return outputWriter;
    }

    /**
     * Wrapper for {@link #setFileOutputWriter(Path)}.
     * @param outputFile the file path to write the output to
     * @throws InvalidPathException if {@code outputFile} could not be converted to {@link Path}
     * @throws FileAlreadyExistsException if file already exists
     */
    protected void setFileOutputWriter(String outputFile) throws InvalidPathException, FileAlreadyExistsException {
        setFileOutputWriter(Paths.get(outputFile));
    }

    /**
     * Sets the {@link OutputWriter} to a {@link FileOutputWriter}. Overrides any previously set {@link OutputWriter}.
     * @param outputFile the file path to write the output to
     * @throws FileAlreadyExistsException if file already exists
     */
    protected void setFileOutputWriter(Path outputFile) throws FileAlreadyExistsException {
        if(checkIfPathIsWritableFile(outputFile)) {
            throw new FileAlreadyExistsException(outputFile.getFileName() + " already exists.");
        }
        this.outputWriter = new FileOutputWriter(outputFile);
    }

    /**
     * Wrapper for {@link #setFileOutputWriterForced(Path)}.
     * @param outputFile the file path to write the output to
     * @throws InvalidPathException if {@code outputFile} could not be converted to {@link Path}
     */
    protected void setFileOutputWriterForced(String outputFile) throws InvalidPathException {
        setFileOutputWriterForced(Paths.get(outputFile));
    }

    /**
     * Sets the {@link OutputWriter} to a {@link FileOutputWriter}. Overrides any previously set {@link OutputWriter}.
     * If an existing file is given, it will be overwritten.
     * @param outputFile the file path to write the output to
     * @see FileOutputWriter#initialize()
     */
    protected void setFileOutputWriterForced(Path outputFile) {
        checkIfPathIsWritableFile(outputFile);
        this.outputWriter = new FileOutputWriter(outputFile);
    }

    /**
     * Sets the {@link OutputWriter} to an {@link StdoutOutputWriter}. Overrides any previously set {@link OutputWriter}.
     */
    protected void setStdoutOutputWriter() {
        this.outputWriter = new StdoutOutputWriter();
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
     * Checks if a given {@link Path} is an existing writable file.
     * @param path {@link Path}
     * @return {@code boolean} {@code true} if so, otherwise {@code false}
     */
    private boolean checkIfPathIsWritableFile(Path path) {
        return Files.isWritable(path) && Files.isRegularFile(path);
    }

    /**
     * Checks if a given {@link Path} is a directory.
     * @param path {@link Path}
     * @return {@code boolean} {@code true} if so, otherwise {@code false}
     */
    private boolean checkIfPathIsDir(Path path) {
        return Files.isDirectory(path);
    }

    /**
     * Checks whether the set variables adhere to the selected {@link RunMode}. Can be used after processing of
     * user input to validate if variables are set correctly (based on the specified {@link RunMode}).
     * @return {@code true} if available variables adhere to {@link RunMode}, {@code false} if not
     */
    @SuppressWarnings("java:S128")
    public boolean validate() {
        switch (runMode) {
            case GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES:
                if(!validateRelatedPhenotypesRetrieval()) return false;
                // NO BREAK: continues!!!
            case GENES_FOR_PHENOTYPES:
                if(!validateGenesForPhenotype()) return false;
            default:
                // No checks required for non-specified cases.
        }

        // If nothing failed, returns true.
        return true;
    }

    /**
     * Checks whether variables were set that are only required for retrieving related {@link Phenotype}{@code s} for
     * the input {@link Phenotype}{@code s}.
     * <br /><br />
     * <b>IMPORTANT:</b> Requirements that are checked in {@link #validateGenesForPhenotype()} are skipped here!
     * @return {@code true} if all needed variables are set, otherwise {@code false}
     */
    private boolean validateRelatedPhenotypesRetrieval() {
        // Check if a factory for related HPO retrieval was set.
        if (getPhenotypesRetrieverFactory() == null) {
            return false;
        }
        // Check if a max distance for related HPO retrieval was set.
        if (getOntologyMaxDistance() == null) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether variables were set that are required for retrieving a
     * {@link org.molgenis.vibe.core.formats.GeneDiseaseCollection} from the database using
     * {@link Phenotype}{@code s} as input.
     * @return {@code true} if all needed variables are set, otherwise {@code false}
     */
    private boolean validateGenesForPhenotype() {
        // Check if vibe database is set.
        if (getVibeDatabase() == null) {
            return false;
        }
        // Check if HPO ontology data is set.
        if (getHpoOntology() == null) {
            return false;
        }
        // Check if an output writer was given.
        if (getOutputWriter() == null) {
            return false;
        }
        // Checks if a gene prioritized output format factory was given.
        if (getGenePrioritizedOutputFormatWriterFactory() == null) {
            return false;
        }
        // Check if there are any input phenotypes.
        if (getPhenotypes().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VibeOptions{" +
                "runMode=" + runMode +
                ", verbose=" + verbose +
                ", hpoOntology=" + hpoOntology +
                ", vibeDatabase=" + vibeDatabase +
                ", phenotypes=" + phenotypes +
                ", phenotypesRetrieverFactory=" + phenotypesRetrieverFactory +
                ", ontologyMaxDistance=" + ontologyMaxDistance +
                ", genePrioritizedOutputFormatWriterFactory=" + genePrioritizedOutputFormatWriterFactory +
                ", outputWriter=" + outputWriter +
                '}';
    }
}
