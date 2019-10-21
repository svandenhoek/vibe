package org.molgenis.vibe.io.output;

import org.apache.commons.lang3.StringUtils;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.GeneDiseaseCombination;

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
        writer.write("gene" + getSeparator() + "diseases" + getSeparator() + "highest GDA score" +
                getSeparator() + "DSI" + getSeparator() + "DPI");
        writer.newLine();

        // Goes through all ordered genes.
        for(Gene gene : priority) {
            // Writes gene symbol to file.
            writer.write(gene.getSymbol() + getSeparator());

            // Stores the highest DisGeNET score for this gene.
            double highestScore = 0;

            // Used for key-value pair separator.
            boolean firstDisease = true;

            // Processes/writes gene data.
            for(GeneDiseaseCombination gdc : collection.getByGene(gene)) {
                // Selects highest DisGeNET score available for the gene.
                if(gdc.getDisgenetScore() > highestScore) {
                    highestScore = gdc.getDisgenetScore();
                }

                // Checks whether this is the first disease. If not, adds a key-value pair separator before the next
                // disease data is written.
                if(!firstDisease) {
                    writer.write(keyValuePairSeparator.toString());
                } else {
                    firstDisease = false;
                }

                // Writes the disease name surrouned by quotes.
                writer.write(gdc.getDisease().getName());

//                // If there is evidence, writes these as well.
//                if(gdc.getAllEvidence().size() > 0) {
//                    // Merges the evidence URIs with as separator the values separator.
//                    String evidence = StringUtils.join(gdc.getAllEvidence(), valuesSeparator.toString());
//                    writer.write(keyValueSeparator + evidence);
//                }
            }

            writer.write(getSeparator() + Double.toString(highestScore) + getSeparator() +
                    gene.getDiseaseSpecificityIndex() + getSeparator() + gene.getDiseasePleiotropyIndex());
            writer.newLine();
        }

        closeWriter();
    }
}
