package org.molgenis.vibe.cli.output.format;

import org.molgenis.vibe.cli.output.target.OutputWriter;
import org.molgenis.vibe.formats.BiologicalEntity;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

/**
 * Interface describing the required methods for all {@link PrioritizedOutputFormatWriter} factories.
 * @param <T> see {@link PrioritizedOutputFormatWriter}
 */
public interface PrioritizedOutputFormatWriterFactory<T extends BiologicalEntity> {
    OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, Prioritizer<T> prioritizer);
}
