package org.molgenis.vibe.core.formats.serialization;

import com.google.gson.*;
import org.molgenis.vibe.core.formats.*;

import java.lang.reflect.Type;
import java.util.*;

public class GeneDiseaseCollectionSerializer implements JsonSerializer<GeneDiseaseCollection> {
    @Override
    public JsonElement serialize(GeneDiseaseCollection src, Type typeOfSrc, JsonSerializationContext context) {
        Set<Source> sources = new HashSet<>();

        // Object to store all data into.
        JsonObject collectionObject = new JsonObject();
        collectionObject.add("combinations", generateGeneDiseaseCombinationsArray(src, sources));
        collectionObject.add(Gene.ID_PREFIX, generateGenesData(src));
        collectionObject.add(Disease.ID_PREFIX, generateDiseaseData(src));
        collectionObject.add("sources", generateSourcesData(sources));
        return collectionObject;
    }

    /**
     * Generates the {@link JsonArray} for the gene-disease combinations.
     * @param src the {@link GeneDiseaseCollection} that should be processed
     * @param allSources a {@link Set} to store all unique {@link Source} instances in found in the gene-disease
     *                   combinations
     * @return the {@link GeneDiseaseCombination}{@code s} present in the {@link GeneDiseaseCollection} as {@link JsonArray}
     */
    private JsonArray generateGeneDiseaseCombinationsArray(GeneDiseaseCollection src, Set allSources) {
        // Array to store all gene-disease combinations in.
        JsonArray combinationsArray = new JsonArray();

        // Goes through all gene-disease combinations.
        for(GeneDiseaseCombination gdc : src.getGeneDiseaseCombinationsOrdered()) {
            // Creates a new gene-disease combinations.
            JsonObject combinationObject = new JsonObject();

            // Adds gene.
            combinationObject.addProperty(Gene.ID_PREFIX, gdc.getGene().getId());

            // Adds disease.
            combinationObject.addProperty(Disease.ID_PREFIX, gdc.getDisease().getId());

            // Adds score.
            combinationObject.addProperty("score", gdc.getDisgenetScore());

            // Adds sources.
            JsonArray evidenceArray = new JsonArray();

            // sources: generate sorted list of sources.
            List<Source> orderedSources = new ArrayList<>();
            orderedSources.addAll(gdc.getSourcesCount().keySet());
            Collections.sort(orderedSources);

            // sources: Goes through all ordered sources.
            for(Source source : orderedSources) {
                // sources: Add source Object itself to set containing all used sources.
                allSources.add(source);

                // sources: Digest source basic data.
                JsonObject evidenceItemObject = new JsonObject();
                evidenceItemObject.addProperty("name", source.getName());
                evidenceItemObject.addProperty("count", gdc.getCountForSource(source));

                // sources: Adds pubmed evidence (empty array if no pubmed evidence).
                JsonArray pubmedEvidenceArray = new JsonArray();

                List<PubmedEvidence> evidenceList = gdc.getPubmedEvidenceForSourceSortedByReleaseDate(source);
                if(evidenceList != null) {
                    for (PubmedEvidence pubmedEvidence : evidenceList) {
                        JsonObject pubmedEvidenceObject = new JsonObject();
                        pubmedEvidenceObject.addProperty("uri", pubmedEvidence.getUri().toString());
                        pubmedEvidenceObject.addProperty("year", pubmedEvidence.getReleaseYear());
                        pubmedEvidenceArray.add(pubmedEvidenceObject);
                    }
                }
                evidenceItemObject.add("pubmed", pubmedEvidenceArray);

                // sources: Adds evidence from single source to array.
                evidenceArray.add(evidenceItemObject);
            }

            // Evidence: Adds array of evidence to combination.
            combinationObject.add("sources", evidenceArray);

            // Adds a single gene-disease combination to the array.
            combinationsArray.add(combinationObject);
        }

        return combinationsArray;
    }

    /**
     * Generates the {@link JsonObject} for the {@link Gene}{@code s} present in the {@link GeneDiseaseCollection}.
     * @param src the {@link GeneDiseaseCollection} that should be processed
     * @return the {@link Gene}{@code s} present in the {@link GeneDiseaseCollection} as {@link JsonObject}
     */
    private JsonObject generateGenesData(GeneDiseaseCollection src) {
        JsonObject genesObject = new JsonObject();

        // Processes items into JsonObject.
        for(Gene gene : src.getGenes()) {
            JsonObject singleGeneObject = new JsonObject();
            singleGeneObject.addProperty(GeneSymbol.ID_PREFIX, gene.getSymbol().getId());
            genesObject.add(gene.getId(), singleGeneObject);
        }

        return genesObject;
    }

    /**
     * Generates the {@link JsonObject} for the {@link Disease}{@code s} present in the {@link GeneDiseaseCollection}.
     * @param src the {@link GeneDiseaseCollection} that should be processed
     * @return the {@link Disease}{@code s} present in the {@link GeneDiseaseCollection} as {@link JsonObject}
     */
    private JsonObject generateDiseaseData(GeneDiseaseCollection src) {
        JsonObject diseasesObject = new JsonObject();

        // Processes items into JsonObject.
        for(Disease disease : src.getDiseases()) {
            JsonObject singleDiseaseObject = new JsonObject();
            singleDiseaseObject.addProperty("name", disease.getName());
            diseasesObject.add(disease.getId(), singleDiseaseObject);
        }

        return diseasesObject;
    }

    /**
     * Generates the {@link JsonObject} for the {@link Source}{@code s} present in the {@link GeneDiseaseCollection}.
     * @param sourcesSet the {@link Set} of all unique {@link Source}{@code s} (after it has been filled using
     * {@link #generateGeneDiseaseCombinationsArray(GeneDiseaseCollection, Set)})
     * @return the {@link Source}{@code s} present in the {@link GeneDiseaseCollection} as {@link JsonObject}
     */
    private JsonObject generateSourcesData(Set<Source> sourcesSet) {
        JsonObject sourcesObject = new JsonObject();

        // Processes items into JsonObject.
        for(Source source : sourcesSet) {
            JsonObject singleSourceObject = new JsonObject();
            singleSourceObject.addProperty("level", source.getLevel().getReadableString());
            sourcesObject.add(source.getName(), singleSourceObject);
        }

        return sourcesObject;
    }
}
