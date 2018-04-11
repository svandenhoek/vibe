package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.io.OntologyModelFilesReader;
import org.molgenis.vibe.io.output.FileOutputWriter;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.io.output.ResultsPerGeneCsvFileOutputWriter;
import org.molgenis.vibe.ontology_processing.MaxDistanceRetriever;
import org.molgenis.vibe.ontology_processing.PhenotypesRetriever;
import org.molgenis.vibe.query_output_digestion.prioritization.GenePrioritizer;
import org.molgenis.vibe.query_output_digestion.prioritization.HighestSingleDisgenetScoreGenePrioritizer;
import org.molgenis.vibe.rdf_processing.GenesForPhenotypeRetriever;

import java.io.IOException;

/**
 * Describes what the application should do.
 */
public enum RunMode {
    NONE("none") {
        @Override
        public void run(OptionsParser appOptions) {
            CommandLineOptionsParser.printHelpMessage();
        }
    }, GET_DISGENET_GENE_GDAS_FOR_PHENOTYPES("Retrieve all genes (with gene-disease association data) for the given phenotypes.") {
        @Override
        public void run(OptionsParser appOptions) throws IOException {
            // Prints RunMode if verbose.
            appOptions.printVerbose(getDescription());

            // Loads HPO dataset.
            appOptions.printVerbose("Loading HPO dataset.");
            OntologyModelFilesReader ontologyReader = new OntologyModelFilesReader(appOptions.getHpoOntology().toString());

            // Retrieves data from HPO dataset.
            appOptions.printVerbose("Retrieving linked HPOs with a maximum distance of: " + appOptions.getOntologyMaxDistance());
            PhenotypesRetriever hpoRetriever = new MaxDistanceRetriever(ontologyReader.getModel(), appOptions.getPhenotypes(), appOptions.getOntologyMaxDistance());
            hpoRetriever.run();

            // Loads DisGeNET dataset.
            appOptions.printVerbose("Loading DisGeNET dataset.");
            ModelReader disgenetReader = new TripleStoreDbReader(appOptions.getDisgenetDataDir());

            // Retrieves data from DisGeNET dataset.
            appOptions.printVerbose("Retrieving data from DisGeNET dataset.");
            GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(disgenetReader, hpoRetriever.getPhenotypeNetworkCollection().getPhenotypes());
            genesForPhenotypeRetriever.run();

            // Stores needed data and allows the rest to be collected by garbage collector.
            GeneDiseaseCollection geneDiseaseCollection = genesForPhenotypeRetriever.getGeneDiseaseCollection();
            genesForPhenotypeRetriever = null;

            // Generates gene order.
            appOptions.printVerbose("Ordering genes based on priority.");
            GenePrioritizer prioritizer = new HighestSingleDisgenetScoreGenePrioritizer(geneDiseaseCollection);
            prioritizer.run();

            // Writes output to file.
            appOptions.printVerbose("Writing genes to file.");
            FileOutputWriter outputWriter = new ResultsPerGeneCsvFileOutputWriter(appOptions.getOutputFile(), geneDiseaseCollection, prioritizer.getPriority());
            outputWriter.run();
        }
    };

    private String description;

    public String getDescription() {
        return description;
    }

    RunMode(String description) {
        this.description = description;
    }

    public abstract void run(OptionsParser appOptions) throws Exception;

    /**
     * Retrieves the {@link RunMode} based on a {@link String} existing out of ONLY numbers.
     * @param i a {@link String} that contains a number (and nothing else).
     * @return the {@link RunMode} belonging to the given number {@code i}
     * @throws NumberFormatException see {@link Integer#parseInt(String)}
     */
    public static RunMode retrieve(String i) throws NumberFormatException {
        return retrieve(Integer.parseInt(i));
    }

    /**
     * Retrieves the {@link RunMode} based on a {@code int}.
     * @param i an {@code int} defining the {@link RunMode}
     * @return the {@link RunMode} belonging to the given number {@code i}
     */
    public static RunMode retrieve(int i) {
        switch (i) {
            case 1:
                return GET_DISGENET_GENE_GDAS_FOR_PHENOTYPES;
            default:
                return NONE;
        }
    }
}
