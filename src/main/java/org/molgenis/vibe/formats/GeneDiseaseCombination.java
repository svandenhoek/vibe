package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.*;

/**
 * A combination between a gene and a disease.
 */
public class GeneDiseaseCombination {
    /**
     * The gene.
     */
    private Gene gene;

    /**
     * The disease.
     */
    private Disease disease;

    /**
     * The score belonging to the gene-disease combination from the DisGeNET database.
     */
    private double disgenetScore;

    /**
     * A {@link Map} storing which {@link Source}{@code s} contains this combination and how often.
     */
    private Map<Source, Integer> sourcesCount = new HashMap<>();

    /**
     * A {@link Map} storing per {@link Source} the {@link URI}{@code s} to the evidence (if available).
     */
    private Map<Source, List<URI>> sourcesEvidence = new HashMap<>();

    public Gene getGene() {
        return gene;
    }

    public Disease getDisease() {
        return disease;
    }

    public double getDisgenetScore() {
        return disgenetScore;
    }

    /**
     * The number of occurrences for this gene-disease combination per {@link Source}.
     * @return an unmodifiable {@link Map}
     */
    public Map<Source, Integer> getSourcesCount() {
        return Collections.unmodifiableMap(sourcesCount);
    }

    /**
     * All {@link Source}{@code s} for this gene-disease combination that have evidence {@link URI}{@code s}.
     * @return an unmodifiable {@link Set}
     */
    public Set<Source> getSourcesWithEvidence() {
        return Collections.unmodifiableSet(sourcesEvidence.keySet());
    }

    /**
     * The evidence {@link URI}{@code s} for the defined {@link Source}
     * @return an unmodifiable {@link List}
     */
    public List<URI> getEvidenceForSource(Source source) {
        return Collections.unmodifiableList(sourcesEvidence.get(source));
    }

    public GeneDiseaseCombination(Gene gene, Disease disease, double disgenetScore) {
        this.gene = requireNonNull(gene);
        this.disease = requireNonNull(disease);
        this.disgenetScore = requireNonNull(disgenetScore);
    }

    /**
     * Adds a {@link Source} to this gene-disease combination with an evidence {@link URI}.
     * @param source
     * @param evidence
     */
    public void add(Source source, URI evidence) {
        add(source);

        List<URI> evidenceList = sourcesEvidence.get(source);
        if(evidenceList == null) {
            evidenceList = new ArrayList<>();
            sourcesEvidence.put(source, evidenceList);
        }
        evidenceList.add(evidence);
    }

    /**
     * Adds a {@link Source} to this gene-disease combination without an evidence {@link URI}.
     * @param source
     */
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
                ", disgenetScore=" + disgenetScore +
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
