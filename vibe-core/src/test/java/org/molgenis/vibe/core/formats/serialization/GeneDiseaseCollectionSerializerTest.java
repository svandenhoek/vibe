package org.molgenis.vibe.core.formats.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.formats.*;

import java.net.URI;
import java.util.Arrays;

public class GeneDiseaseCollectionSerializerTest {
    private static GeneDiseaseCombination[] gdcs;
    private static Gene[] genes;
    private static Disease[] diseases;
    private static Source[] sources;

    private static Gson gson;

    @BeforeAll
    public static void beforeAll() {
        genes = new Gene[]{
                new Gene("ncbigene:1111111", new GeneSymbol("hgnc:AAA")),
                new Gene("ncbigene:2222222", new GeneSymbol("hgnc:BBB"))
        };

        diseases = new Disease[]{
                new Disease("umls:C1111111"),
                new Disease("umls:C2222222"),
        };

        gdcs = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0], 0.5), // no evidence
                new GeneDiseaseCombination(genes[0], diseases[1], 0.3), // only counts
                new GeneDiseaseCombination(genes[1], diseases[1], 0.6) // includes pubmeds
        };

        sources = new Source[]{
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"), "Orphanet", Source.Level.CURATED),
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"), "Befree", Source.Level.LITERATURE)
        };

        gdcs[1].add(sources[0]);

        gdcs[2].add(sources[0]);
        gdcs[2].add(sources[1], new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), 2000));
        gdcs[2].add(sources[1], new PubmedEvidence(URI.create("http://identifiers.org/pubmed/2"), 2001));

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(GeneDiseaseCollection.class, new GeneDiseaseCollectionSerializer());
        gson = gsonBuilder.create();
    }

    /**
     * Tests generated Json. Json arrays should be ordered as following:
     * - gene-disease combinations: gene (integer, ascending) -> disease (string, ascending)
     * - sources: full name (string, ascending)
     * - pubmed evidence: year (int, descending) -> id (integer, ascending)
     */
    @Test
    public void testSerializer() {
        GeneDiseaseCollection collection = new GeneDiseaseCollection();
        collection.addAll(Arrays.asList(gdcs));
        String actualOutput = gson.toJson(collection);

        // ### Json arrays ordering ###
        // gene-disease combinations: gene (first), disease (second)
        // sources: name
        // pubmed evidence: year (descending)
        String expectedOutput = "[\n" +
                "  {\n" +
                "    \"gene\": {\n" +
                "      \"id\": \"ncbigene:1111111\",\n" +
                "      \"symbol\": \"hgnc:AAA\"\n" +
                "    },\n" +
                "    \"disease\": {\n" +
                "      \"id\": \"umls:C1111111\"\n" +
                "    },\n" +
                "    \"disgenet-score\": 0.5,\n" +
                "    \"sources\": []\n" +
                "  },\n" +
                "  {\n" +
                "    \"gene\": {\n" +
                "      \"id\": \"ncbigene:1111111\",\n" +
                "      \"symbol\": \"hgnc:AAA\"\n" +
                "    },\n" +
                "    \"disease\": {\n" +
                "      \"id\": \"umls:C2222222\"\n" +
                "    },\n" +
                "    \"disgenet-score\": 0.3,\n" +
                "    \"sources\": [\n" +
                "      {\n" +
                "        \"id\": \"Orphanet\",\n" +
                "        \"type\": \"curated\",\n" +
                "        \"evidence-count\": 1,\n" +
                "        \"pubmed\": []\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"gene\": {\n" +
                "      \"id\": \"ncbigene:2222222\",\n" +
                "      \"symbol\": \"hgnc:BBB\"\n" +
                "    },\n" +
                "    \"disease\": {\n" +
                "      \"id\": \"umls:C2222222\"\n" +
                "    },\n" +
                "    \"disgenet-score\": 0.6,\n" +
                "    \"sources\": [\n" +
                "      {\n" +
                "        \"id\": \"Befree\",\n" +
                "        \"type\": \"literature\",\n" +
                "        \"evidence-count\": 2,\n" +
                "        \"pubmed\": [\n" +
                "          {\n" +
                "            \"uri\": \"http://identifiers.org/pubmed/2\",\n" +
                "            \"release-year\": 2001\n" +
                "          },\n" +
                "          {\n" +
                "            \"uri\": \"http://identifiers.org/pubmed/1\",\n" +
                "            \"release-year\": 2000\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"Orphanet\",\n" +
                "        \"type\": \"curated\",\n" +
                "        \"evidence-count\": 1,\n" +
                "        \"pubmed\": []\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        Assertions.assertEquals(expectedOutput, actualOutput);
    }

}
