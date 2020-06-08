package org.molgenis.vibe.core.formats.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
                new Disease("umls:C1111111", "a disease name"),
                new Disease("umls:C2222222", "another disease name"),
                new Disease("umls:C3333333", "yet another disease")
        };

        gdcs = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0], 0.5), // only counts
                new GeneDiseaseCombination(genes[0], diseases[1], 0.3), // only counts
                new GeneDiseaseCombination(genes[1], diseases[1], 0.6), // includes pubmeds
                new GeneDiseaseCombination(genes[1], diseases[2], 0.3) // only counts
        };

        sources = new Source[]{
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"), "Orphanet", Source.Level.CURATED),
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"), "Befree", Source.Level.LITERATURE)
        };

        // Adds counts.
        gdcs[0].add(sources[0]);
        gdcs[1].add(sources[0]);
        gdcs[3].add(sources[0]);

        // Adds pubmed sources.
        gdcs[2].add(sources[0]);
        gdcs[2].add(sources[1], new PubmedEvidence(URI.create("http://identifiers.org/pubmed/1"), 2000));
        gdcs[2].add(sources[1], new PubmedEvidence(URI.create("http://identifiers.org/pubmed/3"), 2001));
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

        // ### Json combinations arrays ordering (integer means ordered as integer, not necessarily output is integer) ###
        // gene-disease combinations: gene (integer, ascending), disease (string, ascending)
        // sources: name (string, ascending)
        // pubmed evidence: year (integer, descending) -> id (integer, ascending)

        // ncbigene, umls & sources JsonObjects were not ordered to reduce required processing power (Set -> List -> sort() ).
        String expectedOutput = "{\n" +
                "  \"combinations\": [\n" +
                "    {\n" +
                "      \"ncbigene\": \"1111111\",\n" +
                "      \"umls\": \"C1111111\",\n" +
                "      \"score\": 0.5,\n" +
                "      \"sources\": [\n" +
                "        {\n" +
                "          \"name\": \"Orphanet\",\n" +
                "          \"count\": 1,\n" +
                "          \"pubmed\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"ncbigene\": \"1111111\",\n" +
                "      \"umls\": \"C2222222\",\n" +
                "      \"score\": 0.3,\n" +
                "      \"sources\": [\n" +
                "        {\n" +
                "          \"name\": \"Orphanet\",\n" +
                "          \"count\": 1,\n" +
                "          \"pubmed\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"ncbigene\": \"2222222\",\n" +
                "      \"umls\": \"C2222222\",\n" +
                "      \"score\": 0.6,\n" +
                "      \"sources\": [\n" +
                "        {\n" +
                "          \"name\": \"Befree\",\n" +
                "          \"count\": 3,\n" +
                "          \"pubmed\": [\n" +
                "            {\n" +
                "              \"uri\": \"http://identifiers.org/pubmed/2\",\n" +
                "              \"year\": 2001\n" +
                "            },\n" +
                "            {\n" +
                "              \"uri\": \"http://identifiers.org/pubmed/3\",\n" +
                "              \"year\": 2001\n" +
                "            },\n" +
                "            {\n" +
                "              \"uri\": \"http://identifiers.org/pubmed/1\",\n" +
                "              \"year\": 2000\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Orphanet\",\n" +
                "          \"count\": 1,\n" +
                "          \"pubmed\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"ncbigene\": \"2222222\",\n" +
                "      \"umls\": \"C3333333\",\n" +
                "      \"score\": 0.3,\n" +
                "      \"sources\": [\n" +
                "        {\n" +
                "          \"name\": \"Orphanet\",\n" +
                "          \"count\": 1,\n" +
                "          \"pubmed\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"ncbigene\": {\n" +
                "    \"1111111\": {\n" +
                "      \"hgnc\": \"AAA\"\n" +
                "    },\n" +
                "    \"2222222\": {\n" +
                "      \"hgnc\": \"BBB\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"umls\": {\n" +
                "    \"C2222222\": {\n" +
                "      \"name\": \"another disease name\"\n" +
                "    },\n" +
                "    \"C3333333\": {\n" +
                "      \"name\": \"yet another disease\"\n" +
                "    },\n" +
                "    \"C1111111\": {\n" +
                "      \"name\": \"a disease name\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"sources\": {\n" +
                "    \"Orphanet\": {\n" +
                "      \"level\": \"curated\"\n" +
                "    },\n" +
                "    \"Befree\": {\n" +
                "      \"level\": \"literature\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        System.out.println(actualOutput);
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

}
