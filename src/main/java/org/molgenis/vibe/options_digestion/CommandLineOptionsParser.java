package org.molgenis.vibe.options_digestion;

import static java.util.Objects.requireNonNull;

import org.apache.commons.cli.*;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

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
     * @throws InvalidPathException see {@link #digestCommandLine()}
     * @throws IOException see {@link #digestCommandLine()}
     */
    public CommandLineOptionsParser(String[] args) throws ParseException, InvalidPathException, IOException {
        requireNonNull(args);

        parseCommandLine(args);
        digestCommandLine();
        super.checkConfig();
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

//        options.addOption(Option.builder("m")
//                .longOpt("mode")
//                .desc("The application mode to be ran.")
//                .hasArg()
//                .argName("NUMBER")
//                .build());

        options.addOption(Option.builder("p")
                .longOpt("phenotype")
                .desc("A phenotype described using an HPO id. Must include the 'hp:' or 'HP:' prefix.")
                .hasArg()
                .argName("HPO ID")
                .build());

        options.addOption(Option.builder("n")
                .longOpt("ontology")
                .desc("The Human Phenotype Ontology file (.owl).")
                .hasArg()
                .argName("FILE")
                .build());

        options.addOption(Option.builder("nd")
                .longOpt("ontology-distance")
                .desc("The maximum distance to be used for retrieval of connected HPOs.")
                .hasArg()
                .argName("NUMBER")
                .build());

        options.addOption(Option.builder("d")
                .longOpt("disgenet")
                .desc("The directory containing the files for creating a DisGeNET RDF model.")
                .hasArg()
                .argName("DIR")
                .build());

//        options.addOption(Option.builder("dv")
//                .longOpt("disgenetver")
//                .desc("The disgenet dump file release version.")
//                .hasArg()
//                .argName("VERSION")
//                .build());

        options.addOption(Option.builder("o")
                .longOpt("output")
                .desc("The file to write output to.")
                .hasArg()
                .argName("FILE")
                .build());
    }

    /**
     * Prints the help message to stdout.
     */
    public static void printHelpMessage()
    {
        String cmdSyntax = "java -jar vibe-with-dependencies.jar [-h] [-v] -n <FILE> -nd <NUMBER> -d <DIR> -o <FILE> -p <HPO ID> [-p <HPO ID>]...";
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
    private void parseCommandLine(String[] args) throws ParseException {
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
     * @throws InvalidStringFormatException if user-input text does not adhere to required format (regex)
     */
    private void digestCommandLine() throws InvalidPathException, IOException, NumberFormatException, InvalidStringFormatException {
        if(commandLine.hasOption("v")) {
            setVerbose(true);
        }

        if(commandLine.hasOption("p")) {
            // Digests phenotype(s).
            setPhenotypes(commandLine.getOptionValues("p")); // throws InvalidStringFormatException (IllegalArgumentException)
        }

        if(commandLine.hasOption("n")) {
            setHpoOntology(commandLine.getOptionValue("n"));
            if(commandLine.hasOption("nd")) {
                setOntologyMaxDistance(commandLine.getOptionValue("nd"));
            }
        }

        if(commandLine.hasOption("d")) {
//            if(commandLine.hasOption("dv")) {
//                setDisgenet(commandLine.getOptionValue("d"), commandLine.getOptionValue("dv")); // throws InvalidPathException, IOException
//            } else {
            setDisgenet(commandLine.getOptionValue("d"), DisgenetRdfVersion.V5); // throws InvalidPathException, IOException
//            }
        }

        if(commandLine.hasOption("o")) {
            setOutputFile(commandLine.getOptionValue("o"));
        }

        // Selects run mode.
        // Note: current implementation only has 1 mode and therefore currently uses the uncommented code below instead.
//        if(commandLine.hasOption("m")) {
//            setRunMode(RunMode.retrieve(commandLine.getOptionValue("m"))); // throws NumberFormatException
//        }

        // As there currently is only 1 run mode, if ANY argument is given this run mode is automatically selected (-h overrides this).
        if(commandLine.getOptions().length > 0) {
            // Sets run mode.
            setRunMode(RunMode.GET_DISGENET_GENE_GDAS_FOR_PHENOTYPES);
        }

        // If any additional arguments were given that defined a RunMode, -h resets it to NONE so that only the help message
        // is shown before the application quits.
        if(commandLine.hasOption("h")) {
            setRunMode(RunMode.NONE);
        }
    }
}
