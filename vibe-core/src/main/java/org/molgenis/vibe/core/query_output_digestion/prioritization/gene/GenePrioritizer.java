package org.molgenis.vibe.core.query_output_digestion.prioritization.gene;

import org.molgenis.vibe.core.formats.Gene;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.query_output_digestion.prioritization.Prioritizer;

import java.util.List;

/**
 * Returns a {@link Gene} priority from a {@link GeneDiseaseCollection}.
 */
public interface GenePrioritizer extends Prioritizer<Gene, GeneDiseaseCollection> {
    @Override
    List<Gene> sort(GeneDiseaseCollection collection);
}