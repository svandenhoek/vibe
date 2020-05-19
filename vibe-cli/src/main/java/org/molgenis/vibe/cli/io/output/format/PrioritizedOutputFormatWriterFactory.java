package org.molgenis.vibe.cli.io.output.format;

import org.molgenis.vibe.cli.io.output.target.OutputWriter;
import org.molgenis.vibe.core.formats.BiologicalEntity;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;

import java.util.List;

/**
 * Interface describing the required methods for all {@link PrioritizedOutputFormatWriter} factories.
 * @param <T> see {@link PrioritizedOutputFormatWriter}
 */
public interface PrioritizedOutputFormatWriterFactory<T extends BiologicalEntity> {
    OutputFormatWriter create(OutputWriter outputWriter, GeneDiseaseCollection geneDiseaseCollection, List<T> priority);
}
