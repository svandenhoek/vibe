package org.molgenis.vibe.cli;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.molgenis.vibe.core.GeneDiseaseCollectionRetrievalRunner;
import org.molgenis.vibe.core.PhenotypesRetrievalRunner;
import org.molgenis.vibe.cli.io.options_digestion.CommandLineOptionsParser;
import org.molgenis.vibe.cli.io.options_digestion.VibeOptions;
import org.molgenis.vibe.core.formats.Gene;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.core.query_output_digestion.prioritization.gene.GenePrioritizer;
import org.molgenis.vibe.core.query_output_digestion.prioritization.gene.HighestSingleDisgenetScoreGenePrioritizer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Describes what the application should do.
 */
public enum RunMode {
    NONE("none") {
        @Override
        protected void runMode(VibeOptions vibeOptions, Stopwatch stopwatch) {
            CommandLineOptionsParser.printHelpMessage();
        }
    }, GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES("Retrieves genes for input phenotypes and phenotypes associated to input phenotypes.") {
        @Override
        protected void runMode(VibeOptions vibeOptions, Stopwatch stopwatch) throws IOException {
            PhenotypeNetworkCollection phenotypeNetworkCollection = retrieveAssociatedPhenotypes(vibeOptions, stopwatch);
            GeneDiseaseCollection geneDiseaseCollection = retrieveDisgenetData(vibeOptions, stopwatch, phenotypeNetworkCollection.getPhenotypes());
            List<Gene> genePriority = orderGenes(vibeOptions, stopwatch, geneDiseaseCollection);
            writePrioritizedGenesOutput(vibeOptions, stopwatch, geneDiseaseCollection, genePriority);
        }
    }, GENES_FOR_PHENOTYPES("Retrieves genes for input phenotypes.") {
        @Override
        protected void runMode(VibeOptions vibeOptions, Stopwatch stopwatch) throws Exception {
            GeneDiseaseCollection geneDiseaseCollection = retrieveDisgenetData(vibeOptions, stopwatch, retrieveInputPhenotypes(vibeOptions));
            List<Gene> genePriority = orderGenes(vibeOptions, stopwatch, geneDiseaseCollection);
            writePrioritizedGenesOutput(vibeOptions, stopwatch, geneDiseaseCollection, genePriority);
        }
    };

    private static PhenotypeNetworkCollection retrieveAssociatedPhenotypes(VibeOptions vibeOptions, Stopwatch stopwatch) {
        vibeOptions.printVerbose("# " + vibeOptions.getPhenotypesRetrieverFactory().getDescription());

        resetTimer(stopwatch);
        PhenotypeNetworkCollection phenotypeNetworkCollection = new PhenotypesRetrievalRunner(
                vibeOptions.getHpoOntology(), vibeOptions.getPhenotypesRetrieverFactory(),
                vibeOptions.getPhenotypes(), vibeOptions.getOntologyMaxDistance()).call();
        printElapsedTime(vibeOptions, stopwatch);

        return phenotypeNetworkCollection;
    }

    private static Set<Phenotype> retrieveInputPhenotypes(VibeOptions vibeOptions) {
        return vibeOptions.getPhenotypes();
    }

    private static GeneDiseaseCollection retrieveDisgenetData(VibeOptions vibeOptions, Stopwatch stopwatch, Set<Phenotype> phenotypes) throws IOException {
        vibeOptions.printVerbose("# Retrieving data from main dataset.");

        resetTimer(stopwatch);
        GeneDiseaseCollection geneDiseaseCollection = new GeneDiseaseCollectionRetrievalRunner(
                vibeOptions.getVibeTdb(), phenotypes).call();
        printElapsedTime(vibeOptions, stopwatch);

        return geneDiseaseCollection;
    }

    private static List<Gene> orderGenes(VibeOptions vibeOptions, Stopwatch stopwatch, GeneDiseaseCollection geneDiseaseCollection) {
        vibeOptions.printVerbose("# Ordering genes based on priority.");

        resetTimer(stopwatch);
        GenePrioritizer prioritizer = new HighestSingleDisgenetScoreGenePrioritizer();
        List<Gene> genePriority = prioritizer.sort(geneDiseaseCollection);
        printElapsedTime(vibeOptions, stopwatch);

        return genePriority;
    }

    private static void writePrioritizedGenesOutput(VibeOptions vibeOptions, Stopwatch stopwatch,
                                                    GeneDiseaseCollection geneDiseaseCollection,
                                                    List<Gene> genePriority) throws IOException {
        vibeOptions.printVerbose("# Writing genes to " + vibeOptions.getOutputWriter().target());

        resetTimer(stopwatch);
        vibeOptions.getGenePrioritizedOutputFormatWriterFactory().create(vibeOptions.getOutputWriter(),
                geneDiseaseCollection, genePriority).run();
        printElapsedTime(vibeOptions, stopwatch);
    }

    private VibeOptions vibeOptions;

    private String description;

    private Stopwatch stopwatch;

    protected String getDescription() {
        return description;
    }

    RunMode(String description) {
        this.description = description;
    }

    public final void run(VibeOptions vibeOptions) throws Exception {
        // Sets the OptionsParser.
        this.vibeOptions = vibeOptions;

        // Prints the RunMode description if verbose.
        this.vibeOptions.printVerbose(getDescription());

        // Starts stopwatch.
        this.stopwatch = Stopwatch.createStarted();

        // Runs mode-specific code.
        runMode(vibeOptions, stopwatch);
    }

    protected abstract void runMode(VibeOptions vibeOptions, Stopwatch stopwatch) throws Exception;

    private static void resetTimer(Stopwatch stopwatch) {
        stopwatch.reset();
        stopwatch.start();
    }

    private static void printElapsedTime(VibeOptions vibeOptions, Stopwatch stopwatch) {
        vibeOptions.printVerbose("Elapsed time: " + stopwatch.toString());
    }
}
