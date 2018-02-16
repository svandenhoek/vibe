package org.molgenis.vibe.rdf_processing.querying;

import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.formats.ResourceUri;

import java.util.Iterator;
import java.util.Set;

/**
 * Generates SPARQL queries specific for the DisGeNET RDF dataset.
 */
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

    /**
     * <p>Retrieves the IRI belonging to a HPO id ({@code hp:<numbers>})</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: 1 or more HPO ids (see {@link #createValuesStringFromHpoIds(Set)}
     * <br />[3]: closing "}" belonging to [0]
     */
    private static final String[] IRI_FOR_HPO = {"SELECT DISTINCT ?hpo ?hpoId \n" + // DISTINCT forces unique results only
            "WHERE { \n", // [0] -> [1]
            "?hpo rdf:type sio:SIO_010056 ; \n" +
            "dcterms:identifier ?hpoId . \n" +
            "VALUES ?hpoId ", " . \n", // [1] -> [2] -> [3]
            "}"
    };

    /**
     * <p>Retrieves the children (based on a specified range) belonging to an HPO IRI.</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: child range
     * <br />between [2] and [3]: 1 or more HPO ids (see {@link #createValuesStringFromHpoIds(Set)}
     * <br />[4]: closing "}" belonging to [0]
     */
    private static final String[] HPO_CHILDREN_FOR_IRI = {"SELECT DISTINCT ?hpoParent ?hpo ?hpoId \n" + // DISTINCT forces unique results only
            "WHERE { \n", // [0] -> [1]
            "?hpo rdf:type sio:SIO_010056 ; \n" +
            "rdfs:subClassOf", " ?hpoParent ; \n" + // [1] -> [2]
            "dcterms:identifier ?hpoId . \n" +
            "{ \n" +
            "SELECT DISTINCT (?hpo as ?hpoParent) \n" + // subquery for retrieving URI(s) based on HPO id(s)
            "WHERE { \n" +
            IRI_FOR_HPO[1], IRI_FOR_HPO[2] + // [2] -> [3]
            "} \n" +
            "} \n", // [3] -> [4]
            "} \n"
    };

    /**
     * <p>Retrieves the phenotype-disease associations given based on a VALUES list containing the HPO terms.</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: the HPO terms to filter on (see {@link #createValuesStringForUris(Set)}
     * <br />[3]: closing "}" belonging to [0]
     */
    private static final String[] PDA_FOR_MULTIPLE_HPO = {"SELECT ?hpo ?disease ?diseaseTitle ?pdaSource \n" +
            "WHERE { \n", // [0] -> [1]
            "?hpo rdf:type sio:SIO_010056 . \n" +
            "VALUES ?hpo ", " \n" + // [1] -> [2]
            "?pda rdf:type sio:SIO_000897 ; \n" +
            "sio:SIO_000628 ?hpo , ?disease ; \n" +
            "sio:SIO_000253 ?pdaSource . \n" +
            "?disease rdf:type ncit:C7057 ; \n" +
            "dcterms:title ?diseaseTitle . \n", // [2] -> [3]
            "}"
    };

    //todo: Finish query.
    private static final String[] GDA_FOR_DISEASES = {"SELECT ?disease ?gene ?geneId ?geneSymbolTitle ?gdaScoreNumber ?gdaSource ?evidence \n", // SELECT is [0]
            "WHERE { ?gda sio:SIO_000628 ?disease, ?gene ; \n" + // [1], SIO_000628 -> refers to
            "rdf:type ?type ; \n" +
            "sio:SIO_000216 ?gdaScore ; \n" + // SIO_000216 -> has measurement value
            "sio:SIO_000253 ?gdaSource . \n" + // SIO_000253 -> has source
            "?type rdfs:subClassOf* ", " . \n" + // association type between [1] and [2] (use default: SIO_000983 -> gene-disease association)
            "?gene rdf:type ncit:C16612 ; \n" + // ncit:C16612 -> Gene
            "dcterms:identifier ?geneId ; \n" +
            "sio:SIO_000205 ?geneSymbol . \n" + // SIO_000205 -> is represented by
            "?geneSymbol rdf:type ncit:C43568 ; \n" + // ncit:C43568 -> Gene Symbol
            "dcterms:title ?geneSymbolTitle . \n" +
            "?gdaScore rdf:type ncit:C25338 ; \n" + // ncit:C25338 -> Score
            "sio:SIO_000300 ?gdaScoreNumber . \n" + // SIO_000300 -> has value
            "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" + // SIO_000772 -> has evidence
            "VALUES ?disease ", " \n" + // list of diseases between [2] and [3]"
            "}"
    };

    private static final String[] HPO_GENES = {"SELECT ?diseaseTitle ?geneId ?geneSymbolTitle ?gdaScoreNumber ?pdaSource ?gdaSource ?gdaSourceLevelLabel ?pubmed \n" +
            "WHERE { ?hpo rdf:type sio:SIO_010056 ; \n" + // SIO_010056 -> phenotype
            "dcterms:identifier \"", "\"^^xsd:string . \n" + // HPO term is inserted here!!!
            "?pda rdf:type sio:SIO_000897 ; \n" + // SIO_000897 -> association
            "sio:SIO_000628 ?hpo , ?disease ; \n" + // SIO_000628 -> refers to
            "sio:SIO_000253 ?pdaSource . \n" + // SIO_000253 -> has source
            "?disease rdf:type ncit:C7057 ; \n" + // ncit:C7057 -> Disease
            "dcterms:title ?diseaseTitle . \n" +
            "?pdaSource rdf:type dctypes:Dataset . \n" + // dcterms:title sometimes has multiple hits
            "?gda sio:SIO_000628 ?disease, ?gene ; \n" + // SIO_000628 -> refers to
            "rdf:type ?type ; \n" +
            "sio:SIO_000216 ?gdaScore ; \n" + // SIO_000216 -> has measurement value
            "sio:SIO_000253 ?gdaSource . \n" + // SIO_000253 -> has source
            "?type rdfs:subClassOf* sio:SIO_000983 . \n" + // SIO_000983 -> gene-disease association
            "?gene rdf:type ncit:C16612 ; \n" + // ncit:C16612 -> Gene
            "dcterms:identifier ?geneId ; \n" +
            "sio:SIO_000205 ?geneSymbol . \n" + // SIO_000205 -> is represented by
            "?geneSymbol rdf:type ncit:C43568 ; \n" + // ncit:C43568 -> Gene Symbol
            "dcterms:title ?geneSymbolTitle . \n" +
            "?gdaScore rdf:type ncit:C25338 ; \n" + // ncit:C25338 -> Score
            "sio:SIO_000300 ?gdaScoreNumber . \n" + // SIO_000300 -> has value
            "?gdaSource rdf:type dctypes:Dataset ; \n" + // dcterms:title sometimes has multiple hits
            "wi:evidence ?gdaSourceLevel . \n" +
            "?gdaSourceLevel rdfs:label ?gdaSourceLevelLabel . \n" +
            "OPTIONAL { ?gda sio:SIO_000772 ?pubmed }" + // SIO_000772 -> has evidence
            "}"
    };

    public static String getPrefixes() {
        return PREFIXES;
    }

    public static String getIriForHpo(Set<Hpo> hpos) {
        return PREFIXES + IRI_FOR_HPO[0] + IRI_FOR_HPO[1] + createValuesStringFromHpoIds(hpos) + IRI_FOR_HPO[2] + IRI_FOR_HPO[3];
    }

    public static String getHpoChildren(Set<Hpo> hpos, SparqlRange range) {
        return PREFIXES + HPO_CHILDREN_FOR_IRI[0] + HPO_CHILDREN_FOR_IRI[1] + range.toString() + HPO_CHILDREN_FOR_IRI[2] +
                createValuesStringFromHpoIds(hpos) + HPO_CHILDREN_FOR_IRI[3] + HPO_CHILDREN_FOR_IRI[4];
    }

    public static String getPdas(Set<Hpo> hpo) {
        return PREFIXES + PDA_FOR_MULTIPLE_HPO[0] + PDA_FOR_MULTIPLE_HPO[1] + createValuesStringForUris(hpo) +
                PDA_FOR_MULTIPLE_HPO[2] + PDA_FOR_MULTIPLE_HPO[3];
    }

    public static String getGdas(Set<Disease> diseases, DisgenetAssociationType disgenetAssociationType) {
        return PREFIXES + GDA_FOR_DISEASES[0] + GDA_FOR_DISEASES[1] + disgenetAssociationType.getFormattedId() +
                GDA_FOR_DISEASES[2] + createValuesStringForUris(diseases) + GDA_FOR_DISEASES[3];
    }

//    public static String getPhenotypeDiseaseAssociations(String hpoTerm) {
//        return PREFIXES + RETRIEVE_PDA_FOR_HPO[0] + RETRIEVE_PDA_FOR_HPO[1];
//    }

    public static String getHpoGenes(String hpoTerm) {
        return PREFIXES + HPO_GENES[0] + hpoTerm + HPO_GENES[1];
    }

    public static String getHpoGenes(String hpoTerm, int limit) {
        return addLimit(PREFIXES + HPO_GENES[0] + hpoTerm + HPO_GENES[1], limit);
    }

    private static String createValuesStringForUris(Set<? extends ResourceUri> resourceUris) {
        if(resourceUris.size() < 1) {
            throw new IllegalArgumentException("Set should at least contain 1 item.");
        }
        Iterator<? extends ResourceUri> resourceUriIterator = resourceUris.iterator();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{ <").append(resourceUriIterator.next().getUri());

        while (resourceUriIterator.hasNext()) {
            strBuilder.append("> <").append(resourceUriIterator.next().getUri());
        }
        return strBuilder.append("> }").toString();
    }

    private static String createValuesStringFromHpoIds(Set<Hpo> hpos) {
        if(hpos.size() < 1) {
            throw new IllegalArgumentException("Set should at least contain 1 item.");
        }
        Iterator<Hpo> resourceUriIterator = hpos.iterator();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{ \"").append(resourceUriIterator.next().getFormattedId());

        while (resourceUriIterator.hasNext()) {
            strBuilder.append("\"^^xsd:string \"").append(resourceUriIterator.next().getFormattedId());
        }
        return strBuilder.append("\"^^xsd:string }").toString();
    }
}
