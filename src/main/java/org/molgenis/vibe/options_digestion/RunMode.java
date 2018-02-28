package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.rdf_processing.GenesForPhenotypeRetriever;

/**
 * Describes what the application should do.
 */
public enum RunMode {
    NONE("none") {
        @Override
        public void run(OptionsParser appOptions) {
            CommandLineOptionsParser.printHelpMessage();
        }
    }, GET_GENES_USING_SINGLE_PHENOTYPE("get genes matching a single phenotype") {
        @Override
        public void run(OptionsParser appOptions) throws CorruptDatabaseException {
            appOptions.printVerbose("Preparing DisGeNET dataset.");
            ModelReader reader = new TripleStoreDbReader(appOptions.getDisgenetDataDir());

            // Retrieves data from DisGeNET database.
            appOptions.printVerbose("Retrieving gene-disease associations for given phenotype.");
            GenesForPhenotypeRetriever genesForPhenotypeRetriever = new GenesForPhenotypeRetriever(reader, appOptions.getPhenotypes());
            genesForPhenotypeRetriever.run();

            // Stores needed data and allows the rest to be collected by garbage collector.
            GeneDiseaseCollection geneDiseaseCollection = genesForPhenotypeRetriever.getGeneDiseaseCollection();
            genesForPhenotypeRetriever = null;
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
                return GET_GENES_USING_SINGLE_PHENOTYPE;
            default:
                return NONE;
        }
    }
}
