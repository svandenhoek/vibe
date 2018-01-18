package org.molgenis.vibe.io.disgenet_rdf;

import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.molgenis.vibe.TestFile;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests the {@link RdfFileReader} (based on Apache Jena for RDF file reading/querying).
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
public class RdfFileReaderTester {
    private RdfFileReader reader1;
    private RdfFileReader reader2;

    @BeforeClass
    public void initialize() {
        String reader1File = TestFile.GDA1_RDF.getFilePath();
        reader1 = new DisgenetRdfFileReader();
        reader1.readFile(reader1File);

        String[] reader2Files = new String[]{TestFile.GDA1_RDF.getFilePath(),
                TestFile.GENE_RDF.getFilePath(),
                TestFile.DISEASE_DISEASE_RDF.getFilePath()};
        reader2 = new DisgenetRdfFileReader();
        reader2.readFiles(reader2Files);
    }

    @Test(expectedExceptions = QueryParseException.class)
    public void testInvalidQuery() {
        ResultSet results = reader1.useQuery("SELECT ?id \n" +
                "WHERE { brandNetelKaasMetEenDruppeltjeMunt?! }");
    }

    @Test
    public void testEmptyResults() {
        ResultSet results = reader1.useQuery("SELECT ?gda ?id ?title \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/0> dcterms:identifier ?id ; \n" +
                "dcterms:title ?title }");

        Assert.assertEquals(results.hasNext(), false);
    }

    @Test
    public void testSingleFile() {
        ResultSet results = reader1.useQuery("SELECT ?id \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id }");

        Assert.assertEquals(results.hasNext(), true, "no match found");
        QuerySolution result = results.next();
        Assert.assertEquals(results.hasNext(), false, "more than 1 match found");
        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNe8f5323c9341d6534c17879604dc6bbb");
    }

    @Test
    public void testSingleFileWithLimit() {
        ResultSet results = reader1.useQuery("SELECT ?id \n" +
                "WHERE { ?gda dcterms:identifier ?id } \n" +
                "LIMIT 3");

        int counter = 0;
        while(results.hasNext()) {
            counter += 1;
            results.next();
        }

        Assert.assertEquals(counter, 3);
    }

    @Test
    public void testSingleFileWithMultiFileQuery() {
        ResultSet results = reader1.useQuery("SELECT ?id ?gene ?geneTitle ?diseaseTitle \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ;" +
                "dcterms:title ?geneTitle ." +
                "?disease rdf:type ncit:C7057 ;" +
                "dcterms:title ?diseaseTitle }");

        Assert.assertEquals(results.hasNext(), false, "gene/disease ncit code are not present in gda file but somehow a match was found");
    }

    @Test
    public void testMultiFile1() {
        // sio:SIO_000628 <http://identifiers.org/ncbigene/6607> , <http://linkedlifedata.com/resource/umls/id/C0043116> .
        ResultSet results = reader2.useQuery("SELECT ?id ?gene ?geneTitle ?disease ?diseaseTitle \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ;" +
                "dcterms:title ?geneTitle ." +
                "?disease rdf:type ncit:C7057 ;" +
                "dcterms:title ?diseaseTitle }");

        Assert.assertEquals(results.hasNext(), true, "no match found");
        QuerySolution result = results.next();
        Assert.assertEquals(results.hasNext(), false, "more than 1 match found");

        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNe8f5323c9341d6534c17879604dc6bbb");
        Assert.assertEquals(result.get("gene").toString(), "http://identifiers.org/ncbigene/6607");
        Assert.assertEquals(result.get("geneTitle").asLiteral().getString(), "survival of motor neuron 2, centromeric");
        Assert.assertEquals(result.get("disease").toString(), "http://linkedlifedata.com/resource/umls/id/C0043116");
        Assert.assertEquals(result.get("diseaseTitle").asLiteral().getString(), "HMN (Hereditary Motor Neuropathy) Proximal Type I");
    }

    @Test
    public void testMultiFile2() {
        // sio:SIO_000628 <http://linkedlifedata.com/resource/umls/id/C0268495> , <http://identifiers.org/ncbigene/4157> .
        ResultSet results = reader2.useQuery("SELECT ?id ?gene ?geneTitle ?disease ?diseaseTitle \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNbbaeeb8e8b5fa93f23ca212dd9c281ca> dcterms:identifier ?id ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ;" +
                "dcterms:title ?geneTitle ." +
                "?disease rdf:type ncit:C7057 ;" +
                "dcterms:title ?diseaseTitle }");

        Assert.assertEquals(results.hasNext(), true, "no match found");
        QuerySolution result = results.next();
        Assert.assertEquals(results.hasNext(), false, "more than 1 match found");

        Assert.assertEquals(result.get("id").asLiteral().getString(), "disgenet:DGNbbaeeb8e8b5fa93f23ca212dd9c281ca");
        Assert.assertEquals(result.get("gene").asResource().getURI(), "http://identifiers.org/ncbigene/4157");
        Assert.assertEquals(result.get("geneTitle").asLiteral().getString(), "melanocortin 1 receptor");
        Assert.assertEquals(result.get("disease").asResource().getURI(), "http://linkedlifedata.com/resource/umls/id/C0268495");
        Assert.assertEquals(result.get("diseaseTitle").asLiteral().getString(), "Oculocutaneous albinism type 2");
    }
}
