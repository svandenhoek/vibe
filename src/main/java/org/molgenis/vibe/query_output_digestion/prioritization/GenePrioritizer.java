package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.Gene;

import java.util.List;
import java.util.Set;

/**
 * Generates a priority order for a given collection ({@link List} or {@link Set}) of {@link Gene}{@code s}.
 */
public abstract class GenePrioritizer extends Prioritizer<Gene> {
    public GenePrioritizer(List<Gene> data) {
        super(data);
    }

    public GenePrioritizer(Set<Gene> data) {
        super(data);
    }
}
