package org.molgenis.vibe.io.options_digestion;

import static java.util.Objects.requireNonNull;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.molgenis.vibe.RunMode;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Command line options parser.
 */
public class CommandLineOptionsParser extends OptionsParser {
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
    public CommandLineOptionsParser(String[] args) throws ParseException, IOException {
        requireNonNull(args);

        parseCommandLine(args);
        digestCommandLine();
        if(!super.checkConfig()) {
            throw new IOException("Something went wrong during app configuration. Please contact the developer.");
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
    }

    /**
     * Prints the help message to stdout.
     */
    public static void printHelpMessage() {
        String cmdSyntax = "java -jar vibe-with-dependencies.jar [-h] [-v] -t <FILE> [-w <FILE> -n <NAME> -m <NUMBER>] [-o <FILE>] [-l] -p <HPO ID> [-p <HPO ID>]...";
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
        List<String> missing = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // If no arguments were given, RunMode is set to NONE.
        if(commandLine.getOptions().length == 0) {
            setRunMode(RunMode.NONE);
            return; // IMPORTANT: Does not process any other arguments from this point.
        } else { // Sets default RunMode.
            setRunMode(RunMode.GENES_FOR_PHENOTYPES);
        }

        // OPTIONAL: Only show help message (set RunMode to NONE).
        if(commandLine.hasOption("h")) {
            setRunMode(RunMode.NONE);
            return; // IMPORTANT: Does not process any other arguments from this point.
        }

        // OPTIONAL: Verbose
        if(commandLine.hasOption("v")) {
            setVerbose(true);
        }

        // REQUIRED: TDB.
        if(commandLine.hasOption("t")) {
            try {
                setDatabase(commandLine.getOptionValue("t"));
            } catch (InvalidPathException | IOException e) {
                errors.add(e.getMessage());
            }
        } else {
            missing.add("-t");
        }

        // OPTIONAL: HPO ontology file. Required for -n & -m.
        if(commandLine.hasOption("w")) {
            try {
                setHpoOntology(commandLine.getOptionValue("w"));
            } catch (InvalidPathException | IOException e) {
                errors.add(e.getMessage());
            }
        }

        // OPTIONAL: HPO ontology retrieval algorithm. Both -n and -m need to be provided as well as -w.
        if(commandLine.hasOption("n") || commandLine.hasOption("m")) {
            if (commandLine.hasOption("w") && commandLine.hasOption("n") && commandLine.hasOption("m")) {
                setRunMode(RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES);
                try {
                    setPhenotypesRetrieverFactory(commandLine.getOptionValue("n"));
                    setOntologyMaxDistance(commandLine.getOptionValue("m"));
                } catch (EnumConstantNotPresentException e) {
                    errors.add(e.getMessage());
                }
            } else {
                if (!commandLine.hasOption("w")) {
                    missing.add("-w");
                }
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
                setPhenotypes(commandLine.getOptionValues("p")); // throws InvalidStringFormatException (IllegalArgumentException)
            } catch(InvalidStringFormatException e) {
                errors.add(e.getMessage());
            }
        } else {
            missing.add("-p");
        }

        // If given, sets output file. Otherwise writes to stdout.
        if(commandLine.hasOption("o")) {
            try {
                setFileOutputWriter(commandLine.getOptionValue("o"));
            } catch(InvalidPathException | FileAlreadyExistsException e) {
                errors.add(e.getMessage());
            }
        } else {
            setStdoutOutputWriter();
        }

        // Defines output format.
        if(commandLine.hasOption("l")) {
            setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.SIMPLE);
        } else {
            setGenePrioritizedOutputFormatWriterFactory(GenePrioritizedOutputFormatWriterFactory.REGULAR);
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
