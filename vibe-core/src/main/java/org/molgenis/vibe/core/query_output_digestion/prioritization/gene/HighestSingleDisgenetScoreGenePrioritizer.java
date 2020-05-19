package org.molgenis.vibe.core.query_output_digestion.prioritization.gene;

import org.molgenis.vibe.core.formats.Gene;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.GeneDiseaseCombination;

import java.util.*;

/**
 * A {@link Gene} priority for a {@link GeneDiseaseCollection} that is based on the highest
 * {@link GeneDiseaseCombination#getDisgenetScore()} per {@link Gene}.
 */
public class HighestSingleDisgenetScoreGenePrioritizer implements GenePrioritizer {
    @Override
    public List<Gene> sort(GeneDiseaseCollection collection) {
        // The genes to sort.
        List<Gene> genes = new ArrayList<>(collection.getGenes());

        // Stores highest score per gene.
        Map<Gene, Double> highestGeneScores = new HashMap<>();

        // Goes through all genes.
        for(Gene gene : genes) {
            double scoreForGene = 0;
            Set<GeneDiseaseCombination> combinationsForGene = collection.getByGene(gene);
            // Goes through all disease combinations for a single gene.
            for(GeneDiseaseCombination combination : combinationsForGene) {
                // Stores highest DisGeNET gene-disease score for that gene.
                if(combination.getDisgenetScore() > scoreForGene) {
                    scoreForGene = combination.getDisgenetScore();
                }
            }

            // Stores the gene with its highest score in a HashMap.
            highestGeneScores.put(gene, scoreForGene);
        }

        // Sorts the gene list based on the highest gene-disease score per gene.
        Collections.sort(genes, Comparator.comparingDouble(highestGeneScores::get).reversed());

        return genes;
    }
}
