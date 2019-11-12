package org.molgenis.vibe.io.output.file.gene_prioritized;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.GeneDiseaseCombination;
import org.molgenis.vibe.io.output.ValuesSeparator;
import org.molgenis.vibe.io.output.file.SeparatedValuesFileOutputWriter;

import java.io.BufferedWriter;
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
public class ResultsPerGeneSeparatedValuesFileOutputWriter extends SeparatedValuesFileOutputWriter {
    /**
     * The data to be written.
     */
    private GeneDiseaseCollection collection;

    /**
     * The order of the {@link Gene}{@code s}.
     */
    private List<Gene> priority;

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
     * @param path path of file for data to be written to
     * @param collection the data to be written
     * @param priority defines the order in which the {@link Gene}{@code s} are written to the file
     * @param primarySeparator highest level values separator
     * @param keyValuePairSeparator separates different key-value pairs
     * @param keyValueSeparator separates a key and value
     * @param valuesSeparator separates the values from a key-value pair
     * @throws IllegalArgumentException if any separator is equal to another separator
     */
    public ResultsPerGeneSeparatedValuesFileOutputWriter(Path path, GeneDiseaseCollection collection, List<Gene> priority,
                                                         ValuesSeparator primarySeparator, ValuesSeparator keyValuePairSeparator,
                                                         ValuesSeparator keyValueSeparator, ValuesSeparator valuesSeparator) {
        super(path, primarySeparator);
        this.collection = requireNonNull(collection);
        this.priority = requireNonNull(priority);
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

    public void run() throws IOException {
        BufferedWriter writer = getWriter();

        // Writes header.
        writer.write("gene (NCBI)" + getSeparator() + "diseases (UMLS) with sources per disease" + getSeparator() + "highest GDA score");
        writer.newLine();

        // Goes through all ordered genes.
        for(Gene gene : priority) {
            // Writes gene symbol to file.
            writer.write(gene.getId() + getSeparator());

            // The gene-disease combinations for this gene.
            List<GeneDiseaseCombination> geneDiseaseCombinations = collection.getByGeneOrderedByGdaScore(gene);

            // Goes through the available gda's and writes the information to the file.
            for(int i = 0; i < geneDiseaseCombinations.size(); i++) {
                // The current gene-disease combination.
                GeneDiseaseCombination gdc = geneDiseaseCombinations.get(i);


                if(i == 0) { // If first disease for this gene, write score as "highest GDA score".
                    writer.write(Double.toString(gdc.getDisgenetScore()) + getSeparator());
                } else { // If not first disease for this gene, adds separator.
                    writer.write(keyValuePairSeparator.toString());
                }

                // Writes the disease id.
                writer.write(gdc.getDisease().getId());

                // Writes gda score.
                writer.write(" (" + gdc.getDisgenetScore() + ")");

                // If there is evidence, writes these as well.
                if(gdc.getAllEvidence().size() > 0) {
                    // Merges the evidence URIs with as separator the values separator.
                    String evidence = StringUtils.join(gdc.getAllEvidenceSimplified(), valuesSeparator.toString());
                    writer.write(keyValueSeparator + evidence);
                }
            }

            writer.newLine();
        }

        closeWriter();
    }
}
