package org.molgenis.vibe.rdf_querying;

import com.google.common.base.Stopwatch;
import com.google.common.io.ByteStreams;
import org.apache.jena.query.ResultSetFormatter;
import org.molgenis.vibe.TestFile;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RetrieveGeneByHpoSpeed {
    private static final String database = TestFile.TDB_FULL.getFilePath();
    private static final int testRepeats = 3;

    ModelReader reader;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        reader = new TripleStoreDbReader(database);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        reader.close();
    }

    // timeOut gives "org.apache.jena.tdb.transaction.TDBTransactionException: Not in a transaction".
    // Can't run on big tests because too slow.
    @Test(groups = {"benchmarking", "tooSlow"})
    public void checkSpeedGdaFirst() {
        String query = "SELECT ?hpo ?disease ?geneTitle \n" +
                "WHERE { ?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gda rdf:type ?type ; \n" +
                "sio:SIO_000628 ?gene , ?disease . \n" +
                "?gene rdf:type ncit:C16612 ; \n" +
                "dcterms:title ?geneTitle . \n" +
                "?disease rdf:type ncit:C7057 . \n" +
                "?pda rdf:type sio:SIO_000897 ;" +
                "sio:SIO_000628 ?hpo , ?disease ." +
                "?hpo rdf:type sio:SIO_010056 ;" +
                "dcterms:identifier \"hp:0009811\"^^xsd:string }";
        String[] times = runQuery(DisgenetQueryGenerator.getPrefixes() + query);
        System.out.println("gda times: " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    @Test(groups = {"benchmarking"})
    public void checkSpeedPdaFirst() {
        String query = "SELECT ?hpo ?disease ?geneTitle \n" +
                "WHERE { ?pda rdf:type sio:SIO_000897 ; \n" +
                "sio:SIO_000628 ?hpo , ?disease . \n" +
                "?hpo rdf:type sio:SIO_010056 ; \n" +
                "dcterms:identifier \"hp:0009811\"^^xsd:string . \n" +
                "?disease rdf:type ncit:C7057 . \n" +
                "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
                "rdf:type ?type . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gene rdf:type ncit:C7057 ; " +
                "dcterms:title ?geneTitle .}";
        String[] times = runQuery(DisgenetQueryGenerator.getPrefixes() + query);
        System.out.println("pda times: " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    @Test(groups = {"benchmarking"})
    public void checkSpeedHpoFirst() {
        String query = "SELECT ?hpo ?disease ?geneTitle \n" +
                "WHERE { ?hpo rdf:type sio:SIO_010056 ; \n" +
                "dcterms:identifier \"hp:0009811\"^^xsd:string . \n" +
                "?pda rdf:type sio:SIO_000897 ; \n" +
                "sio:SIO_000628 ?hpo , ?disease . \n" +
                "?disease rdf:type ncit:C7057 . \n" +
                "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
                "rdf:type ?type . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gene rdf:type ncit:C7057 ; \n" +
                "dcterms:title ?geneTitle .}";
        String[] times = runQuery(DisgenetQueryGenerator.getPrefixes() + query);
        System.out.println("hpo times: " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    private String[] runQuery(String queryString) {
        String[] times = new String[testRepeats];
        for(int i = 0; i<testRepeats;i++) {
            Stopwatch timer = Stopwatch.createStarted();
            QueryRunner runner = new QueryRunner(reader.getModel(), DisgenetQueryGenerator.getPrefixes() + queryString);
            ResultSetFormatter.out(ByteStreams.nullOutputStream(), runner.getResultSet());
            times[i] = timer.stop().toString();
            runner.close();
        }
        return times;
    }
}
