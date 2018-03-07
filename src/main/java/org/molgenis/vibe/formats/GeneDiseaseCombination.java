package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.*;

/**
 * A combination between a gene and a disease.
 */
public class GeneDiseaseCombination extends BiologicalEntityCombination<Gene, Disease> {
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
        return getT1();
    }

    public Disease getDisease() {
        return getT2();
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

    public Set<Source> getSourcesWithCount() {
        return Collections.unmodifiableSet(sourcesCount.keySet());
    }

    /**
     * The count for the defined {@link Source}
     * @param source the {@link Source} to retrieve the count for
     * @return an {@code int} containing the frequency of this source found
     */
    public int getCountForSource(Source source) {
        Integer count = sourcesCount.get(source);
        if(count == null) {
            count = 0;
        }
        return count;
    }

    /**
     * All {@link Source}{@code s} for this gene-disease combination that have evidence {@link URI}{@code s}.
     * @return an unmodifiable {@link Set} containing {@link Source}{@code s}
     */
    public Set<Source> getSourcesWithEvidence() {
        return Collections.unmodifiableSet(sourcesEvidence.keySet());
    }

    /**
     * The evidence {@link URI}{@code s} for the defined {@link Source}
     * @param source
     * @return an unmodifiable {@link List} containing evidence {@link URI}{@code s}, or {@code null} if {@link Source} does not have any evidence
     */
    public List<URI> getEvidenceForSource(Source source) {
        List evidence = sourcesEvidence.get(source);
        if(evidence != null) {
            evidence = Collections.unmodifiableList(evidence);
        }
        return evidence;
    }

    public Set<URI> getAllEvidence() {
        Set<URI> allSources = new HashSet<>();
        for(List<URI> evidenceForSingleSource : sourcesEvidence.values()) {
            allSources.addAll(evidenceForSingleSource);
        }

        return allSources;
    }

    /**
     * Simple constructor allowing for easy comparison of collections.
     * @param gene
     * @param disease
     */
    public GeneDiseaseCombination(Gene gene, Disease disease) {
        super(gene, disease);
    }

    public GeneDiseaseCombination(Gene gene, Disease disease, double disgenetScore) {
        super(gene, disease);
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
                "disgenetScore=" + disgenetScore +
                ", sourcesCount=" + sourcesCount +
                ", sourcesEvidence=" + sourcesEvidence +
                "} " + super.toString();
    }
}
