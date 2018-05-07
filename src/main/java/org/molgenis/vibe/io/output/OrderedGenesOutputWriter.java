package org.molgenis.vibe.io.output;

import org.molgenis.vibe.formats.Gene;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A very basic ordered genes output writer.
 */
public class OrderedGenesOutputWriter extends FileOutputWriter {

    /**
     * The order of the {@link Gene}{@code s}.
     */
    private List<Gene> priority;

    /**
     * The second level separator to be used to separate values within a single field separated by the primary separator.
     */
    private ValuesSeparator separator;


    public OrderedGenesOutputWriter(Path path, List<Gene> priority, ValuesSeparator separator) {
        super(path);
        this.priority = requireNonNull(priority);
        this.separator = requireNonNull(separator);
    }

    @Override
    public void run() throws IOException {
        BufferedWriter writer = getWriter();

        // Writes all genes except last one (with added separator).
        for(int i = 0; i < priority.size()-1; i++) {
            writer.write(priority.get(i).getSymbol() + separator);
        }
        // Writes last gene (without added separator).
        writer.write(priority.get(priority.size()-1).getSymbol());

        closeWriter();
    }
}
