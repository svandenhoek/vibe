package org.molgenis.vibe;

import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.options_digestion.CommandLineOptionsParser;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.options_digestion.RunMode;
import org.molgenis.vibe.rdf_processing.GenesForHpoRetriever;

/**
 * The main application class.
 */
public class VibeApplication {
    /**
     * The main method for when used as a standalone application.
     * @param args {@link String}{@code []}
     */
    public static void main(String[] args) {
        VibeApplication app = new VibeApplication();

        try {
            CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);
            // If RunMode is NONE, shows help message and quits application.
            if(appOptions.getRunMode() == RunMode.NONE) {
                CommandLineOptionsParser.printHelpMessage();
            } else { // Any other RunMode will continue application.
                try {
                    app.run(appOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            CommandLineOptionsParser.printHelpMessage();
        }
    }

    /**
     * The actual processing parts of the application.
     * @param appOptions {@link OptionsParser}
     */
    public void run(OptionsParser appOptions) {
        appOptions.printVerbose("Preparing DisGeNET dataset");
        ModelReader modelReader = new TripleStoreDbReader(appOptions.getDisgenetDataDir());

        if(appOptions.getRunMode() == RunMode.GET_GENES_WITH_SINGLE_HPO) {
            appOptions.printVerbose("Generating query for " + appOptions.getHpoTerms()[0].getFormattedId());
            GenesForHpoRetriever genesForHpo = new GenesForHpoRetriever(appOptions, modelReader);
            genesForHpo.run();
        }
    }
}
