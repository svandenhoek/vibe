package org.molgenis.vibe.cli.io.output.format;

import org.molgenis.vibe.core.formats.BiologicalEntity;
import org.molgenis.vibe.cli.io.output.target.OutputWriter;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * An {@link OutputFormatWriter} that writes in a specific format: where the results are prioritized based on a
 * {@link BiologicalEntity}.
 * @param <T> the {@link BiologicalEntity} used by the {@link PrioritizedOutputFormatWriter} for prioritizing the output
 */
public abstract class PrioritizedOutputFormatWriter<T extends BiologicalEntity> extends OutputFormatWriter {
    private List<T> priority;

    protected List<T> getPriority() {
        return priority;
    }

    public PrioritizedOutputFormatWriter(OutputWriter outputWriter, List<T> priority) {
        super(outputWriter);
        this.priority = requireNonNull(priority);
    }
}
