package org.molgenis.vibe.cli.output.format.gene_prioritized;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.cli.output.ValuesSeparator;
import org.molgenis.vibe.cli.output.format.PrioritizedOutputFormatWriter;
import org.molgenis.vibe.cli.output.target.OutputWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * A very basic ordered genes output writer.
 */
public class OrderedGenesOutputFormatWriter extends PrioritizedOutputFormatWriter<Gene> {
    /**
     * The second level separator to be used to separate values within a single field separated by the primary separator.
     */
    private ValuesSeparator separator;

    public OrderedGenesOutputFormatWriter(OutputWriter writer, Prioritizer<Gene> prioritizer, ValuesSeparator separator) {
        super(writer, prioritizer);
        this.separator = requireNonNull(separator);
    }

    @Override
    protected void generateOutput() throws IOException {
        // Writes all genes except last one (with added separator).
        for(int i = 0; i < getPrioritizer().getPriority().size()-1; i++) {
            getOutputWriter().write(getPrioritizer().getPriority().get(i).getId() + separator);
        }
        // Writes last gene (without added separator).
        getOutputWriter().write(getPrioritizer().getPriority().get(getPrioritizer().getPriority().size()-1).getId());
    }
}
