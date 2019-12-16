package org.molgenis.vibe.io.output.format;

import org.molgenis.vibe.formats.BiologicalEntity;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.io.output.format.OutputFormatWriter;
import org.molgenis.vibe.io.output.format.PrioritizedOutputFormatWriter;
import org.molgenis.vibe.io.output.target.FileOutputWriter;
import org.molgenis.vibe.io.output.target.OutputWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

import java.nio.file.Path;

/**
 * Interface describing the required methods for all {@link PrioritizedOutputFormatWriter} factories.
 * @param <T> see {@link PrioritizedOutputFormatWriter}
 */
public interface PrioritizedOutputFormatWriterFactory<T extends BiologicalEntity> {
    OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, Prioritizer<T> prioritizer);
}
