package org.molgenis.vibe.rdf_processing.querying;

import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetQueryStringGenerator;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RetrieveGdaByGeneSpeedComparison extends QuerySpeedComparison {
    private static final int testRepeats = 5;

    @Test(groups = {"benchmarking"})
    public void checkSpeedValuesFirst() {
        String query = "SELECT ?gene ?disease ?diseaseId ?diseaseTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
                "WHERE { \n" +
                "VALUES ?gene {<http://identifiers.org/ncbigene/1311>} \n" +
                "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
                "rdf:type ?type ; \n" +
                "sio:SIO_000216 ?gdaScore ; \n" +
                "sio:SIO_000253 ?gdaSource . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gdaScore rdf:type ncit:C25338 ; \n" +
                "sio:SIO_000300 ?gdaScoreNumber . \n" +
                "?disease rdf:type ncit:C7057 ; \n" +
                "dcterms:identifier ?diseaseId ; \n" +
                "dcterms:title ?diseaseTitle . \n" +
                "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
                "}";
        String[] times = runQuery(DisgenetQueryStringGenerator.getPrefixes() + query, testRepeats);
        System.out.println("values first: " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    @Test(groups = {"benchmarking"})
    public void checkSpeedGdaFirstValuesSecondV1() {
        String query = "SELECT ?gene ?disease ?diseaseId ?diseaseTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
                "WHERE { \n" +
                "?gda sio:SIO_000628 ?gene . \n" +
                "VALUES ?gene {<http://identifiers.org/ncbigene/1311>} \n" +
                "?gda sio:SIO_000628 ?disease ; \n" +
                "rdf:type ?type ; \n" +
                "sio:SIO_000216 ?gdaScore ; \n" +
                "sio:SIO_000253 ?gdaSource . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gdaScore rdf:type ncit:C25338 ; \n" +
                "sio:SIO_000300 ?gdaScoreNumber . \n" +
                "?disease rdf:type ncit:C7057 ; \n" +
                "dcterms:identifier ?diseaseId ; \n" +
                "dcterms:title ?diseaseTitle . \n" +
                "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
                "}";
        String[] times = runQuery(DisgenetQueryStringGenerator.getPrefixes() + query, testRepeats);
        System.out.println("gda first - values second (V1): " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    @Test(groups = {"benchmarking"})
    public void checkSpeedGdaFirstValuesSecondV2() {
        String query = "SELECT ?gene ?disease ?diseaseId ?diseaseTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
                "WHERE { \n" +
                "?gda sio:SIO_000628 ?gene , ?disease . \n" +
                "VALUES ?gene {<http://identifiers.org/ncbigene/1311>} \n" +
                "?gda rdf:type ?type ; \n" +
                "sio:SIO_000216 ?gdaScore ; \n" +
                "sio:SIO_000253 ?gdaSource . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gdaScore rdf:type ncit:C25338 ; \n" +
                "sio:SIO_000300 ?gdaScoreNumber . \n" +
                "?disease rdf:type ncit:C7057 ; \n" +
                "dcterms:identifier ?diseaseId ; \n" +
                "dcterms:title ?diseaseTitle . \n" +
                "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
                "}";
        String[] times = runQuery(DisgenetQueryStringGenerator.getPrefixes() + query, testRepeats);
        System.out.println("gda first - values second (V2): " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    @Test(groups = {"benchmarking"})
    public void checkSpeedGdaFirstValuesSecondV3() {
        String query = "SELECT ?gene ?disease ?diseaseId ?diseaseTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
                "WHERE { \n" +
                "?gda sio:SIO_000628 ?gene , ?disease ; \n" +
                "rdf:type ?type ; \n" +
                "sio:SIO_000216 ?gdaScore ; \n" +
                "sio:SIO_000253 ?gdaSource . \n" +
                "VALUES ?gene {<http://identifiers.org/ncbigene/1311>} \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gdaScore rdf:type ncit:C25338 ; \n" +
                "sio:SIO_000300 ?gdaScoreNumber . \n" +
                "?disease rdf:type ncit:C7057 ; \n" +
                "dcterms:identifier ?diseaseId ; \n" +
                "dcterms:title ?diseaseTitle . \n" +
                "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
                "}";
        String[] times = runQuery(DisgenetQueryStringGenerator.getPrefixes() + query, testRepeats);
        System.out.println("gda first - values second (V3): " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }

    @Test(groups = {"benchmarking"})
    public void checkSpeedValuesLast() {
        String query = "SELECT ?gene ?disease ?diseaseId ?diseaseTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
                "WHERE { \n" +
                "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
                "rdf:type ?type ; \n" +
                "sio:SIO_000216 ?gdaScore ; \n" +
                "sio:SIO_000253 ?gdaSource . \n" +
                "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                "?gdaScore rdf:type ncit:C25338 ; \n" +
                "sio:SIO_000300 ?gdaScoreNumber . \n" +
                "?disease rdf:type ncit:C7057 ; \n" +
                "dcterms:identifier ?diseaseId ; \n" +
                "dcterms:title ?diseaseTitle . \n" +
                "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
                "VALUES ?gene {<http://identifiers.org/ncbigene/1311>} \n" +
                "}";
        String[] times = runQuery(DisgenetQueryStringGenerator.getPrefixes() + query, testRepeats);
        System.out.println("values last: " + Arrays.stream(times).map(String::toString).collect(Collectors.joining(", ")));
    }
}
