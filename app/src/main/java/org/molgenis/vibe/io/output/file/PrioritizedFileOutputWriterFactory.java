package org.molgenis.vibe.io.output.file;

import org.molgenis.vibe.formats.BiologicalEntity;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

import java.nio.file.Path;

public interface PrioritizedFileOutputWriterFactory<T extends BiologicalEntity> {
    FileOutputWriter create(Path path, GeneDiseaseCollection geneDiseaseCollection, Prioritizer<T> prioritizer);
}
