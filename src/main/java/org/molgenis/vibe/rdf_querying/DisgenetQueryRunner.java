package org.molgenis.vibe.rdf_querying;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public final class DisgenetQueryRunner extends SparqlQueryRunner {
    // See: http://www.disgenet.org/web/DisGeNET/menu/rdf#sparql-queries -> DisGeNET NAMESPACES
    // 18 lines (for debugging SPARQL queries)
    private static final String prefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"+
            "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"+
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"+
            "PREFIX dcterms: <http://purl.org/dc/terms/> \n"+
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"+
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n"+
            "PREFIX void: <http://rdfs.org/ns/void#> \n"+
            "PREFIX sio: <http://semanticscience.org/resource/>\n"+
            "PREFIX ncit: <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#> \n"+
            "PREFIX up: <http://purl.uniprot.org/core/> \n"+
            "PREFIX dcat: <http://www.w3.org/ns/dcat#> \n"+
            "PREFIX dctypes: <http://purl.org/dc/dcmitype/> \n"+
            "PREFIX wi: <http://http://purl.org/ontology/wi/core#> \n"+
            "PREFIX eco: <http://http://purl.obolibrary.org/obo/eco.owl#> \n"+
            "PREFIX prov: <http://http://http://www.w3.org/ns/prov#> \n"+
            "PREFIX pav: <http://http://http://purl.org/pav/> \n"+
            "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";

    // Returns: gene-disease disgenet ID, gene name, disease name
    private static final String gdaGeneDisease = "SELECT ?gda ?type ?geneTitle ?diseaseTitle \n" +
            "WHERE { ?type rdfs:subClassOf* sio:SIO_000983 . \n" +
            "?gda rdf:type ?type ; \n" +
            "sio:SIO_000628 ?gene , ?disease . \n" +
            "?gene rdf:type ncit:C16612 ; \n" +
            "dcterms:title ?geneTitle ." +
            "?disease rdf:type ncit:C7057 ; \n" +
            "dcterms:title ?diseaseTitle }";

//        private static final String[]hpoGenes = {"SELECT ?gda ?sio ?geneTitle ?disease \n" +
//            "WHERE { ?sio rdfs:subClassOf* sio:SIO_000983 . \n" +
//                "?gda rdf:type ?sio ; \n" +
//                "sio:SIO_000628 ?gene , ?disease . \n" +
//                "?gene rdf:type ncit:C16612 ; \n" +
//                "dcterms:title ?geneTitle . \n" +
//                "?disease rdf:type ncit:C7057 }",};

//            private static final String[]hpoGenes = {"SELECT ?pda ?hpoId ?disease \n" +
//            "WHERE { ?pda rdf:type sio:SIO_000897 ; \n" +
//                    "sio:SIO_000628 ?hpo, ?disease ." +
//                    "?hpo rdf:type sio:SIO_010056 ;" +
//                    "dcterms:identifier ?hpoId ." +
//                    "?disease rdf:type ncit:C7057 }",};

            private static final String[]hpoGenes = {"SELECT ?gda ?type ?pda ?geneTitle ?hpoId \n" +
                    "WHERE { ?type rdfs:subClassOf* sio:SIO_000983 . \n" +
                    "?gda rdf:type ?type ; \n" +
                    "sio:SIO_000628 ?gene , ?disease . \n" +
                    "?gene rdf:type ncit:C16612 ; \n" +
                    "dcterms:title ?geneTitle . \n" +
                    "?disease rdf:type ncit:C7057 . \n" +
                    "?pda rdf:type sio:SIO_000897 ;" +
                    "sio:SIO_000628 ?hpo , ?disease ." +
                    "?hpo rdf:type sio:SIO_010056 ;" +
                    "dcterms:identifier ?hpoId }",};

    public DisgenetQueryRunner(Model model) {
        super(model);
    }

    public static String getPrefixes() {
        return prefixes;
    }

    public ResultSet getGdaGeneDisease() {
        return runQuery(prefixes + gdaGeneDisease);
    }

    public ResultSet getGdaGeneDisease(int limit) {
        return runQuery(addLimit(prefixes + gdaGeneDisease, limit));
    }

    public ResultSet getHpoGenes(String hpoTerm) {
        return runQuery(prefixes + hpoGenes[0]);// + hpoTerm + hpoGenes[1]);
    }

//    public ResultSet getHpoGenes(String hpoTerm, int limit) {
//        return runQuery(addLimit(prefixes + hpoGenes[0] + hpoTerm + hpoGenes[1], limit));
//    }
}
