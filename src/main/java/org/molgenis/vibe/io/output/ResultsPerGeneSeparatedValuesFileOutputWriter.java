package org.molgenis.vibe.io.output;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.GeneDiseaseCombination;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
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
     * The second level separator to be used to separate values within a single field separated by the primary separator.
     */
    private ValuesSeparator secondarySeparator;

    /**
     *
     * @param path path of file for data to be written to
     * @param collection the data to be written
     * @param priority defines the order in which the {@link Gene}{@code s} are written to the file
     * @param primarySeparator highest level values separator
     * @param secondarySeparator second level values separator (separation of values within a single value separated by
     *                          the {@code primarySeparator})
     */
    public ResultsPerGeneSeparatedValuesFileOutputWriter(Path path, GeneDiseaseCollection collection, List<Gene> priority,
                                                         ValuesSeparator primarySeparator, ValuesSeparator secondarySeparator) {
        super(path, primarySeparator);
        this.collection = requireNonNull(collection);
        this.priority = requireNonNull(priority);
        this.secondarySeparator = requireNonNull(secondarySeparator);
    }

    public void run() throws IOException {
        BufferedWriter writer = getWriter();

        writer.write("gene" + getSeparator() + "diseases" + getSeparator() + "highest score");
        writer.newLine();
        for(Gene gene : priority) {
            boolean firstDisease = true;
            writer.write(gene.getSymbol() + getSeparator() + QUOTE_MARK);

            double highestScore = 0;

            Set<GeneDiseaseCombination> geneCombinations = collection.getByGene(gene);
            for(GeneDiseaseCombination gdc : geneCombinations) {
                if(gdc.getDisgenetScore() > highestScore) {
                    highestScore = gdc.getDisgenetScore();
                }
                if(firstDisease) {
                    writer.write(gdc.getDisease().getName());
                    firstDisease=false;
                } else {
                    writer.write(secondarySeparator + gdc.getDisease().getName());
                }
            }
            writer.write(QUOTE_MARK + getSeparator() + Double.toString(highestScore));
            writer.newLine();
        }

        closeWriter();
    }
}
