package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.util.*;

public class GeneDiseaseCombination {
    private Gene gene;
    private Disease disease;
    private double score;
    private Map<Source, Integer> sourcesCount = new HashMap<>();
    private Map<Source, List<String>> sourcesEvidence = new HashMap<>();

    public Gene getGene() {
        return gene;
    }

    public Disease getDisease() {
        return disease;
    }

    public double getScore() {
        return score;
    }

    public Map<Source, Integer> getSourcesCount() {
        return Collections.unmodifiableMap(sourcesCount);
    }

    public Map<Source, List<String>> getSourcesEvidence() {
        return Collections.unmodifiableMap(sourcesEvidence);
    }

    public GeneDiseaseCombination(Gene gene, Disease disease, double score) {
        this.gene = requireNonNull(gene);
        this.disease = requireNonNull(disease);
        this.score = requireNonNull(score);
    }

    public void add(Source source, String evidence) {
        add(source);

        List<String> evidenceList = sourcesEvidence.get(source);
        if(evidenceList == null) {
            evidenceList = new ArrayList<>();
            sourcesEvidence.put(source, evidenceList);
        }
        evidenceList.add(evidence);
    }

    public void add(Source source) {
        Integer count = sourcesCount.get(source);
        if(count == null) {
            sourcesCount.put(source, 1);
        } else {
            sourcesCount.put(source, count + 1);
        }
    }

    @Override
    public String toString() {
        return "GeneDiseaseCombination{" +
                "gene=" + gene +
                ", disease=" + disease +
                ", score=" + score +
                ", sourcesCount=" + sourcesCount +
                ", sourcesEvidence=" + sourcesEvidence +
                '}';
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
