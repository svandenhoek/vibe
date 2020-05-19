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
        protected void runMode() {
            CommandLineOptionsParser.printHelpMessage();
        }
    }, GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES("Retrieves genes for input phenotypes and phenotypes associated to input phenotypes.") {
        @Override
        protected void runMode() throws IOException {
            PhenotypeNetworkCollection phenotypeNetworkCollection = retrieveAssociatedPhenotypes();
            GeneDiseaseCollection geneDiseaseCollection = retrieveDisgenetData(phenotypeNetworkCollection.getPhenotypes());
            List<Gene> genePriority = orderGenes(geneDiseaseCollection);
            writePrioritizedGenesOutput(geneDiseaseCollection, genePriority);
        }
    }, GENES_FOR_PHENOTYPES("Retrieves genes for input phenotypes.") {
        @Override
        protected void runMode() throws Exception {
            GeneDiseaseCollection geneDiseaseCollection = retrieveDisgenetData(retrieveInputPhenotypes());
            List<Gene> genePriority = orderGenes(geneDiseaseCollection);
            writePrioritizedGenesOutput(geneDiseaseCollection, genePriority);
        }
    };

    protected PhenotypeNetworkCollection retrieveAssociatedPhenotypes() {
        vibeOptions.printVerbose("# " + vibeOptions.getPhenotypesRetrieverFactory().getDescription());

        resetTimer();
        PhenotypesRetrievalRunner runner = new PhenotypesRetrievalRunner(
                vibeOptions.getHpoOntology(), vibeOptions.getPhenotypesRetrieverFactory(), vibeOptions.getPhenotypes(),
                vibeOptions.getOntologyMaxDistance());

        try {
            return runner.call();
        } finally {
            printElapsedTime();
            runner.close();
        }
    }

    protected Set<Phenotype> retrieveInputPhenotypes() {
        return vibeOptions.getPhenotypes();
    }

    protected GeneDiseaseCollection retrieveDisgenetData(Set<Phenotype> phenotypes) throws IOException {
        vibeOptions.printVerbose("# Retrieving data from main dataset.");

        resetTimer();
        GeneDiseaseCollectionRetrievalRunner runner = new GeneDiseaseCollectionRetrievalRunner(
                vibeOptions.getVibeTdb(), phenotypes);

        try {
            return runner.call();
        } finally {
            printElapsedTime();
            runner.close();
        }
    }

    protected List<Gene> orderGenes(GeneDiseaseCollection geneDiseaseCollection) {
        vibeOptions.printVerbose("# Ordering genes based on priority.");

        resetTimer();
        GenePrioritizer prioritizer = new HighestSingleDisgenetScoreGenePrioritizer();
        List<Gene> genePriority = prioritizer.sort(geneDiseaseCollection);
        printElapsedTime();

        return genePriority;
    }

    protected void writePrioritizedGenesOutput(GeneDiseaseCollection geneDiseaseCollection, List<Gene> genePriority) throws IOException {
        vibeOptions.printVerbose("# Writing genes to " + vibeOptions.getOutputWriter().target());

        resetTimer();
        vibeOptions.getGenePrioritizedOutputFormatWriterFactory().create(vibeOptions.getOutputWriter(),
                geneDiseaseCollection, genePriority).run();
        printElapsedTime();
    }

    private VibeOptions vibeOptions;

    private String description;

    private Stopwatch stopwatch;

    protected String getDescription() {
        return description;
    }

    RunMode(String description) {
        this.description = description;
        stopwatch = Stopwatch.createStarted();
    }

    public final void run(VibeOptions vibeOptions) throws Exception {
        // Sets the OptionsParser.
        this.vibeOptions = vibeOptions;

        // Prints the RunMode description if verbose.
        this.vibeOptions.printVerbose(getDescription());
        // Runs mode-specific code.
        runMode();
    }

    protected abstract void runMode() throws Exception;

    protected void resetTimer() {
        stopwatch.reset();
        stopwatch.start();
    }

    protected void printElapsedTime() {
        vibeOptions.printVerbose("Elapsed time: " + stopwatch.toString());
    }
}
