package org.molgenis.vibe.io.output.format;

import org.molgenis.vibe.formats.BiologicalEntity;
import org.molgenis.vibe.io.output.target.OutputWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

import static java.util.Objects.requireNonNull;

/**
 * An {@link OutputFormatWriter} that writes in a specific format: where the results are prioritized based on a
 * {@link BiologicalEntity}.
 * @param <T> the {@link BiologicalEntity} used by the {@link PrioritizedOutputFormatWriter} for prioritizing the output
 */
public abstract class PrioritizedOutputFormatWriter<T extends BiologicalEntity> extends OutputFormatWriter {
    private Prioritizer<T> prioritizer;

    protected Prioritizer<T> getPrioritizer() {
        return prioritizer;
    }

    public PrioritizedOutputFormatWriter(OutputWriter outputWriter, Prioritizer<T> prioritizer) {
        super(outputWriter);
        this.prioritizer = requireNonNull(prioritizer);
    }
}
