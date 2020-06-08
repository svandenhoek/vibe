package org.molgenis.vibe.core.formats.serialization;

import com.google.gson.*;
import org.molgenis.vibe.core.formats.*;

import java.lang.reflect.Type;
import java.util.*;

public class GeneDiseaseCollectionSerializer implements JsonSerializer<GeneDiseaseCollection> {
    @Override
    public JsonElement serialize(GeneDiseaseCollection src, Type typeOfSrc, JsonSerializationContext context) {
        // Array to store all gene-disease combinations in.
        JsonArray combinationsArray = new JsonArray();

        // Goes through all gene-disease combinations.
        for(GeneDiseaseCombination gdc : src.getGeneDiseaseCombinationsOrdered()) {
            // Creates a new gene-disease combinations.
            JsonObject combinationObject = new JsonObject();

            // Adds gene.
            JsonObject geneObject = new JsonObject();
            geneObject.addProperty("id", gdc.getGene().getFormattedId());
            geneObject.addProperty("symbol", gdc.getGene().getSymbol().getFormattedId());
            combinationObject.add("gene", geneObject);

            // Adds disease.
            JsonObject diseaseObject = new JsonObject();
            diseaseObject.addProperty("id", gdc.getDisease().getFormattedId());
            combinationObject.add("disease", diseaseObject);

            // Adds score.
            combinationObject.addProperty("disgenet-score", gdc.getDisgenetScore());

            // Adds sources.
            JsonArray evidenceArray = new JsonArray();

            // sources: generate sorted list of sources.
            List<Source> orderedSources = new ArrayList<>();
            orderedSources.addAll(gdc.getSourcesCount().keySet());
            Collections.sort(orderedSources);

            // sources: Goes through all ordered sources.
            for(Source source : orderedSources) {
                JsonObject evidenceItemObject = new JsonObject();
                evidenceItemObject.addProperty("id", source.getName());
                evidenceItemObject.addProperty("type", source.getLevel().getReadableString());
                evidenceItemObject.addProperty("evidence-count", gdc.getCountForSource(source));

                // sources: Adds pubmed evidence (empty array if no pubmed evidence).
                JsonArray pubmedEvidenceArray = new JsonArray();

                List<PubmedEvidence> evidenceList = gdc.getPubmedEvidenceForSourceSortedByReleaseDate(source);
                if(evidenceList != null) {
                    for (PubmedEvidence pubmedEvidence : evidenceList) {
                        JsonObject pubmedEvidenceObject = new JsonObject();
                        pubmedEvidenceObject.addProperty("uri", pubmedEvidence.getUri().toString());
                        pubmedEvidenceObject.addProperty("release-year", pubmedEvidence.getReleaseYear());
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
}
