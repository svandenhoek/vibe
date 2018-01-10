package org.molgenis.vibe.io.disgenet_rdf;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class DisgenetGdaFileReader extends DisgenetRdfFileReader {

    public void read(String file) {
        readFile(file);
        // TODO retrieve gene/disease (needs external files for defining ncit value)
        // "PREFIX dcterms: <http://purl.org/dc/terms/> PREFIX sio: <http://semanticscience.org/resource/> " +
        // "SELECT ?id ?name ?gene ?disease " +
        // "WHERE {dcterms:identifier ?id ; dcterms:title ?name ; sio:SIO_000628 ?gene,?disease ;}"
        ResultSet results = useQuery("SELECT ?gda ?id ?title \n" +
                "WHERE { ?gda dcterms:identifier ?id ; \n" +
                "dcterms:title ?title }");
        while(results.hasNext()) {
            QuerySolution result = results.next();
            System.out.println(result.get("gda") + " - " + result.get("id") + " - " + result.get("title"));
        }
    }
}