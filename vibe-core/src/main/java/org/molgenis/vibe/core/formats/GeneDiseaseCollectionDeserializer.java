package org.molgenis.vibe.core.formats;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;

public class GeneDiseaseCollectionDeserializer extends GeneDiseaseCollectionJsonSerialization implements JsonDeserializer<GeneDiseaseCollection> {
    @Override
    public GeneDiseaseCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject collectionObject = json.getAsJsonObject();
        return generateGeneDiseaseCollection(collectionObject.getAsJsonArray(COMBINATIONS_KEY),
                retrieveGenes(collectionObject.getAsJsonObject(GENES_KEY)),
                retrieveDiseases(collectionObject.getAsJsonObject(DISEASES_KEY)),
                retrieveSources(collectionObject.getAsJsonObject(SOURCES_KEY))
        );
    }

    private GeneDiseaseCollection generateGeneDiseaseCollection(JsonArray combinationsArray,
                                                                Map<String, Gene> genesMap,
                                                                Map<String, Disease> diseasesMap,
                                                                Map<String, Source> sourcesMap) {

        GeneDiseaseCollection collection = new GeneDiseaseCollection();

        System.out.println(sourcesMap);

        // Iterates through all combinations.
        Iterator<JsonElement> combinationsIterator = combinationsArray.iterator();
        while(combinationsIterator.hasNext()) {
            // Create basic GeneDiseaseCombination.
            JsonObject combinationObject = combinationsIterator.next().getAsJsonObject();
            Gene gene = genesMap.get(combinationObject.getAsJsonPrimitive(COMBINATION_GENE_KEY).getAsString());
            Disease disease = diseasesMap.get(combinationObject.getAsJsonPrimitive(COMBINATION_DISEASE_KEY).getAsString());
            Double disgenetScore = combinationObject.getAsJsonPrimitive(COMBINATION_DISGENET_SCORE_KEY).getAsDouble();
            GeneDiseaseCombination geneDiseaseCombination = new GeneDiseaseCombination(gene, disease, disgenetScore);

            // Iterates through all sources.
            Iterator<JsonElement> combinationSourcesIterator = combinationObject.getAsJsonArray(COMBINATION_SOURCES_KEY).iterator();
            while ( combinationSourcesIterator.hasNext() ) {
                // The source.
                JsonObject sourceObject = combinationSourcesIterator.next().getAsJsonObject();
                Source source = sourcesMap.get(sourceObject.getAsJsonPrimitive(COMBINATION_SOURCE_NAME_KEY).getAsString());

                // Adds source counts.
                int sourceCount = sourceObject.getAsJsonPrimitive(COMBINATION_SOURCE_COUNT_KEY).getAsInt();
                geneDiseaseCombination.setSourceCount(source, sourceCount);

                // Prepares for iterating through pubmed evidence.
                Set<PubmedEvidence> pubmedEvidenceForSource = new HashSet<>();
                JsonArray pubmedsArray = sourceObject.getAsJsonArray(COMBINATION_SOURCE_PUBMEDS_KEY);
                Iterator<JsonElement> pubmedsIterator = pubmedsArray.iterator();

                // Retrieves each individual pubmed evidence item.
                while ( pubmedsIterator.hasNext() ) {
                    JsonObject pubmedObject = pubmedsIterator.next().getAsJsonObject();
                    URI pubmedUri = URI.create(pubmedObject.getAsJsonPrimitive(COMBINATION_SOURCE_PUBMED_URI_KEY).getAsString());
                    int pubmedYear = pubmedObject.getAsJsonPrimitive(COMBINATION_SOURCE_PUBMED_YEAR_KEY).getAsInt();
                    pubmedEvidenceForSource.add(new PubmedEvidence(pubmedUri, pubmedYear));
                }

                // Adds source pubmed evidence.
                if(!pubmedEvidenceForSource.isEmpty()) {
                    geneDiseaseCombination.setPubmedEvidenceForSource(source, pubmedEvidenceForSource);
                }
            }

            // Adds combination to collection.
            collection.add(geneDiseaseCombination);
        }

        return collection;
    }

    private Map<String,Gene> retrieveGenes(JsonObject jsonObject) {
        Map<String,Gene> genesMap = new HashMap<>();

        for( String geneId : jsonObject.keySet() ) {
            JsonObject geneObject = jsonObject.getAsJsonObject(geneId);
            String fullGeneId = Gene.ID_PREFIX + ":" + geneId;
            String fullGeneSymbolId = GeneSymbol.ID_PREFIX + ":" + geneObject.getAsJsonPrimitive(GENE_SYMBOL_KEY).getAsString();
            genesMap.put(geneId, new Gene(fullGeneId, new GeneSymbol(fullGeneSymbolId)));
        }

        return genesMap;
    }

    private Map<String,Disease> retrieveDiseases(JsonObject jsonObject) {
        Map<String,Disease> diseasesMap = new HashMap<>();

        for( String diseaseId : jsonObject.keySet() ) {
            JsonObject diseaseObject = jsonObject.getAsJsonObject(diseaseId);
            String fullDiseaseId = Disease.ID_PREFIX + ":" + diseaseId;
            String diseaseName = diseaseObject.getAsJsonPrimitive(DISEASE_NAME_KEY).getAsString();
            diseasesMap.put(diseaseId, new Disease(fullDiseaseId, diseaseName));
        }

        return diseasesMap;
    }

    private Map<String,Source> retrieveSources(JsonObject jsonObject) {
        Map<String,Source> sourcesMap = new HashMap<>();

        for( String sourceName : jsonObject.keySet() ) {
            JsonObject sourceObject = jsonObject.getAsJsonObject(sourceName);
            URI sourceUri = URI.create(sourceObject.getAsJsonPrimitive(SOURCE_URI_KEY).getAsString());
            String sourceFullName = sourceObject.getAsJsonPrimitive(SOURCE_FULL_NAME_KEY).getAsString();
            String levelString = sourceObject.getAsJsonPrimitive(SOURCE_LEVEL_KEY).getAsString();
            sourcesMap.put(sourceName, new Source(sourceUri, sourceFullName, levelString));
        }

        return sourcesMap;
    }
}
