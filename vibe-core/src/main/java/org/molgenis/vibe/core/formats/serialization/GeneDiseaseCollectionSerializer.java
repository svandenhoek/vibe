package org.molgenis.vibe.core.formats.serialization;

import com.google.gson.*;
import org.molgenis.vibe.core.formats.*;

import java.lang.reflect.Type;
import java.util.*;

public class GeneDiseaseCollectionSerializer extends GeneDiseaseCollectionJson implements JsonSerializer<GeneDiseaseCollection> {
    @Override
    public JsonElement serialize(GeneDiseaseCollection src, Type typeOfSrc, JsonSerializationContext context) {
        Set<Source> sources = new HashSet<>();

        // Object to store all data into.
        JsonObject collectionObject = new JsonObject();
        collectionObject.add(COMBINATIONS_KEY, generateGeneDiseaseCombinationsArray(src, sources));
        collectionObject.add(GENES_KEY, generateGenesData(src));
        collectionObject.add(DISEASES_KEY, generateDiseaseData(src));
        collectionObject.add(SOURCES_KEY, generateSourcesData(sources));
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
            combinationObject.addProperty(COMBINATION_GENE_KEY, gdc.getGene().getId());

            // Adds disease.
            combinationObject.addProperty(COMBINATION_DISEASE_KEY, gdc.getDisease().getId());

            // Adds score.
            combinationObject.addProperty(COMBINATION_DISGENET_SCORE_KEY, gdc.getDisgenetScore());

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
                evidenceItemObject.addProperty(COMBINATION_SOURCE_NAME_KEY, source.getName());
                evidenceItemObject.addProperty(COMBINATION_SOURCE_COUNT_KEY, gdc.getCountForSource(source));

                // sources: Adds pubmed evidence (empty array if no pubmed evidence).
                JsonArray pubmedEvidenceArray = new JsonArray();

                List<PubmedEvidence> evidenceList = gdc.getPubmedEvidenceForSourceSortedByReleaseDate(source);
                if(evidenceList != null) {
                    for (PubmedEvidence pubmedEvidence : evidenceList) {
                        JsonObject pubmedEvidenceObject = new JsonObject();
                        pubmedEvidenceObject.addProperty(COMBINATION_SOURCE_PUBMED_URI_KEY, pubmedEvidence.getUri().toString());
                        pubmedEvidenceObject.addProperty(COMBINATION_SOURCE_PUBMED_YEAR_KEY, pubmedEvidence.getReleaseYear());
                        pubmedEvidenceArray.add(pubmedEvidenceObject);
                    }
                }
                evidenceItemObject.add(COMBINATION_SOURCE_PUBMEDS_KEY, pubmedEvidenceArray);

                // sources: Adds evidence from single source to array.
                evidenceArray.add(evidenceItemObject);
            }

            // Evidence: Adds array of evidence to combination.
            combinationObject.add(COMBINATION_SOURCES_KEY, evidenceArray);

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
            singleGeneObject.addProperty(GENE_SYMBOL_KEY, gene.getSymbol().getId());
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
            singleDiseaseObject.addProperty(DISEASE_NAME_KEY, disease.getName());
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
            singleSourceObject.addProperty(SOURCE_LEVEL_KEY, source.getLevel().getReadableString());
            singleSourceObject.addProperty(SOURCE_FULL_NAME_KEY, source.getFullName());
            singleSourceObject.addProperty(SOURCE_URI_KEY, source.getUri().toString());
            sourcesObject.add(source.getName(), singleSourceObject);
        }

        return sourcesObject;
    }
}
