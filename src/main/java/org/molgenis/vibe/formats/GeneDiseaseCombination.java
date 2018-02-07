package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GeneDiseaseCombination {
    private Gene gene;
    private Disease disease;
    private double score;
    private HashMap<Source, Integer> sourcesCount;
    private HashMap<Source, ArrayList<String>> sourcesEvidence;

    public GeneDiseaseCombination(Gene gene, Disease disease, double score) {
        this.gene = requireNonNull(gene);
        this.disease = requireNonNull(disease);
        this.score = requireNonNull(score);
    }

    public void add(Source source, String evidence) {
        //TODO: continue working on function
    }

    public void add(Source source) {
        //TODO: continue working on function
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneDiseaseCombination that = (GeneDiseaseCombination) o;
        return Objects.equals(gene, that.gene) &&
                Objects.equals(disease, that.disease);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gene, disease);
    }
}
