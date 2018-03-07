package org.molgenis.vibe.io.output;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCollection;
import org.molgenis.vibe.formats.GeneDiseaseCombination;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ResultsPerGeneCsvFileOutputWriter extends CsvFileOutputWriter {

    private GeneDiseaseCollection collection;
    private List<Gene> priority;

    public ResultsPerGeneCsvFileOutputWriter(Path path, GeneDiseaseCollection collection, List<Gene> priority) {
        super(path);
        this.collection = collection;
        this.priority = priority;
    }

    public void run() throws IOException {
        BufferedWriter writer = getWriter();

        writer.write("gene,diseases");
        writer.newLine();
        for(Gene gene : priority) {
            boolean firstDisease = true;
            writer.write(gene.getSymbol() + ",");

            Set<GeneDiseaseCombination> geneCombinations = collection.getByGene(gene);
            for(GeneDiseaseCombination gdc : geneCombinations) {
                if(!firstDisease) {
                    writer.write(";");
                }
                writer.write(gdc.getDisease().getName());

            }
            writer.newLine();
        }

        writer.flush();
        writer.close();
    }
}
