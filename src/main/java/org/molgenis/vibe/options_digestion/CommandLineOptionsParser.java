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
    static {
        options = new Options();
        createOptions();
    }

    /**
     * Variable for generating & digesting the command line options.
     */
    private static Options options;

    /**
     * Variable for digesting the command line.
     */
    private CommandLine commandLine;

    /**
     * Digests the command line options and allows retrieval of useful parameters using getters.
     * @param args the arguments to be digested
     * @throws ParseException see {@link #parseCommandLine(String[])}
     * @throws MissingOptionException see {@link #parseCommandLine(String[])}
     * @throws InvalidPathException see {@link #digestCommandLine()}
     * @throws IOException see {@link #digestCommandLine()}
     */
    public CommandLineOptionsParser(String[] args) throws ParseException, MissingOptionException, InvalidPathException, IOException {
        requireNonNull(args);

        parseCommandLine(args);
        digestCommandLine();
        if(!super.checkConfig()) {
            throw new IOException("The given user-input (such as settings or file locations) does not adhere the run mode.");
        }
    }

    /**
     * Generates the possible command line arguments.
     */
    private static void createOptions() {
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Show help message.")
                .build());

        options.addOption(Option.builder("v")
                .longOpt("verbose")
                .desc("Shows text indicating the processes that are being done.")
                .build());

        options.addOption(Option.builder("m")
                .longOpt("mode")
                .desc("The mode the application should run.\n1: Give an HPO code to retrieve genes matched to it.")
                .hasArg()
                .argName("NUMBER")
                .build());

        options.addOption(Option.builder("p")
                .longOpt("phenotype")
                .desc("An HPO id. Can be either the number only or with the 'hp:' prefix.")
                .hasArg()
                .argName("HPO ID")
                .build());

//        options.addOption(Option.builder("r")
//                .longOpt("rvcf")
//                .desc("The rVCF file that needs to be processed.")
//                .hasArg()
//                .argName("FILE")
//                .build());

        options.addOption(Option.builder("d")
                .longOpt("disgenet")
                .desc("The directory containing the files for creating a DisGeNET RDF model.")
                .hasArg()
                .argName("DIR")
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
    public static void printHelpMessage()
    {
        String cmdSyntax = "java -jar vibe-with-dependencies.jar [-h] -d <DIR> [-dv <VERSION>] -p <HPO ID>";
        String helpHeader = "";
        String helpFooter = "Molgenis VIBE";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, cmdSyntax, helpHeader, options, helpFooter, false);
    }

    /**
     * Parses the command line with the possible arguments.
     *
     * @param args the arguments to be digested
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
     * @throws InvalidPathException if user-input which should be a file/directory could not be converted to {@link Path}
     * @throws IOException if invalid user-input was given (often due to unreadable/missing files)
     * @throws NumberFormatException if user-input which should be an int could not be interpreted as one
     */
    private void digestCommandLine() throws InvalidPathException, IOException, NumberFormatException {
        if(commandLine.hasOption("v")) {
            setVerbose(true);
        }

//        if(commandLine.hasOption("r")) {
//            setRvcfData(commandLine.getOptionValue("r"));
//        }

        if(commandLine.hasOption("p")) {
            setHpoTerms(commandLine.getOptionValues("p")); // throws InvalidStringFormatException (IOException)
        }

        if(commandLine.hasOption("d")) {
            if(commandLine.hasOption("dv")) {
                setDisgenet(commandLine.getOptionValue("d"), commandLine.getOptionValue("dv")); // throws InvalidPathException, IOException
            } else {
                setDisgenet(commandLine.getOptionValue("d"), DisgenetRdfVersion.V5); // throws InvalidPathException, IOException
            }
        }

        // If explicit run mode was given, this is chosen. This also overrides guessed run modes based on given user-input.
        // Note that the help option overrides the run mode.
        if(commandLine.hasOption("m")) {
            setRunMode(RunMode.getMode(commandLine.getOptionValue("m"))); // throws NumberFormatException
        }

        // If any additional arguments were given that defined a RunMode, -h resets it to NONE so that only the help message
        // is shown before the application quits.
        if(commandLine.hasOption("h")) {
            setRunMode(RunMode.NONE);
        }
    }
}
