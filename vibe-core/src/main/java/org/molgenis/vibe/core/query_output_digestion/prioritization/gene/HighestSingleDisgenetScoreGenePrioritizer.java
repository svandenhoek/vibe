package org.molgenis.vibe.core.query_output_digestion.prioritization.gene;

import org.molgenis.vibe.core.formats.Gene;
import org.molgenis.vibe.core.formats.GeneDiseaseCollection;
import org.molgenis.vibe.core.formats.GeneDiseaseCombination;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Generates a priority order for a given collection ({@link List} or {@link Set}) of {@link Gene}{@code s}.
 * The priority order is based on the highest {@link GeneDiseaseCombination#getDisgenetScore()} per {@link Gene}.
 */
public class HighestSingleDisgenetScoreGenePrioritizer extends GenePrioritizer {
    /**
     * The data to be used for creating a priority order.
     */
    private GeneDiseaseCollection geneDiseaseCollection;

    public HighestSingleDisgenetScoreGenePrioritizer(GeneDiseaseCollection geneDiseaseCollection) {
        super(geneDiseaseCollection.getGenes());
        this.geneDiseaseCollection = requireNonNull(geneDiseaseCollection);
    }

    @Override
    public void run() {
        List<Gene> genes = getPriority();
        Map<Gene, Double> highestGeneScores = new HashMap<>();

        // Goes through all genes.
        for(Gene gene : genes) {
            double scoreForGene = 0;
            Set<GeneDiseaseCombination> combinationsForGene = geneDiseaseCollection.getByGene(gene);
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

        // Updates the priority order.
        setPriority(genes);
    }
}
