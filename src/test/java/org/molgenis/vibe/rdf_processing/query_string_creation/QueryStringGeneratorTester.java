package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.molgenis.vibe.TestData;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.rdf_processing.QueryTester;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;
import org.molgenis.vibe.rdf_processing.querying.QueryRunnerRewindable;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the {@link QueryStringGenerator} (based on Apache Jena for RDF file reading/querying).
 *
 * Note that these tests use data from DisGeNET for validation. These files are not provided (though a bash download
 * script is present in the GitHub repository). For validation purposes some data (such as gene-disease association IDs)
 * are present within this test class. However, this was kept as minimal as possible while still being able to actually
 * test the functioning of the code and only reflects what is EXPECTED to be found within the DisGeNET dataset when using
 * the query (on a technical basis). The DisGeNET RDF dataset can be downloaded from http://rdf.disgenet.org/download/
 * and the license can be found on http://www.disgenet.org/ds/DisGeNET/html/legal.html .
 */
public class QueryStringGeneratorTester extends QueryTester {
    private ModelReader reader;
    private QueryRunnerRewindable runner;
    private final String prefixes = DisgenetQueryStringGenerator.getPrefixes();

    @BeforeClass
    public void beforeClass() throws IOException {
        reader = new TripleStoreDbReader(TestData.TDB_MINI.getDir());
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        reader.close();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        runner.close();
    }

    @Test(expectedExceptions = QueryParseException.class)
    public void testInvalidQuery() {
        new QueryRunner(reader.getModel(), new QueryString(prefixes + "SELECT ?id \n" +
                "WHERE { brandNetelKaasMetEenDruppeltjeMunt?! }"));
    }

    @Test
    public void testEmptyResults() {
        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?id \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/0> dcterms:identifier ?id . }"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
        Assert.assertEquals(runner.hasNext(), false);
    }

    @Test
    public void testSingleGdaId() {
        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?id \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNa4eb0beb985996e1956b22097c0ad0de> dcterms:identifier ?id }"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
        assertSingleFieldFromRunnerOutput(runner, "id", Arrays.asList("disgenet:DGNa4eb0beb985996e1956b22097c0ad0de"));
    }

    @Test
    public void testSingleGdaIdReferences() {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://identifiers.org/ncbigene/1289");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C0039516");
        List<String> actualOutput = new ArrayList<>();

        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?value \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNa4eb0beb985996e1956b22097c0ad0de> sio:SIO_000628 ?value }"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
        assertSingleFieldFromRunnerOutput(runner, "value", expectedOutput);
    }

    @Test
    public void testLimit() {
        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes +  "SELECT ?gda \n" +
                "WHERE { ?gda rdf:type sio:SIO_001121 } \n" +
                "LIMIT 3"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
        assertQueryResultCountWithExpectedResult(runner, 3); // test file contains 9
    }

    @Test
    public void testGdaGeneDiseaseQuery() {
        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?id ?gene ?disease \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNa4eb0beb985996e1956b22097c0ad0de> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 . \n" +
                "?disease rdf:type ncit:C7057 . \n" +
                "}"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();

        Assert.assertEquals(runner.hasNext(), true, "no match found");
        QuerySolution result = runner.next();
        Assert.assertEquals(runner.hasNext(), false, "more than 1 match found");

        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNa4eb0beb985996e1956b22097c0ad0de");
        Assert.assertEquals(result.get("gene").toString(), "http://identifiers.org/ncbigene/1289");
        Assert.assertEquals(result.get("disease").toString(), "http://linkedlifedata.com/resource/umls/id/C0039516");
    }

    @Test
    public void testDiseaseRetrievalForHpo() {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1849955");
        expectedOutput.add("http://linkedlifedata.com/resource/umls/id/C1834674");

        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?disease \n" +
                "WHERE { \n" +
                "VALUES ?hpo {<http://purl.obolibrary.org/obo/HP_0002996>} \n" +
                "{ \n" +
                "?hpo skos:exactMatch ?disease . \n" +
                "} \n" +
                "UNION \n" +
                "{ \n" +
                "?pda rdf:type sio:SIO_000897 ; \n" +
                "sio:SIO_000628 ?hpo , ?disease . \n" +
                "} \n" +
                "?disease rdf:type ncit:C7057 . \n" +
                "}"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
        assertSingleFieldFromRunnerOutput(runner, "disease", expectedOutput);
    }

    @Test
    public void testGeneRetrievalForHpo() {
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("http://identifiers.org/ncbigene/1280");
        expectedOutput.add("http://identifiers.org/ncbigene/8243");
        expectedOutput.add("http://identifiers.org/ncbigene/1291");
        expectedOutput.add("http://identifiers.org/ncbigene/1292");

        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?gene \n" +
                "WHERE { \n" +
                "VALUES ?hpo {<http://purl.obolibrary.org/obo/HP_0002996>} \n" +
                "{ \n" +
                "?hpo rdf:type sio:SIO_010056 ; \n" +
                "skos:exactMatch ?disease . \n" +
                "} \n" +
                "UNION \n" +
                "{ \n" +
                "?pda rdf:type sio:SIO_000897 ; \n" +
                "sio:SIO_000628 ?hpo , ?disease . \n" +
                "} \n" +
                "?disease rdf:type ncit:C7057 . \n" +
                "?gda sio:SIO_000628 ?disease , ?gene ; \n" +
                "rdf:type ?type . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gene rdf:type ncit:C16612 . \n" +
                "}"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
        assertSingleFieldFromRunnerOutput(runner, "gene", expectedOutput);
    }

    @Test
    public void testGdaDataRetrievalForGenes() {
        runner = new QueryRunnerRewindable(reader.getModel(), new QueryString(prefixes + "SELECT ?gene ?geneId ?geneTitle ?geneSymbolTitle ?disease ?diseaseId ?diseaseTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
                "WHERE { \n" +
                "VALUES ?gene {<http://identifiers.org/ncbigene/1280>}" +
                "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
                "rdf:type ?type ; \n" +
                "sio:SIO_000216 ?gdaScore ; \n" +
                "sio:SIO_000253 ?gdaSource . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" + // [1] -> [2]
                "?gene rdf:type ncit:C16612 ; \n" +
                "dcterms:identifier ?geneId ; \n" +
                "dcterms:title ?geneTitle ; \n" +
                "sio:SIO_000205 ?geneSymbol . \n" +
                "?geneSymbol rdf:type ncit:C43568 ; \n" +
                "dcterms:title ?geneSymbolTitle . \n" +
                "?gdaScore rdf:type ncit:C25338 ; \n" +
                "sio:SIO_000300 ?gdaScoreNumber . \n" +
                "?disease rdf:type ncit:C7057 ; \n" +
                "dcterms:identifier ?diseaseId ; \n" +
                "dcterms:title ?diseaseTitle . \n" +
                "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
                "}"));
        ResultSetFormatter.out(System.out, runner.getResultSet());
        runner.reset();
//        assertSingleFieldFromRunnerOutput(runner, "gene", expectedOutput);
    }
}
