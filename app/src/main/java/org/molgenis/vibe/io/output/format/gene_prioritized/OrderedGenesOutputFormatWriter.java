package org.molgenis.vibe.io.output.format.gene_prioritized;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.io.output.ValuesSeparator;
import org.molgenis.vibe.io.output.format.PrioritizedOutputFormatWriter;
import org.molgenis.vibe.io.output.target.FileOutputWriter;
import org.molgenis.vibe.io.output.target.OutputWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A very basic ordered genes output writer.
 */
public class OrderedGenesOutputFormatWriter extends PrioritizedOutputFormatWriter<Gene> {

    /**
     * The order of the {@link Gene}{@code s}.
     */
    private List<Gene> priority;

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
        for(int i = 0; i < priority.size()-1; i++) {
            getOutputWriter().write(priority.get(i).getId() + separator);
        }
        // Writes last gene (without added separator).
        getOutputWriter().write(priority.get(priority.size()-1).getId());
    }
}
