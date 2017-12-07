package org.molgenis;

import org.apache.commons.cli.ParseException;
import org.molgenis.options_digestion.CommandLineOptionsParser;
import org.molgenis.options_digestion.OptionsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;

/**
 * The main application class.
 */
public class Vibe {
    /**
     * The main method for when used as a standalone application.
     * @param args {@link String}{@code []}
     */
    public static void main(String[] args) {
        Vibe app = new Vibe();

        try {
            CommandLineOptionsParser appOptions = new CommandLineOptionsParser(args);

            try {
                app.run(appOptions);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidPathException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The actual processing parts of the application.
     * @param appOptions {@link OptionsParser}
     */
    public void run(OptionsParser appOptions) throws IOException {
    }
}
