package org.molgenis.vibe.cli.input;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.cli.output.format.PrioritizedOutputFormatWriter;
import org.molgenis.vibe.cli.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.cli.output.target.FileOutputWriter;
import org.molgenis.vibe.cli.output.target.OutputWriter;
import org.molgenis.vibe.cli.output.target.StdoutOutputWriter;
import org.molgenis.vibe.ontology_processing.PhenotypesRetrieverFactory;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.GenePrioritizerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
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
     * Defines the {@link OutputWriter} to be used.
     */
    private OutputWriter outputWriter;

    /**
     * Defines the {@link PrioritizedOutputFormatWriter} to be used (currently for genes only).
     */
    private GenePrioritizedOutputFormatWriterFactory genePrioritizedOutputFormatWriterFactory;

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
    private GenePrioritizerFactory genePrioritizerFactory = GenePrioritizerFactory.HIGHEST_DISGENET_SCORE;

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
        if(checkIfPathIsReadableFile(outputFile)) {
            throw new FileAlreadyExistsException(outputFile.getFileName() + " already exists.");
        }
        this.outputWriter = new FileOutputWriter(outputFile);
    }

    /**
     * Sets the {@link OutputWriter} to an {@link StdoutOutputWriter}. Overrides any previously set {@link OutputWriter}.
     */
    protected void setStdoutOutputWriter() {
        this.outputWriter = new StdoutOutputWriter();
    }

    public GenePrioritizedOutputFormatWriterFactory getGenePrioritizedOutputFormatWriterFactory() {
        return genePrioritizedOutputFormatWriterFactory;
    }

    protected void setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory genePrioritizedOutputFormatWriterFactory) {
        this.genePrioritizedOutputFormatWriterFactory = genePrioritizedOutputFormatWriterFactory;
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

    public Integer getOntologyMaxDistance() {
        return ontologyMaxDistance;
    }

    protected void setOntologyMaxDistance(String ontologyMaxDistance) throws NumberFormatException {
        setOntologyMaxDistance(Integer.parseInt(ontologyMaxDistance));
    }

    protected void setOntologyMaxDistance(int ontologyMaxDistance) {
        if (ontologyMaxDistance >= 0){
            this.ontologyMaxDistance = ontologyMaxDistance;
        } else {
            throw new IllegalArgumentException("value must be >= 0.");
        }
    }

    public GenePrioritizerFactory getGenePrioritizerFactory() {
        return genePrioritizerFactory;
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

