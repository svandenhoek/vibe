package org.molgenis.vibe.rdf_querying;

public final class DisgenetQueryGenerator extends SparqlQueryGenerator {
    // See: http://www.disgenet.org/web/DisGeNET/menu/rdf#sparql-queries -> DisGeNET NAMESPACES
    // Some namespaces contained 1 or more additional "http://". These were removed.
    // 18 lines (for debugging SPARQL queries)
    private static final String PREFIXES = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"+
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
            "PREFIX wi: <http://purl.org/ontology/wi/core#> \n"+
            "PREFIX eco: <http://purl.obolibrary.org/obo/eco.owl#> \n"+
            "PREFIX prov: <http://www.w3.org/ns/prov#> \n"+
            "PREFIX pav: <http://purl.org/pav/> \n"+
            "PREFIX obo: <http://purl.obolibrary.org/obo/> \n";

    // Returns: gene-disease disgenet ID, gene name, disease name
    private static final String GDA_GENE_DISEASE = "SELECT ?gda ?type ?geneTitle ?diseaseTitle \n" +
            "WHERE { ?type rdfs:subClassOf* sio:SIO_000983 . \n" +
            "?gda rdf:type ?type ; \n" +
            "sio:SIO_000628 ?gene , ?disease . \n" +
            "?gene rdf:type ncit:C16612 ; \n" +
            "dcterms:title ?geneTitle ." +
            "?disease rdf:type ncit:C7057 ; \n" +
            "dcterms:title ?diseaseTitle }";

    private static final String[] HPO_GENES = {"SELECT ?diseaseTitle ?geneId ?geneTitle ?gdaSourceTitle \n" +
            "WHERE { ?hpo rdf:type sio:SIO_010056 ; \n" +
            "dcterms:identifier \"", "\"^^xsd:string . \n" +
            "?pda rdf:type sio:SIO_000897 ; \n" +
            "sio:SIO_000628 ?hpo , ?disease . \n" +
            "?disease rdf:type ncit:C7057 ; \n" +
            "dcterms:title ?diseaseTitle . \n" +
            "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
            "rdf:type ?type ; \n" +
            "sio:SIO_000253 ?gdaSource . \n" +
            "?gdaSource rdf:type dctypes:Dataset ;" +
            "dcterms:title ?gdaSourceTitle ." +
            "?type rdfs:subClassOf* sio:SIO_000983 . \n" +
            "?gene rdf:type ncit:C16612 ; \n" +
            "dcterms:identifier ?geneId ; \n" +
            "dcterms:title ?geneTitle . }"};

    public static String getPrefixes() {
        return PREFIXES;
    }

    public static String getGdaGeneDisease() {
        return PREFIXES + GDA_GENE_DISEASE;
    }

    public static String getGdaGeneDisease(int limit) {
        return addLimit(PREFIXES + GDA_GENE_DISEASE, limit);
    }

    public static String getHpoGenes(String hpoTerm) {
        return PREFIXES + HPO_GENES[0] + hpoTerm + HPO_GENES[1];
    }

    public static String getHpoGenes(String hpoTerm, int limit) {
        return addLimit(PREFIXES + HPO_GENES[0] + hpoTerm + HPO_GENES[1], limit);
    }
}
