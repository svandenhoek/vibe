package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.TestFile;
import org.molgenis.vibe.io.ModelFilesReader;
import org.molgenis.vibe.io.ModelReader;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests the {@link SparqlQueryGenerator} (based on Apache Jena for RDF file reading/querying).
 *
 * IMPORTANT: Run TestNGPreprocessing.sh before using TestNG!
 *
 * Note that these tests use data from DisGeNET for validation. These files are not provided (though a bash download
 * script is present in the GitHub repository). For validation purposes some data (such as gene-disease association IDs)
 * are present within this test class. However, this was kept as minimal as possible while still being able to actually
 * test the functioning of the code and only reflects what is EXPECTED to be found within the DisGeNET dataset when using
 * the query (on a technical basis). The DisGeNET RDF dataset can be downloaded from http://rdf.disgenet.org/download/
 * and the license can be found on http://www.disgenet.org/ds/DisGeNET/html/legal.html .
 */
public class SparqlQueryGeneratorTester {
    private ModelReader reader1;
    private ModelReader reader2;
    private QueryRunner runner;
    private final String prefixes = DisgenetQueryGenerator.getPrefixes();

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        String fileSet1 = TestFile.GDA1_RDF.getFilePath();
        String[] fileSet2 = new String[]{TestFile.GDA1_RDF.getFilePath(),
                TestFile.GENE_RDF.getFilePath(),
                TestFile.DISEASE_DISEASE_RDF.getFilePath()};

        reader1 = new ModelFilesReader(fileSet1);
        reader2 = new ModelFilesReader(fileSet2);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        reader1.close();
        reader2.close();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        runner.close();
    }

    @Test(expectedExceptions = QueryParseException.class)
    public void testInvalidQuery() {
        new QueryRunner(reader1.getModel(), prefixes + "SELECT ?id \n" +
                "WHERE { brandNetelKaasMetEenDruppeltjeMunt?! }");
    }

    @Test
    public void testEmptyResults() {
        runner = new QueryRunner(reader1.getModel(), prefixes + "SELECT ?gda ?id ?title \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/0> dcterms:identifier ?id ; \n" +
                "dcterms:title ?title }");

        Assert.assertEquals(runner.hasNext(), false);
    }

    @Test
    public void testSingleFileGdaId() {
        runner = new QueryRunner(reader1.getModel(), prefixes + "SELECT ?id \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id }");

        Assert.assertEquals(runner.hasNext(), true, "no match found");
        QuerySolution result = runner.next();
        Assert.assertEquals(runner.hasNext(), false, "more than 1 match found");
        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNe8f5323c9341d6534c17879604dc6bbb");
    }

    @Test
    public void testSingleFileGdaReference() {
        Set<String> expectedReferences = new HashSet<>();
        expectedReferences.add("http://identifiers.org/ncbigene/6607");
        expectedReferences.add("http://linkedlifedata.com/resource/umls/id/C0043116");
        Set<String> actualReferences = new HashSet<>();

        runner = new QueryRunner(reader1.getModel(), prefixes + "SELECT ?value \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> sio:SIO_000628 ?value }");

        int i = 0;
        while(runner.hasNext()) {
            QuerySolution result = runner.next();
            actualReferences.add(result.get("value").toString());
        }

        Assert.assertEquals(actualReferences, expectedReferences, "");
    }

    @Test
    public void testSingleFileWithLimit() {
        runner = new QueryRunner(reader1.getModel(), prefixes +  "SELECT ?id \n" +
                "WHERE { ?gda dcterms:identifier ?id } \n" +
                "LIMIT 3");

        int counter = 0;
        while(runner.hasNext()) {
            counter += 1;
            runner.next();
        }

        Assert.assertEquals(counter, 3);
    }

    @Test
    public void testSingleFileWithMultiFileQuery() {
        runner = new QueryRunner(reader1.getModel(), prefixes + "SELECT ?id ?gene ?geneTitle ?diseaseTitle \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ;" +
                "dcterms:title ?geneTitle ." +
                "?disease rdf:type ncit:C7057 ;" +
                "dcterms:title ?diseaseTitle }");

        Assert.assertEquals(runner.hasNext(), false, "gene/disease ncit code are not present in gda file but somehow a match was found");
    }

    @Test
    public void testMultiFile1() {
        // sio:SIO_000628 <http://identifiers.org/ncbigene/6607> , <http://linkedlifedata.com/resource/umls/id/C0043116> .
        runner = new QueryRunner(reader2.getModel(), prefixes + "SELECT ?id ?gene ?geneTitle ?disease ?diseaseTitle \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ;" +
                "dcterms:title ?geneTitle ." +
                "?disease rdf:type ncit:C7057 ;" +
                "dcterms:title ?diseaseTitle }");

        Assert.assertEquals(runner.hasNext(), true, "no match found");
        QuerySolution result = runner.next();
        Assert.assertEquals(runner.hasNext(), false, "more than 1 match found");

        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNe8f5323c9341d6534c17879604dc6bbb");
        Assert.assertEquals(result.get("gene").toString(), "http://identifiers.org/ncbigene/6607");
        Assert.assertEquals(result.get("disease").toString(), "http://linkedlifedata.com/resource/umls/id/C0043116");
    }

    @Test
    public void testMultiFile2() {
        // sio:SIO_000628 <http://linkedlifedata.com/resource/umls/id/C0268495> , <http://identifiers.org/ncbigene/4157> .
        runner = new QueryRunner(reader2.getModel(), prefixes + "SELECT ?id ?gene ?geneTitle ?disease ?diseaseTitle \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNbbaeeb8e8b5fa93f23ca212dd9c281ca> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ;" +
                "dcterms:title ?geneTitle ." +
                "?disease rdf:type ncit:C7057 ;" +
                "dcterms:title ?diseaseTitle }");

        Assert.assertEquals(runner.hasNext(), true, "no match found");
        QuerySolution result = runner.next();
        Assert.assertEquals(runner.hasNext(), false, "more than 1 match found");

        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNbbaeeb8e8b5fa93f23ca212dd9c281ca");
        Assert.assertEquals(result.get("gene").asResource().getURI(), "http://identifiers.org/ncbigene/4157");
        Assert.assertEquals(result.get("disease").asResource().getURI(), "http://linkedlifedata.com/resource/umls/id/C0268495");
    }
}
