package org.molgenis.vibe.cli;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.molgenis.vibe.core.GeneDiseaseCollectionRetrievalRunner;
import org.molgenis.vibe.core.PhenotypesRetrievalRunner;
import org.molgenis.vibe.cli.input.CommandLineOptionsParser;
import org.molgenis.vibe.cli.input.VibeOptions;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.cli.output.format.OutputFormatWriter;
import org.molgenis.vibe.core.query_output_digestion.prioritization.gene.GenePrioritizer;

import java.io.IOException;
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
            GenePrioritizer prioritizer = orderGenes(geneDiseaseCollection);
            writePrioritizedGenesOutput(geneDiseaseCollection, prioritizer);
        }
    }, GENES_FOR_PHENOTYPES("Retrieves genes for input phenotypes.") {
        @Override
        protected void runMode() throws Exception {
            GeneDiseaseCollection geneDiseaseCollection = retrieveDisgenetData(retrieveInputPhenotypes());
            GenePrioritizer prioritizer = orderGenes(geneDiseaseCollection);
            writePrioritizedGenesOutput(geneDiseaseCollection, prioritizer);
        }
    };

    protected PhenotypeNetworkCollection retrieveAssociatedPhenotypes() {
        vibeOptions.printVerbose("# " + vibeOptions.getPhenotypesRetrieverFactory().getDescription());
        PhenotypesRetrievalRunner runner = new PhenotypesRetrievalRunner(vibeOptions.getHpoOntology(),
                vibeOptions.getPhenotypesRetrieverFactory(), vibeOptions.getPhenotypes(),
                vibeOptions.getOntologyMaxDistance());
        printElapsedTime();
        return runner.call();
    }

    protected Set<Phenotype> retrieveInputPhenotypes() {
        return vibeOptions.getPhenotypes();
    }

    protected GeneDiseaseCollection retrieveDisgenetData(Set<Phenotype> phenotypes) throws IOException {
        vibeOptions.printVerbose("# Retrieving data from main dataset.");
        GeneDiseaseCollectionRetrievalRunner runner = new GeneDiseaseCollectionRetrievalRunner(vibeOptions.getVibeTdb(), phenotypes);
        printElapsedTime();
        return runner.call();
    }

    protected GenePrioritizer orderGenes(GeneDiseaseCollection geneDiseaseCollection) {
        vibeOptions.printVerbose("# Ordering genes based on priority.");
        GenePrioritizer prioritizer = vibeOptions.getGenePrioritizerFactory().create(geneDiseaseCollection);
        prioritizer.run();
        printElapsedTime();

        return prioritizer;
    }

    protected void writePrioritizedGenesOutput(GeneDiseaseCollection geneDiseaseCollection, GenePrioritizer prioritizer) throws IOException {
        vibeOptions.printVerbose("# Writing genes to " + vibeOptions.getOutputWriter().target());
        OutputFormatWriter outputFormatWriter = vibeOptions.getGenePrioritizedOutputFormatWriterFactory().create(vibeOptions.getOutputWriter(), geneDiseaseCollection, prioritizer);
        outputFormatWriter.run();
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

    protected void printElapsedTime() {
        vibeOptions.printVerbose("Elapsed time: " + stopwatch.toString());
        // Resets stopwatch.
        stopwatch.reset();
        stopwatch.start();
    }
}
