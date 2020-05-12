package org.molgenis.vibe.cli.io.output.format.gene_prioritized;

import org.molgenis.vibe.core.formats.*;
import org.molgenis.vibe.cli.io.output.ValuesSeparator;
import org.molgenis.vibe.cli.io.output.target.OutputWriter;
import org.molgenis.vibe.core.query_output_digestion.prioritization.Prioritizer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link ResultsPerGeneSeparatedValuesOutputFormatWriter} where {@link BiologicalEntity#getId()} is used for generating the output.
 */
public class ResultsPerGeneSeparatedValuesOutputFormatWriterUsingIds extends ResultsPerGeneSeparatedValuesOutputFormatWriter {

    public ResultsPerGeneSeparatedValuesOutputFormatWriterUsingIds(OutputWriter writer, Prioritizer<Gene> prioritizer,
                                                                   GeneDiseaseCollection collection, ValuesSeparator primarySeparator,
                                                                   ValuesSeparator keyValuePairSeparator, ValuesSeparator keyValueSeparator,
                                                                   ValuesSeparator valuesSeparator) {
        super(writer, prioritizer, collection, primarySeparator, keyValuePairSeparator, keyValueSeparator, valuesSeparator);
    }

    @Override
    protected String writeGene(Gene gene) {
        return gene.getId();
    }

    @Override
    protected String writeGeneSymbol(Gene gene) {
        return gene.getSymbol().getId();
    }

    @Override
    protected String writeDisease(Disease disease) {
        return disease.getId();
    }

    @Override
    protected List<String> writeEvidence(List<PubmedEvidence> pubmedEvidenceList) {
        return pubmedEvidenceList.stream().map(PubmedEvidence::getId).collect(Collectors.toList());
    }
}
