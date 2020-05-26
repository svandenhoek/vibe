package org.molgenis.vibe.cli;

import org.molgenis.vibe.cli.io.options_digestion.CommandLineOptionsParser;
import org.molgenis.vibe.cli.io.options_digestion.VibeOptions;
import org.molgenis.vibe.cli.properties.VibePropertiesLoader;

import java.io.IOException;

/**
 * The main application class.
 */
public class VibeApplication {
    /**
     * The main method for when used as a standalone application.
     * @param args {@link String}{@code []}
     */
    public static void main(String[] args) {
        try {
            // Parses application properties.
            // Should always be ran first (as it sets the values for the VibeProperties enum)!
            VibePropertiesLoader.loadProperties();

            try {
                // Parses user-input.
                VibeOptions vibeOptions = new VibeOptions();
                CommandLineOptionsParser.parse(args, vibeOptions);

                // If all input input correctly parsed, runs app.
                if(vibeOptions.validate()) {
                    try {
                        vibeOptions.getRunMode().run(vibeOptions);
                    } catch (IOException e) {
                        System.err.println(e.getLocalizedMessage());
                    } catch (Exception e) { // Errors generated while running the app.
                        printUnexpectedExceptionOccurred();
                        e.printStackTrace();
                    }
                } else { // Errors caused by invalid options configuration.
                    printUnexpectedExceptionOccurred();
                    vibeOptions.toString();

                }
            } catch (Exception e) { // Errors generated during options parsing.
                System.err.println(e.getLocalizedMessage());
                CommandLineOptionsParser.printHelpMessage();
            }
        } catch (IOException e) {
            printUnexpectedExceptionOccurred();
            e.printStackTrace();
        }
    }

    public static void printUnexpectedExceptionOccurred() {
        System.err.println("######## ######## ########");
        System.err.println("An unexpected exception occurred. Please notify the developer (see https://github.com/molgenis/vibe) and supply the text as seen below.");
        System.err.println("######## ######## ########");
    }
}
