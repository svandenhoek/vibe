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

    /**
     * Used for some formatting if a command line option has options of its own.
     */
    private static final String argumentOptionsFormat = "|%-12s%s%n";

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
                        String.format(argumentOptionsFormat, "children", "Uses child algorithm.") +
                        String.format(argumentOptionsFormat, "distance", "Uses distance algorithm."))
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
     * @throws InvalidPathException if user-input which should be a file/directory could not be converted to {@link Path}
     * @throws IOException if invalid user-input was given (often due to unreadable/missing files)
     * @throws NumberFormatException if user-input which should be an int could not be interpreted as one
     * @throws InvalidStringFormatException if user-input text does not adhere to required format (regex)
     */

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
    private static void digestCommandLine(CommandLine commandLine, VibeOptions vibeOptions) throws InvalidPathException, IOException, NumberFormatException, InvalidStringFormatException {
        List<String> missing = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // REQUIRED: n arguments > 0.
        // OPTIONAL: Help message.
        if(commandLine.getOptions().length == 0 || commandLine.hasOption("h")) {
            vibeOptions.setRunMode(RunMode.NONE);
            return; // IMPORTANT: Does not process any other arguments from this point.
        }

        // Sets default RunMode.
        vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES);

        // OPTIONAL: Verbose
        if(commandLine.hasOption("v")) {
            vibeOptions.setVerbose(true);
        }

        // REQUIRED: TDB.
        if(commandLine.hasOption("t")) {
            try {
                vibeOptions.setVibeTdb(commandLine.getOptionValue("t"));
            } catch (InvalidPathException | IOException e) {
                errors.add(e.getMessage());
            }
        } else {
            missing.add("-t");
        }

        // REQUIRED: HPO ontology file.
        if(commandLine.hasOption("w")) {
            try {
                vibeOptions.setHpoOntology(commandLine.getOptionValue("w"));
            } catch (InvalidPathException | IOException e) {
                errors.add(e.getMessage());
            }
        } else {
            missing.add("-w");
        }

        // OPTIONAL: HPO ontology retrieval algorithm. Both -n and -m need to be provided.
        if(commandLine.hasOption("n") || commandLine.hasOption("m")) {
            if (commandLine.hasOption("n") && commandLine.hasOption("m")) {
                vibeOptions.setRunMode(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES);
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
            } else {
                if (!commandLine.hasOption("n")) {
                    missing.add("-n");
                }
                if (!commandLine.hasOption("m")) {
                    missing.add("-m");
                }
            }
        }

        // REQUIRED: Phenotypes.
        if(commandLine.hasOption("p")) {
            try {
                vibeOptions.setPhenotypes(commandLine.getOptionValues("p")); // throws InvalidStringFormatException (IllegalArgumentException)
            } catch(InvalidStringFormatException e) {
                errors.add(e.getMessage());
            }
        } else {
            missing.add("-p");
        }

        // If given, sets output file. Otherwise writes to stdout.
        if(commandLine.hasOption("o")) {
            try {
                vibeOptions.setFileOutputWriter(commandLine.getOptionValue("o"));
            } catch(InvalidPathException | FileAlreadyExistsException e) {
                errors.add(e.getMessage());
            }
        } else {
            vibeOptions.setStdoutOutputWriter();
        }

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

        // Processes missing and errors and throws an Exception if any errors were present.
        if(missing.size() > 0) {
            errors.add(0, "Missing arguments: " + StringUtils.join(missing, ", "));
        }
        if(errors.size() > 0) {
            throw new IOException(StringUtils.join(errors, System.lineSeparator()));
        }
    }
}
