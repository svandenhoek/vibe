package org.molgenis.vibe.formats;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.*;

/**
 * A combination of a {@link Gene} and a {@link Disease}.
 */
public class GeneDiseaseCombination extends BiologicalEntityCombination<Gene, Disease> {
    /**
     * The score belonging to the gene-disease combination from the DisGeNET database.
     */
    private Double disgenetScore;

    /**
     * A {@link Map} storing which {@link Source}{@code s} contains this combination and how often.
     */
    private Map<Source, Integer> sourcesCount = new HashMap<>();

    /**
     * A {@link Map} storing per {@link Source} the {@link URI}{@code s} to the evidence (if available).
     */
    private Map<Source, Set<PubmedEvidence>> pubmedEvidence = new HashMap<>();

    /**
     * @return the {@link Gene}
     * @see #getT1()
     */
    public Gene getGene() {
        return getT1();
    }

    /**
     * @return the {@link Disease}
     * @see #getT2()
     */
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

    /**
     * @deprecated unnecessary method, can be achieved through {@link #getSourcesCount()}{@code .keySet()}.
     */
    @Deprecated
    public Set<Source> getSourcesWithCount() {
        return Collections.unmodifiableSet(sourcesCount.keySet());
    }

    /**
     * The count for the defined {@link Source}
     * @param source the {@link Source} to retrieve the count for
     * @return an {@code int} containing the frequency of this source found (if {@link Source} is not present returns a 0)
     */
    public int getCountForSource(Source source) {
        Integer count = sourcesCount.get(source);
        if(count == null) {
            count = 0;
        }
        return count;
    }

    /**
     * All {@link Source}{@code s} for this gene-disease combination that have {@link PubmedEvidence}.
     * @return an unmodifiable {@link Set} containing {@link Source}{@code s}
     */
    public Set<Source> getSourcesWithPubmedEvidence() {
        return Collections.unmodifiableSet(pubmedEvidence.keySet());
    }

    /**
     * All {@link Source}{@code s} for this gene-disease combination that have evidence {@link URI}{@code s}.
     * Currently only a wrapper for {@link #getSourcesWithPubmedEvidence()}.
     * @return an unmodifiable {@link Set} containing {@link Source}{@code s}
     * @deprecated use {@link #getSourcesWithPubmedEvidence()} instead
     */
    @Deprecated
    public Set<Source> getSourcesWithEvidence() {
        return getSourcesWithPubmedEvidence();
    }

    /**
     * The {@link PubmedEvidence} for the defined {@link Source}.
     * @param source
     * @return an unmodifiable {@link Set} containing {@link PubmedEvidence},
     * or {@code null} if {@link Source} does not have any evidence
     */
    public Set<PubmedEvidence> getPubmedEvidenceForSource(Source source) {
        Set<PubmedEvidence> evidence = pubmedEvidence.get(source);
        if(evidence != null) {
            evidence = Collections.unmodifiableSet(evidence);
        }

        return evidence;
    }

    /**
     * The {@link PubmedEvidence} of all {@link Source}{@code s} combined.
     * @return a {@link Set} containing all the {@link PubmedEvidence}
     */
    public Set<PubmedEvidence> getAllPubmedEvidence() {
        Set<PubmedEvidence> evidence = new HashSet<>();
        pubmedEvidence.values().forEach(pubmedEvidenceSubset -> evidence.addAll(pubmedEvidenceSubset));
        return evidence;
    }

    /**
     * The evidence {@link URI}{@code s} for the defined {@link Source}
     * @param source
     * @return a sorted {@link List} containing evidence {@link URI}{@code s}, or {@code null} if {@link Source} does not have any evidence
     * @deprecated use {@link #getPubmedEvidenceForSource(Source)} instead
     */
    @Deprecated
    public List<URI> getEvidenceForSource(Source source) {
        Set<PubmedEvidence> evidence = getPubmedEvidenceForSource(source);

        if(evidence == null) {
            return null;
        }

        List<URI> outputList = new ArrayList<>();
        evidence.forEach((item) -> outputList.add(item.getUri()));

        Collections.sort(outputList);

        return outputList;
    }

    /**
     * The evidence of all {@link Source}{@code s} combined.
     * @return a {@link Set} containing all the evidence {@link URI}{@code s}
     * @deprecated use {@link #getAllPubmedEvidence()} instead
     */
    @Deprecated
    public Set<URI> getAllEvidence() {
        Set<URI> outputSet = new HashSet<>();
        getAllPubmedEvidence().forEach((item) -> outputSet.add(item.getUri()));
        return outputSet;
    }

    /**
     * Wrapper for {@link #getAllEvidence()} that returns an ordered list.
     * @return a {@link List} containing all the evidence {@link URI}{@code s}
     * @deprecated use {@link #getAllPubmedEvidence()} instead (returns {@link Set} instead of {@link List})
     */
    @Deprecated
    public List<URI> getAllEvidenceOrdered() {
        List<PubmedEvidence> pubmedEvidenceList = new ArrayList(getAllPubmedEvidence());
        Collections.sort(pubmedEvidenceList);

        List<URI> outputList = new ArrayList<>();
        pubmedEvidenceList.forEach((item) -> outputList.add(item.getUri()));

        return outputList;
    }

    /**
     * Wrapper for {@link #getAllEvidenceOrdered()} that converts the {@link URI}{@code s} to {@link String}{@code s}.
     * @return a {@link List} containing all the evidence {@link URI}{@code s} as {@link String}{@code s}
     * @deprecated use {@link #getAllPubmedEvidence()} instead (returns {@link Set} instead of {@link List})
     */
    @Deprecated
    public List<String> getAllEvidenceOrderedStrings() {
        List<String> outputList = new ArrayList<>();
        getAllEvidenceOrdered().forEach((item) -> outputList.add(item.toString()));
        return outputList;
    }

    /**
     * Wrapper for {@code #getAllEvidence} where {@link URI}{@code s} starting with {@code http://identifiers.org/pubmed/}
     * are reduced to only the number behind it. If a source starts with a different {@link URI}, the full {@link URI} is
     * still returned (though after being converted to a {@link String}).
     * @return a {@link Set} containing numbers for PubMed IDs (starting with {@code http://identifiers.org/pubmed/} as
     * {@link URI}) and for other sources the full {@link URI} as a {@link String}
     * @deprecated use {@link #getAllPubmedEvidence()} instead (returns {@link Set} instead of {@link List})
     */
    @Deprecated
    public Set<String> getAllEvidenceSimplified() {
        Set<String> simplifiedSources = new HashSet<>();
        simplifyEvidence(new ArrayList<>(this.getAllEvidence()), simplifiedSources);

        return simplifiedSources;
    }

    /**
     * The same as {@link #getAllEvidenceSimplified()}, only returns an alphabetically ordered {@link List} instead of a
     * {@link Set}.
     * @return a {@link List} containing numbers for PubMed IDs (starting with {@code http://identifiers.org/pubmed/} as
     * {@link URI}) and for other sources the full {@link URI} as a {@link String}
     * @see #getAllEvidenceSimplified()
     * @deprecated use {@link #getAllPubmedEvidence()} instead (returns {@link Set} instead of {@link List})
     */
    @Deprecated
    public List<String> getAllEvidenceSimplifiedOrdered() {
        List<String> simplifiedSources = new ArrayList<>();
        simplifyEvidence(this.getAllEvidenceOrdered(), simplifiedSources);

        return simplifiedSources;
    }

    /**
     * Goes through all source {@link URI}{@code s} given and stores them (if possible simplifified) in the given {@link Collection}.
     * @param simplifiedSources where the (simplified) sources should be stored in as {@link String}
     * @param allSourceUris the {@link Set} that should be simplified
     * @deprecated private method not needed anymore when {@link Deprecated} public methods are removed
     */
    @Deprecated
    private void simplifyEvidence(List<URI> allSourceUris, Collection<String> simplifiedSources) {
        for(URI source : allSourceUris) {
            String sourceString = source.toString();
            simplifiedSources.add((sourceString.startsWith("http://identifiers.org/pubmed/") ? sourceString.substring(30) : sourceString));
        }
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
     * @deprecated use {@link #add(Source, PubmedEvidence)} instead, URIs added through this method will have release year "-1".
     */
    @Deprecated
    public void add(Source source, URI evidence) {
        add(source, new PubmedEvidence(evidence, -1));
    }

    public void add(Source source, PubmedEvidence evidence) {
        // Increments counter for source.
        add(source);

        // Stores PubMed evidence.
        Set<PubmedEvidence> evidenceList = pubmedEvidence.get(source);
        if(evidenceList == null) {
            evidenceList = new HashSet<>();
            pubmedEvidence.put(source, evidenceList);
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
                ", pubmedEvidence=" + pubmedEvidence +
                ' ' + super.toString() +
                '}';
    }

    @Override
    public boolean allFieldsEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GeneDiseaseCombination that = (GeneDiseaseCombination) o;

        if (!(
                super.allFieldsEquals(that) &&
                Objects.equals(disgenetScore, that.disgenetScore) &&
                Objects.equals(sourcesCount, that.sourcesCount) &&
                Objects.equals(pubmedEvidence, that.pubmedEvidence)
        )) {
            return false;
        }

        // Checks allFieldsEquals() for all items in pubmedEvidence.
        for (Source source : pubmedEvidence.keySet()) {
            List<PubmedEvidence> thisPubmedEvidences = new ArrayList<>(pubmedEvidence.get(source));
            Collections.sort(thisPubmedEvidences);

            List<PubmedEvidence> thatPubmedEvidences = new ArrayList<>(that.pubmedEvidence.get(source));
            Collections.sort(thatPubmedEvidences);

            for (int i = 0; i < thisPubmedEvidences.size(); i++) {
                if (!thisPubmedEvidences.get(i).allFieldsEquals(thatPubmedEvidences.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }
}
