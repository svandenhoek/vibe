package org.molgenis.vibe.query_output_digestion.prioritization;

import org.molgenis.vibe.formats.Gene;

import java.util.List;
import java.util.Set;

public abstract class GenePrioritizer extends Prioritizer<Gene> {
    public GenePrioritizer(List<Gene> data) {
        super(data);
    }

    public GenePrioritizer(Set<Gene> data) {
        super(data);
    }
}
