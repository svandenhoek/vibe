package org.molgenis.vibe.io.output.format.gene_prioritized;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.GeneDiseaseCombination;
import org.molgenis.vibe.io.output.ValuesSeparator;
import org.molgenis.vibe.io.output.format.PrioritizedOutputFormatWriter;
import org.molgenis.vibe.io.output.target.FileOutputWriter;
import org.molgenis.vibe.io.output.target.OutputWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Writer for writing {@link Gene}{@code} to a CSV file where a single line represent a {@link Gene}. A separate {@link List}
 * defines the order of {@link Gene}{@code s} in the output file.
 */
public class ResultsPerGeneSeparatedValuesOutputFormatWriter extends PrioritizedOutputFormatWriter<Gene> {
    /**
     * The data to be written.
     */
    private GeneDiseaseCollection collection;

    /**
     * Separates key-value pairs.
     */
    private ValuesSeparator primarySeparator;

    /**
     * Separates key-value pairs.
     */
    private ValuesSeparator keyValuePairSeparator;

    /**
     * Separates the key from the values in a key-value pair.
     */
    private ValuesSeparator keyValueSeparator;

    /**
     * Separates the values from a single key from a key-value pair.
     */
    private ValuesSeparator valuesSeparator;

    /**
     *
     * @param writer writer object to be used to write the data
     * @param collection the data to be written
     * @param prioritizer defines the order in which the {@link Gene}{@code s} are written
     * @param primarySeparator highest level values separator
     * @param keyValuePairSeparator separates different key-value pairs
     * @param keyValueSeparator separates a key and value
     * @param valuesSeparator separates the values from a key-value pair
     * @throws IllegalArgumentException if any separator is equal to another separator
     */
    public ResultsPerGeneSeparatedValuesOutputFormatWriter(OutputWriter writer, Prioritizer<Gene> prioritizer, GeneDiseaseCollection collection,
                                                           ValuesSeparator primarySeparator, ValuesSeparator keyValuePairSeparator,
                                                           ValuesSeparator keyValueSeparator, ValuesSeparator valuesSeparator) {
        super(writer, prioritizer);
        this.collection = requireNonNull(collection);
        this.primarySeparator = requireNonNull(primarySeparator);
        this.keyValuePairSeparator = requireNonNull(keyValuePairSeparator);
        this.keyValueSeparator = requireNonNull(keyValueSeparator);
        this.valuesSeparator = requireNonNull(valuesSeparator);

        Set<ValuesSeparator> separators = new HashSet<>();
        separators.add(primarySeparator);
        separators.add(keyValuePairSeparator);
        separators.add(keyValueSeparator);
        separators.add(valuesSeparator);

        // Checks whether all separators are unique.
        if(separators.size() < 4) {
            throw new IllegalArgumentException("the separators cannot be the same");
        }
    }

    public void generateOutput() throws IOException {
        // Writes header.
        getOutputWriter().write("gene (NCBI)" + primarySeparator + "gene symbol (HGNC)" + primarySeparator + "highest GDA score" + primarySeparator + "diseases (UMLS) with sources per disease");
        getOutputWriter().writeNewLine();

        // Goes through all ordered genes.
        for(Gene gene : getPrioritizer().getPriority()) {
            // Writes gene symbol to file.
            getOutputWriter().write(gene.getId() + primarySeparator + gene.getSymbol().getId() + primarySeparator);

            // The gene-disease combinations for this gene.
            List<GeneDiseaseCombination> geneDiseaseCombinations = collection.getByGeneOrderedByGdaScore(gene);

            // Goes through the available gda's and writes the information to the file.
            for(int i = 0; i < geneDiseaseCombinations.size(); i++) {
                // The current gene-disease combination.
                GeneDiseaseCombination gdc = geneDiseaseCombinations.get(i);


                if(i == 0) { // If first disease for this gene, write score as "highest GDA score".
                    getOutputWriter().write(Double.toString(gdc.getDisgenetScore()) + primarySeparator);
                } else { // If not first disease for this gene, adds separator.
                    getOutputWriter().write(keyValuePairSeparator.toString());
                }

                // Writes the disease id.
                getOutputWriter().write(gdc.getDisease().getId());

                // Writes gda score.
                getOutputWriter().write(" (" + gdc.getDisgenetScore() + ")");

                // If there is evidence, writes these as well.
                if(gdc.getAllEvidence().size() > 0) {
                    // Merges the evidence URIs with as separator the values separator.
                    String evidence = StringUtils.join(gdc.getAllEvidenceSimplifiedOrdered(), valuesSeparator.toString());
                    getOutputWriter().write(keyValueSeparator + evidence);
                }
            }

            getOutputWriter().writeNewLine();
        }
    }
}
