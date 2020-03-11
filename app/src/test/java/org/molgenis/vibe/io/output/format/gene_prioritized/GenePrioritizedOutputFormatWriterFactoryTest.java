package org.molgenis.vibe.io.output.format.gene_prioritized;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.output.format.OutputFormatWriter;
import org.molgenis.vibe.io.output.target.StdoutOutputWriter;
import org.molgenis.vibe.query_output_digestion.prioritization.Prioritizer;
import org.molgenis.vibe.query_output_digestion.prioritization.gene.HighestSingleDisgenetScoreGenePrioritizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

public class GenePrioritizedOutputFormatWriterFactoryTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    private static final StdoutOutputWriter writer = new StdoutOutputWriter();
    private static GeneDiseaseCollection collection;
    private static Prioritizer<Gene> prioritizer;

    @BeforeAll
    public static void beforeAll() {
        // Redirect stdout for tests.
        System.setOut(new PrintStream(outContent));

        // Create simple output collection.
        Disease[] diseases = new Disease[]{
                new Disease("umls:C0265292"),
                new Disease("umls:C0220687"),
                new Disease("umls:C1835764")
        };

        Gene[] genes = new Gene[]{
                new Gene("ncbigene:2697", new GeneSymbol("hgnc:GJA1")),
                new Gene("ncbigene:29123", new GeneSymbol("hgnc:ANKRD11"))
        };

        GeneDiseaseCombination[] geneDiseaseCombinations = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0], 0.31E0),
                new GeneDiseaseCombination(genes[1], diseases[1], 0.8E0),
                new GeneDiseaseCombination(genes[1], diseases[2], 0.1E0)
        };

        Source[] sources = new Source[] {
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/BEFREE"), "BeFree 2018 Dataset Distribution", Source.Level.LITERATURE),
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/ORPHANET"), "Orphanet 2017 Dataset Distribution", Source.Level.CURATED),
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/CLINVAR"), "ClinVar 2018 Dataset Distribution", Source.Level.CURATED),
                new Source(URI.create("http://rdf.disgenet.org/v6.0.0/void/HPO"), "HPO", Source.Level.CURATED)
        };
        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/23951358"));
        geneDiseaseCombinations[1].add(sources[0], URI.create("http://identifiers.org/pubmed/23494856"));
        geneDiseaseCombinations[1].add(sources[2], URI.create("http://identifiers.org/pubmed/26633545"));
        geneDiseaseCombinations[1].add(sources[1]);
        geneDiseaseCombinations[2].add(sources[3]);

        collection = new GeneDiseaseCollection(new HashSet<>(Arrays.asList(geneDiseaseCombinations)));

        // Mock prioritizer.
        prioritizer = Mockito.mock(HighestSingleDisgenetScoreGenePrioritizer.class);
        Mockito.when(prioritizer.getPriority()).thenReturn(Arrays.asList(genes[1], genes[0]));
    }

    @AfterEach
    public void afterEach() {
        outContent.reset();
    }

    @AfterAll
    public static void afterAll() {
        System.setOut(originalOut);
    }

    @Test
    public void testSimple() throws IOException {
        OutputFormatWriter formatWriter = GenePrioritizedOutputFormatWriterFactory.SIMPLE.create(writer, collection, prioritizer);
        formatWriter.run();
        String expectedOutput = "29123,2697";
        Assertions.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testDefaultWithId() throws IOException {
        OutputFormatWriter formatWriter = GenePrioritizedOutputFormatWriterFactory.REGULAR_ID.create(writer, collection, prioritizer);
        formatWriter.run();
        String expectedOutput = "gene (NCBI)\tgene symbol (HGNC)\thighest GDA score\tdiseases (UMLS) with sources per disease" + System.lineSeparator() +
                "29123\tANKRD11\t0.8\tC0220687 (0.8):23494856,26633545|C1835764 (0.1)" + System.lineSeparator() +
                "2697\tGJA1\t0.31\tC0265292 (0.31):23951358" + System.lineSeparator();
        Assertions.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testDefaultWithUri() throws IOException {
        OutputFormatWriter formatWriter = GenePrioritizedOutputFormatWriterFactory.REGULAR_URI.create(writer, collection, prioritizer);
        formatWriter.run();
        String expectedOutput = "gene (NCBI)\tgene symbol (HGNC)\thighest GDA score\tdiseases (UMLS) with sources per disease" + System.lineSeparator() +
                "http://identifiers.org/ncbigene/29123\thttp://identifiers.org/hgnc.symbol/ANKRD11\t0.8\thttp://linkedlifedata.com/resource/umls/id/C0220687 (0.8):http://identifiers.org/pubmed/23494856,http://identifiers.org/pubmed/26633545|http://linkedlifedata.com/resource/umls/id/C1835764 (0.1)" + System.lineSeparator() +
                "http://identifiers.org/ncbigene/2697\thttp://identifiers.org/hgnc.symbol/GJA1\t0.31\thttp://linkedlifedata.com/resource/umls/id/C0265292 (0.31):http://identifiers.org/pubmed/23951358" + System.lineSeparator();
        Assertions.assertEquals(expectedOutput, outContent.toString());
    }
}
