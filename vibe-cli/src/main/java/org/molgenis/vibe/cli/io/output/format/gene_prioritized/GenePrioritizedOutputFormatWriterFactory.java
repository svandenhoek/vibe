package org.molgenis.vibe.cli.io.output.format.gene_prioritized;


import org.molgenis.vibe.cli.io.output.ValuesSeparator;
import org.molgenis.vibe.cli.io.output.format.OutputFormatWriter;
import org.molgenis.vibe.cli.io.output.format.PrioritizedOutputFormatWriter;
import org.molgenis.vibe.cli.io.output.format.PrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.cli.io.output.target.OutputWriter;
import org.molgenis.vibe.core.formats.Gene;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;

import java.util.List;

/**
 * A factory for {@link Gene} prioritized {@link PrioritizedOutputFormatWriter}{@code s}.
 */
public enum GenePrioritizedOutputFormatWriterFactory implements PrioritizedOutputFormatWriterFactory<Gene> {
    SIMPLE {
        @Override
        public OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, List<Gene> priority) {
            return new OrderedGenesOutputFormatWriter(outputWriter, priority, ValuesSeparator.COMMA);
        }
    },
    REGULAR_ID {
        @Override
        public OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, List<Gene> priority) {
            return new ResultsPerGeneSeparatedValuesOutputFormatWriterUsingIds(outputWriter, priority, geneDiseaseCollection,
                    ValuesSeparator.TAB, ValuesSeparator.VERTICAL_LINE, ValuesSeparator.COLON, ValuesSeparator.COMMA);
        }
    },
    REGULAR_URI {
        @Override
        public OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, List<Gene> priority) {
            return new ResultsPerGeneSeparatedValuesOutputFormatWriterUsingUris(outputWriter, priority, geneDiseaseCollection,
                    ValuesSeparator.TAB, ValuesSeparator.VERTICAL_LINE, ValuesSeparator.COLON, ValuesSeparator.COMMA);
        }
    }
}
