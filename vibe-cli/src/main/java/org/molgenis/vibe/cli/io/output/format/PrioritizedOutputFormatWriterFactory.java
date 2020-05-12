package org.molgenis.vibe.cli.io.output.format;

import org.molgenis.vibe.cli.io.output.target.OutputWriter;
import org.molgenis.vibe.core.formats.BiologicalEntity;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.query_output_digestion.prioritization.Prioritizer;

/**
 * Interface describing the required methods for all {@link PrioritizedOutputFormatWriter} factories.
 * @param <T> see {@link PrioritizedOutputFormatWriter}
 */
public interface PrioritizedOutputFormatWriterFactory<T extends BiologicalEntity> {
    OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, Prioritizer<T> prioritizer);
}
