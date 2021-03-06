package org.molgenis.vibe.cli.io.output.format.gene_prioritized;

import org.molgenis.vibe.core.formats.*;
import org.molgenis.vibe.cli.io.output.ValuesSeparator;
import org.molgenis.vibe.cli.io.output.target.OutputWriter;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link ResultsPerGeneSeparatedValuesOutputFormatWriter} where {@link BiologicalEntity#getUri()} is used for generating the output.
 */
public class ResultsPerGeneSeparatedValuesOutputFormatWriterUsingUris extends ResultsPerGeneSeparatedValuesOutputFormatWriter {

    public ResultsPerGeneSeparatedValuesOutputFormatWriterUsingUris(OutputWriter writer, List<Gene> priority,
                                                                   GeneDiseaseCollection collection, ValuesSeparator primarySeparator,
                                                                   ValuesSeparator keyValuePairSeparator, ValuesSeparator keyValueSeparator,
                                                                   ValuesSeparator valuesSeparator) {
        super(writer, priority, collection, primarySeparator, keyValuePairSeparator, keyValueSeparator, valuesSeparator);
    }

    @Override
    protected String writeGene(Gene gene) {
        return gene.getUri().toString();
    }

    @Override
    protected String writeGeneSymbol(Gene gene) {
        return gene.getSymbol().getUri().toString();
    }

    @Override
    protected String writeDisease(Disease disease) {
        return disease.getUri().toString();
    }

    @Override
    protected List<String> writeEvidence(List<PubmedEvidence> pubmedEvidenceList) {
        return pubmedEvidenceList.stream().map(PubmedEvidence::getUri).map(URI::toString).collect(Collectors.toList());
    }
}
