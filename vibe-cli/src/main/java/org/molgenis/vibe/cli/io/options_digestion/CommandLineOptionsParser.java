package org.molgenis.vibe.cli.io.options_digestion;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.molgenis.vibe.cli.RunMode;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.cli.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Command line options parser.
 */
public abstract class CommandLineOptionsParser {
    static {
        options = new Options();
        createOptions();
    }

    private CommandLineOptionsParser() {
    }

    /**
     * Used for some formatting if a command line option has options of its own.
     */
    private static final String ARGUMENT_OPTIONS_FORMAT = "|%-12s%s%n";

    /**
     * Variable for generating & digesting the command line options.
     */
    private static Options options;

    /**
     * Digests the command line arguments and stores this in a newly created {@link VibeOptions} instance.
     * @param args the command line arguments
     * @return a {@link VibeOptions} that contains the information from the command line
     * @throws IOException see {@link #digestCommandLine(CommandLine, VibeOptions)}
     * @throws ParseException see {@link #parseCommandLine(String[])}
     */
    public static VibeOptions parse(String[] args) throws IOException, ParseException {
        VibeOptions vibeOptions = new VibeOptions();
        parse(args, vibeOptions);
        return vibeOptions;
    }

    /**
     * Digests the command line arguments and stores this in the supplied {@link VibeOptions} instance.
     * @param args the command line arguments
     * @param vibeOptions in which the digested command line arguments should be stored
     * @throws IOException see {@link #digestCommandLine(CommandLine, VibeOptions)}
     * @throws ParseException see {@link #parseCommandLine(String[])}
     */
    public static void parse(String[] args, VibeOptions vibeOptions) throws IOException, ParseException {
        CommandLine commandLine = parseCommandLine(args);
        digestCommandLine(commandLine, vibeOptions);
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

        options.addOption(Option.builder("p")
                .longOpt("phenotype")
                .desc("A phenotype described using an HPO id. Must include the 'hp:' or 'HP:' prefix.")
                .hasArg()
                .argName("HPO ID")
                .build());

        options.addOption(Option.builder("w")
                .longOpt("ontology")
                .desc("The Human Phenotype Ontology file (.owl). Can be given without -n or -m, but has no use then.")
                .hasArg()
                .argName("FILE")
                .build());

        options.addOption(Option.builder("n")
                .longOpt("ontology-algorithm")
                .desc("The ontology algorithm to be used for related HPO retrieval:" + System.lineSeparator() +
                        String.format(ARGUMENT_OPTIONS_FORMAT, "children", "Uses child algorithm.") +
                        String.format(ARGUMENT_OPTIONS_FORMAT, "distance", "Uses distance algorithm."))
                .hasArg()
                .argName("NAME")
                .build());

        options.addOption(Option.builder("m")
                .longOpt("ontology-max")
                .desc("The maximum distance to be used for the ontology algorithm.")
                .hasArg()
                .argName("NUMBER")
                .build());

        options.addOption(Option.builder("t")
                .longOpt("tdb")
                .desc("The directory containing the DisGeNET RDF model as a Apache Jena TDB.")
                .hasArg()
                .argName("DIR")
                .build());

        options.addOption(Option.builder("o")
                .longOpt("output")
                .desc("The file to write output to.")
                .hasArg()
                .argName("FILE")
                .build());

        options.addOption(Option.builder("l")
                .longOpt("simple-output")
                .desc("Simple output format (file only contains separated gene symbols)")
                .build());

        options.addOption(Option.builder("u")
                .longOpt("uri")
                .desc("Returns uri's instead of id's for certain output fields" + System.lineSeparator() +
                        "(doesn't work in combination with -l).")
                .build());
    }

    /**
     * Prints the help message to stdout.
     */
    public static void printHelpMessage() {
        String cmdSyntax = "java -jar vibe-with-dependencies.jar [-h] [-v] -t <FILE> -w <FILE> [-n <NAME> -m <NUMBER>] [-o <FILE>] [-l] [-u] -p <HPO ID> [-p <HPO ID>]...";
        String helpHeader = "";
        String helpFooter = "Molgenis VIBE";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, cmdSyntax, helpHeader, options, helpFooter, false);
    }

    /**
     * Parses the command line with the possible arguments.
     *
     * @param args the arguments to be digested
     * @return the parsed command line
     * @throws ParseException see {@link DefaultParser#parse(Options, String[])}
     */
    private static CommandLine parseCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return(parser.parse(options, args));
    }

    /**
     * Digests the parsed command line arguments.
     *
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     * @throws InvalidPathException if user-input which should be a file/directory could not be converted to {@link Path}
     * @throws IOException if invalid user-input was given (often due to unreadable/missing files)
     * @throws NumberFormatException if user-input which should be an int could not be interpreted as one
     * @throws InvalidStringFormatException if user-input text does not adhere to required format (regex)
     */
    private static void digestCommandLine(CommandLine commandLine, VibeOptions vibeOptions)
            throws InvalidPathException, IOException, NumberFormatException, InvalidStringFormatException {
        // Sets RunMode.
        defineRunMode(commandLine, vibeOptions);

        // If RunMode is NONE, stops digesting command line.
        if(vibeOptions.getRunMode() == RunMode.NONE) {
            return; // IMPORTANT: Does not process any other arguments from this point.
        }

        // Checks for missing arguments, and if so, throws IOException.
        checkForMissingArguments(commandLine, vibeOptions);

        // Stores errors produced by the given arguments.
        List<String> errors = new ArrayList<>();

        // Digests the databases needed be the application.
        digestDatabases(commandLine, vibeOptions, errors);

        // Digests the input phenotypes.
        digestInputPhenotypes(commandLine, vibeOptions, errors);

        // Digests output arguments (including logging/verbosity).
        digestOutputArguments(commandLine, vibeOptions, errors);

        // Digests arguments related to the HPO ontology traversal.
        if(vibeOptions.getRunMode() == RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES) {
            digestHpoOntologyArguments(commandLine, vibeOptions, errors);
        }

        // Checks if any errors were created, and if so, throws IOException.
        if(errors.size() > 0) {
            throw new IOException(StringUtils.join(errors, System.lineSeparator()));
        }
    }

    /**
     * Using the given arguments, sets the correct {@link RunMode} in {@link VibeOptions}.
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     */
    private static void defineRunMode(CommandLine commandLine, VibeOptions vibeOptions) {
        if(commandLine.getOptions().length == 0 || commandLine.hasOption("h")) {
            vibeOptions.setRunMode(RunMode.NONE);
        } else if (commandLine.hasOption("n") || commandLine.hasOption("m")) {
            vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES);
        } else {
            vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        }
    }

    /**
     * Validates whether the given arguments adhere to the requirements (no missing required arguments, when optional
     * arguments require other arguments check if these are present as well, etc.)
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     * @return a {@link List} containing all missing arguments, or an empty {@link List} if no arguments are missing
     */
    private static void checkForMissingArguments(CommandLine commandLine, VibeOptions vibeOptions) throws IOException {
        // Stores the missing expected arguments.
        List<String> missing = new ArrayList<>();

        // Arguments that are always required.
        String[] requiredArguments = new String[] {
                "t", // TDB
                "w", // HPO owl
                "p", // 1 or more phenotypes
        };

        // Checks which of the always required arguments are missing.
        for(String argument : requiredArguments) {
            if(!commandLine.hasOption(argument)) {
                missing.add("-" + argument);
            }
        }

        // Checks arguments specific for RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES.
        if(vibeOptions.getRunMode() == RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES) {
            if (!commandLine.hasOption("n")) {
                missing.add("-n");
            }
            if (!commandLine.hasOption("m")) {
                missing.add("-m");
            }
        }

        // Generates IOException with missing arguments if any were found.
        if( missing.size() > 0 ) {
            throw new IOException("Missing arguments: " + StringUtils.join(missing, ", "));
        }
    }

    /**
     * Digests the required arguments.
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     * @param errors a {@link List} to add error messages to if any occur
     */
    private static void digestDatabases(CommandLine commandLine, VibeOptions vibeOptions, List<String> errors) {
        // TDB.
        try {
            vibeOptions.setVibeTdb(commandLine.getOptionValue("t"));
        } catch (InvalidPathException | IOException e) {
            errors.add(e.getMessage());
        }

        // HPO ontology file.
        try {
            vibeOptions.setHpoOntology(commandLine.getOptionValue("w"));
        } catch (InvalidPathException | IOException e) {
            errors.add(e.getMessage());
        }
    }

    /**
     * Digests the input phenotypes to be processed.
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     * @param errors a {@link List} to add error messages to if any occur
     */
    private static void digestInputPhenotypes(CommandLine commandLine, VibeOptions vibeOptions, List<String> errors) {
        try {
            vibeOptions.setPhenotypes(commandLine.getOptionValues("p")); // throws InvalidStringFormatException (IllegalArgumentException)
        } catch(InvalidStringFormatException e) {
            errors.add(e.getMessage());
        }
    }

    /**
     * Digests arguments related to the output generation (output format/target), including logging/verbosity.
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     * @param errors a {@link List} to add error messages to if any occur
     */
    private static void digestOutputArguments(CommandLine commandLine, VibeOptions vibeOptions, List<String> errors) {
        // Whether tool should be verbose.
        vibeOptions.setVerbose(commandLine.hasOption("v"));

        // Defines output format.
        if(commandLine.hasOption("l")) {
            vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.SIMPLE);
        } else {
            if(commandLine.hasOption("u")) {
                vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_URI);
            } else {
                vibeOptions.setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR_ID);
            }
        }

        // Defines output target.
        if(commandLine.hasOption("o")) {
            try {
                vibeOptions.setFileOutputWriter(commandLine.getOptionValue("o"));
            } catch(InvalidPathException | FileAlreadyExistsException e) {
                errors.add(e.getMessage());
            }
        } else {
            vibeOptions.setStdoutOutputWriter();
        }
    }

    /**
     * Digests arguments related to the HPO ontology traversal.
     * @param commandLine the parsed command line
     * @param vibeOptions in which the parsed command line information should be stored
     * @param errors a {@link List} to add error messages to if any occur
     */
    private static void digestHpoOntologyArguments(CommandLine commandLine, VibeOptions vibeOptions, List<String> errors) {
        try {
            vibeOptions.setPhenotypesRetrieverFactory(commandLine.getOptionValue("n"));
        } catch (EnumConstantNotPresentException e) {
            errors.add(commandLine.getOptionValue("n") + " is not a valid HPO retrieval algorithm.");
        }
        try {
            vibeOptions.setOntologyMaxDistance(commandLine.getOptionValue("m"));
        } catch (IllegalArgumentException e) {
            errors.add(commandLine.getOptionValue("m") + " is not a valid HPO retrieval algorithm distance (must be a number >= 0).");
        }
    }
}
