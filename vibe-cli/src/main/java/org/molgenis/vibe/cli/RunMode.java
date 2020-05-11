package org.molgenis.vibe.cli;

import org.apache.jena.ext.com.google.common.base.Stopwatch;
import org.molgenis.vibe.GeneDiseaseCollectionRetrievalRunner;
import org.molgenis.vibe.PhenotypesRetrievalRunner;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.PhenotypeNetworkCollection;
import org.molgenis.vibe.cli.output.format.OutputFormatWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.GenePrioritizer;

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
            GeneDiseaseCollection geneDiseaseCollection = retrieveDisgenetData(getAppOptions().getPhenotypes());
            GenePrioritizer prioritizer = orderGenes(geneDiseaseCollection);
            writePrioritizedGenesOutput(geneDiseaseCollection, prioritizer);
        }
    };

    protected PhenotypeNetworkCollection retrieveAssociatedPhenotypes() {
        getAppOptions().printVerbose("# " + getAppOptions().getPhenotypesRetrieverFactory().getDescription());
        PhenotypesRetrievalRunner runner = new PhenotypesRetrievalRunner(getAppOptions().getHpoOntology(),
                getAppOptions().getPhenotypesRetrieverFactory(), getAppOptions().getPhenotypes(),
                getAppOptions().getOntologyMaxDistance());
        printElapsedTime();
        return runner.call();
    }

    protected GeneDiseaseCollection retrieveDisgenetData(Set<Phenotype> phenotypes) throws IOException {
        getAppOptions().printVerbose("# Retrieving data from main dataset.");
        GeneDiseaseCollectionRetrievalRunner runner = new GeneDiseaseCollectionRetrievalRunner(getAppOptions().getDatabase(), phenotypes);
        printElapsedTime();
        return runner.call();
    }

    protected GenePrioritizer orderGenes(GeneDiseaseCollection geneDiseaseCollection) {
        getAppOptions().printVerbose("# Ordering genes based on priority.");
        GenePrioritizer prioritizer = getAppOptions().getGenePrioritizerFactory().create(geneDiseaseCollection);
        prioritizer.run();
        printElapsedTime();

        return prioritizer;
    }

    protected void writePrioritizedGenesOutput(GeneDiseaseCollection geneDiseaseCollection, GenePrioritizer prioritizer) throws IOException {
        getAppOptions().printVerbose("# Writing genes to " + getAppOptions().getOutputWriter().target());
        OutputFormatWriter outputFormatWriter = getAppOptions().getGenePrioritizedOutputFormatWriterFactory().create(getAppOptions().getOutputWriter(), geneDiseaseCollection, prioritizer);
        outputFormatWriter.run();
        printElapsedTime();
    }

    private OptionsParser appOptions;

    private String description;

    private Stopwatch stopwatch;

    protected OptionsParser getAppOptions() {
        return appOptions;
    }

    protected void setAppOptions(OptionsParser appOptions) {
        this.appOptions = appOptions;
    }

    protected String getDescription() {
        return description;
    }

    RunMode(String description) {
        this.description = description;
        stopwatch = Stopwatch.createStarted();
    }

    public final void run(OptionsParser appOptions) throws Exception {
        // Sets the OptionsParser.
        setAppOptions(appOptions);
        // Prints the RunMode description if verbose.
        getAppOptions().printVerbose(getDescription());
        // Runs mode-specific code.
        runMode();
    }

    protected abstract void runMode() throws Exception;

    protected void printElapsedTime() {
        getAppOptions().printVerbose("Elapsed time: " + stopwatch.toString());
        // Resets stopwatch.
        stopwatch.reset();
        stopwatch.start();
    }
}
