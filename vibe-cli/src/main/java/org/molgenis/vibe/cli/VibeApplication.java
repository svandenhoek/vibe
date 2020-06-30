package org.molgenis.vibe.cli;

import org.molgenis.vibe.cli.io.options_digestion.CommandLineOptionsParser;
import org.molgenis.vibe.cli.io.options_digestion.VibeOptions;

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
            // Parses user-input.
            VibeOptions vibeOptions = new VibeOptions();
            CommandLineOptionsParser.parse(args, vibeOptions);

            // If all input correctly parsed, runs app.
            if(vibeOptions.validate()) {
                executeRunMode(vibeOptions);
            } else { // Errors caused by invalid options configuration.
                printUnexpectedExceptionOccurred();
                vibeOptions.toString();
            }
        } catch (Exception e) { // Errors generated during options parsing.
            System.err.println(e.getLocalizedMessage());
            CommandLineOptionsParser.printHelpMessage();
        }
    }

    /**
     * Runs the selected {@link RunMode} and handles any {@link Exception} that is triggered in it.
     * @param vibeOptions the {@link VibeOptions} as generated through {@link CommandLineOptionsParser)}
     */
    private static void executeRunMode(VibeOptions vibeOptions) {
        try {
            vibeOptions.getRunMode().run(vibeOptions);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (Exception e) { // Errors generated while running the app.
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
