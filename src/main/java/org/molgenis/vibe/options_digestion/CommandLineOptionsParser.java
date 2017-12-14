package org.molgenis.vibe.options_digestion;

import static java.util.Objects.requireNonNull;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Command line options parser.
 */
public class CommandLineOptionsParser extends OptionsParser {
    /**
     * Variable for generating & digesting the command line options.
     */
    private Options options = new Options();

    /**
     * Variable for digesting the command line.
     */
    private CommandLine commandLine;

    /**
     * Digests the command line options and allows retrieval of useful parameters using getters.
     * @param args {@link String}{@code []}
     * @throws ParseException see {@link #parseCommandLine(String[])}
     * @throws MissingOptionException see {@link #parseCommandLine(String[])}
     * @throws InvalidPathException see {@link #digestCommandLine()}
     * @throws IOException see {@link #digestCommandLine()}
     */
    public CommandLineOptionsParser(String[] args) throws ParseException, MissingOptionException, InvalidPathException, IOException {
        requireNonNull(args);

        createOptions();
        parseCommandLine(args);
        digestCommandLine();
    }

    /**
     * Generates the possible command line arguments.
     */
    private void createOptions() {
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Show help message.")
                .build());

        options.addOption(Option.builder("r")
                .longOpt("rvcf")
                .desc("The rVCF file that needs to be processed.")
                .hasArg()
                .argName("FILE")
                .required()
                .build());

        options.addOption(Option.builder("d")
                .longOpt("disgenet")
                .desc("The disgenet dump file.")
                .hasArg()
                .argName("FILE")
                .required()
                .build());

        options.addOption(Option.builder("dv")
                .longOpt("disgenetver")
                .desc("The disgenet dump file release version.")
                .hasArg()
                .argName("VERSION")
                .build());
    }

    /**
     * Prints the help message to stdout.
     */
    public void printHelpMessage()
    {
        String cmdSyntax = "java jar ViveApplication.jar [-h] -r <FILE> -d <FILE>";
        String helpHeader = "";
        String helpFooter = "Molgenis VIBE";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, cmdSyntax, helpHeader, this.options, helpFooter, false);
    }

    /**
     * Parses the command line with the possible arguments.
     *
     * @param args {@link String}{@code []}
     * @throws ParseException see {@link DefaultParser#parse(Options, String[])}
     * @throws MissingOptionException if not all required arguments are present
     */
    private void parseCommandLine(String[] args) throws ParseException, MissingOptionException {
        // Creates parser.
        CommandLineParser parser = new DefaultParser();

        // Execute the parsing. Throws a MissingOptionException if not all required options are present.
        commandLine = parser.parse(options, args);
    }

    /**
     * Digests the parsed command line arguments.
     *
     * @throws InvalidPathException see {@link #setDisgenetDump(String)} or {@link #setRvcfData(String)}
     * @throws IOException see {@link #setDisgenetDump(Path)} or {@link #setRvcfData(Path)}
     */
    private void digestCommandLine() throws InvalidPathException, IOException {
        if(commandLine.hasOption("h")) {
            printHelpMessage();
        }
        if(commandLine.hasOption("r")) {
            setRvcfData(commandLine.getOptionValue("r"));
        }
        if(commandLine.hasOption("d")) {
            setDisgenetDump(commandLine.getOptionValue("d"));
        }
        if(commandLine.hasOption("dv")) {
            setDisgenetRdfVersion(commandLine.getOptionValue("dv"));
        }
    }
}
