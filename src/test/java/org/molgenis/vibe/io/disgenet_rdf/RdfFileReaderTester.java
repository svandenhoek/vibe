package org.molgenis.vibe.io.disgenet_rdf;

import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RdfFileReaderTester extends Tester{
    private RdfFileReader reader1;

    @BeforeClass
    public void initialize() {
        reader1 = new DisgenetGdaFileReader();
        reader1.readFile(getClassLoader().getResource("gda_SIO_001347.ttl").getFile());
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
        ResultSet results = reader1.useQuery("SELECT ?gda ?id ?title \n" +
                "WHERE { <http://rdf.disgenet.org/resource/gda/DGNe8f5323c9341d6534c17879604dc6bbb> dcterms:identifier ?id ; \n" +
                "dcterms:title ?title }");
        if(!results.hasNext()) { Assert.fail("no hit found");}
        QuerySolution result = results.next();
        if(results.hasNext()) { Assert.fail("more than 1 hit found");}
        Assert.assertEquals(result.get("id").toString(), "disgenet:DGNe8f5323c9341d6534c17879604dc6bbb");
    }

}
