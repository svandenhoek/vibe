package org.molgenis.vibe.io.output;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.GeneDiseaseCombination;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Writer for writing {@link Gene}{@code} to a CSV file where a single line represent a {@link Gene}. A separate {@link List}
 * defines the order of {@link Gene}{@code s} in the output file.
 */
public class ResultsPerGeneCsvFileOutputWriter extends CsvFileOutputWriter {

    /**
     * The data to be written.
     */
    private GeneDiseaseCollection collection;

    /**
     * The order of the {@link Gene}{@code s}.
     */
    private List<Gene> priority;

    /**
     *
     * @param path path of file for data to be written to
     * @param collection the data to be written
     * @param priority defines the order in which the {@link Gene}{@code s} are written to the file
     */
    public ResultsPerGeneCsvFileOutputWriter(Path path, GeneDiseaseCollection collection, List<Gene> priority) {
        super(path);
        this.collection = collection;
        this.priority = priority;
    }

    public void run() throws IOException {
        BufferedWriter writer = getWriter();

        writer.write("gene,diseases,highest score");
        writer.newLine();
        for(Gene gene : priority) {
            boolean firstDisease = true;
            writer.write(gene.getSymbol() + SEPARATOR + QUOTE_MARK);

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
                    writer.write(";" + gdc.getDisease().getName());
                }
            }
            writer.write(QUOTE_MARK + SEPARATOR + Double.toString(highestScore));
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }
}
