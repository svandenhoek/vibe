package org.molgenis.vibe.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.vibe.cli.io.options_digestion.VibeOptions;
import org.molgenis.vibe.cli.io.output.format.gene_prioritized.GenePrioritizedOutputFormatWriterFactory;
import org.molgenis.vibe.cli.io.output.target.StdoutOutputWriter;
import org.molgenis.vibe.core.formats.Phenotype;
import org.molgenis.vibe.core.io.input.ModelReaderFactory;
import org.molgenis.vibe.core.io.input.VibeDatabase;
import org.molgenis.vibe.core.ontology_processing.PhenotypesRetrieverFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RunModeIT {
    @Mock private VibeOptions mockedVibeOptions;

    @Test
    void testIfHelpWorks() throws IOException {
        // Only tests if everything works as a whole. Actual data processing validation is done in their own tests.
        Assertions.assertDoesNotThrow(() -> RunMode.HELP.run(mockedVibeOptions));
    }

    @Test
    void testIfVersionWorks() throws IOException {
        // Only tests if everything works as a whole. Actual data processing validation is done in their own tests.
        Assertions.assertDoesNotThrow(() -> RunMode.VERSION.run(mockedVibeOptions));
    }

    @Test
    void testIfGenesForPhenotypesWorks() throws IOException {
        when(mockedVibeOptions.getPhenotypes())
                .thenReturn(new HashSet<>(Arrays.asList(new Phenotype("hp:0008438"))));

        when(mockedVibeOptions.getVibeDatabase())
                .thenReturn(new VibeDatabase(TestData.HDT.getFullPath(), ModelReaderFactory.HDT));

        when(mockedVibeOptions.getGenePrioritizedOutputFormatWriterFactory())
                .thenReturn(GenePrioritizedOutputFormatWriterFactory.SIMPLE);
        when(mockedVibeOptions.getOutputWriter()).thenReturn(new StdoutOutputWriter());

        // Only tests if everything works as a whole. Actual data processing validation is done in their own tests.
        Assertions.assertDoesNotThrow(() -> RunMode.GENES_FOR_PHENOTYPES.run(mockedVibeOptions));
    }

    @Test
    void testIfGenesForPhenotypesWithAssociatedPhenotypesWorks() throws IOException {
        when(mockedVibeOptions.getPhenotypes())
                .thenReturn(new HashSet<>(Arrays.asList(new Phenotype("hp:0008438"))));
        when(mockedVibeOptions.getHpoOntology()).thenReturn(TestData.HPO_OWL.getFullPath());
        when(mockedVibeOptions.getPhenotypesRetrieverFactory()).thenReturn(PhenotypesRetrieverFactory.CHILDREN);
        when(mockedVibeOptions.getOntologyMaxDistance()).thenReturn(0);

        when(mockedVibeOptions.getVibeDatabase())
                .thenReturn(new VibeDatabase(TestData.HDT.getFullPath(), ModelReaderFactory.HDT));

        when(mockedVibeOptions.getGenePrioritizedOutputFormatWriterFactory())
                .thenReturn(GenePrioritizedOutputFormatWriterFactory.SIMPLE);
        when(mockedVibeOptions.getOutputWriter()).thenReturn(new StdoutOutputWriter());

        // Only tests if everything works as a whole. Actual data processing validation is done in their own tests.
        Assertions.assertDoesNotThrow(
                () -> RunMode.GENES_FOR_PHENOTYPES_WITH_ASSOCIATED_PHENOTYPES.run(mockedVibeOptions)
        );
    }
}
