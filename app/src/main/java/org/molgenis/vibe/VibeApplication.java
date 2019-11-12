package org.molgenis.vibe;

import org.molgenis.vibe.options_digestion.CommandLineOptionsParser;

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
            CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);
            try {
                appOptions.getRunMode().run(appOptions);
            } catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
            } catch (Exception e) {
                System.err.println("######## ######## ########");
                System.err.println("An unexpected exception occurred. Please notify the developer (see https://github.com/molgenis/vibe) and supply the stacktrace as seen below.");
                System.err.println("######## ######## ########");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            CommandLineOptionsParser.printHelpMessage();
        }
    }
}
