package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.ResourceUri;

import java.net.URI;
import java.util.Iterator;
import java.util.Set;

/**
 * Generates SPARQL queries specific for the DisGeNET RDF dataset.
 */
public final class DisgenetQueryStringGenerator extends QueryStringGenerator {
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
     * <p>Retrieves all the source IRIs with their longest available title (MAX) and their source level (MAX, though 1 only should exist)</p>
     *
     * <p>The {@code GROUP BY} will group the results per source, where MAX(sourceTitleGr) will use the longest title. Other items need to be grouped
     * as well (even if not needed) because otherwise a "{@code Non-group key variable in SELECT}" error is thrown.</p>
     */
    private static final String SOURCES = "SELECT DISTINCT ?source ?sourceTitle ?sourceLevel \n" + // DISTINCT forces unique results only
            "WHERE { \n" +
            "?source wi:evidence ?sourceLevel . \n" +
            "{" +
            "SELECT ?source (MAX(?sourceTitleGrouped) AS ?sourceTitle) \n" + // some sources have multiple titles, MAX picks longest title only
            "WHERE { \n" +
            "?source rdf:type dctypes:Dataset ; \n" +
            "dcterms:title ?sourceTitleGrouped . \n" +
            "} \n" +
            "GROUP BY ?source \n" +
            "} \n" +
            "} \n";

    /**
     * <p>Retrieves the IRI belonging to a HPO id ({@code hp:<numbers>})</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: 1 or more HPO ids (see {@link #createHposStringFromPhenotypes(Set)}
     * <br />[3]: closing "}" belonging to [0]
     */
    private static final String[] IRI_FOR_HPO = {"SELECT DISTINCT ?hpo ?hpoId ?hpoTitle \n" + // DISTINCT forces unique results only
            "WHERE { \n", // [0] -> [1]
            "?hpo rdf:type sio:SIO_010056 ; \n" +
            "dcterms:identifier ?hpoId ; \n" +
            "dcterms:title ?hpoTitle . \n" +
            "VALUES ?hpoId ", " . \n", // [1] -> [2] -> [3]
            "}"
    };

    /**
     * <p>Retrieves the children (based on a specified range) belonging to an HPO IRI.</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: child range
     * <br />between [2] and [3]: 1 or more HPO ids (see {@link #createValuesStringForUris(Set)}
     * <br />[4]: closing "}" belonging to [0]
     */
    private static final String[] HPO_WITH_CHILDREN_FOR_HPO_IRI = {"SELECT DISTINCT ?hpoRoot ?hpo ?hpoId ?hpoTitle \n" + // DISTINCT forces unique results only
            "WHERE { \n", // [0] -> [1]
            "?hpo rdf:type sio:SIO_010056 ; \n" +
            "rdfs:subClassOf", " ?hpoRoot ; \n" + // [1] -> [2]
            "dcterms:identifier ?hpoId ; \n" +
            "dcterms:title ?hpoTitle . \n" +
            "VALUES ?hpoRoot ", " \n", // [2] -> [3] -> [4]
            "} \n"
    };

//    /**
//     * <p>Retrieves the children (based on a specified range) belonging to an HPO.</p>
//     *
//     * [0]: "SELECT ... WHERE { "
//     * <br />between [1] and [2]: child range
//     * <br />between [2] and [3]: 1 or more HPO ids (see {@link #createHposStringFromPhenotypes(Set)}
//     * <br />[4]: closing "}" belonging to [0]
//     */
//    private static final String[] HPO_WITH_CHILDREN_FOR_HPO = {"SELECT DISTINCT ?hpoParent ?hpo ?hpoId ?hpoTitle \n" + // DISTINCT forces unique results only
//            "WHERE { \n", // [0] -> [1]
//            "?hpo rdf:type sio:SIO_010056 ; \n" +
//            "rdfs:subClassOf", " ?hpoParent ; \n" + // [1] -> [2]
//            "dcterms:identifier ?hpoId ; \n" +
//            "dcterms:title ?hpoTitle . \n" +
//            "{ \n" +
//            "SELECT DISTINCT (?hpo as ?hpoParent) \n" + // subquery for retrieving URI(s) based on HPO id(s)
//            "WHERE { \n" +
//            IRI_FOR_HPO[1], IRI_FOR_HPO[2] + // [2] -> [3]
//            "} \n" +
//            "} \n", // [3] -> [4]
//            "} \n"
//    };

    /**
     * <p>Retrieves the phenotype-disease associations given based on a VALUES list containing the HPO terms.</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: the HPO terms to filter on (see {@link #createValuesStringForUris(Set)}
     * <br />[3]: closing "}" belonging to [0]
     */
    private static final String[] PDA_FOR_MULTIPLE_HPO = {"SELECT ?hpo ?disease ?diseaseId ?diseaseTitle ?pdaSource \n" +
            "WHERE { \n", // [0] -> [1]
            "?hpo rdf:type sio:SIO_010056 . \n" +
            "VALUES ?hpo ", " \n" + // [1] -> [2]
            "?pda rdf:type sio:SIO_000897 ; \n" +
            "sio:SIO_000628 ?hpo , ?disease ; \n" +
            "sio:SIO_000253 ?pdaSource . \n" +
            "?disease rdf:type ncit:C7057 ; \n" +
            "dcterms:identifier ?diseaseId ; \n" +
            "dcterms:title ?diseaseTitle . \n", // [2] -> [3]
            "}"
    };

    /**
     * <p>Retrieves the phenotype-disease associations given based on a VALUES list containing the HPO terms.</p>
     *
     * [0]: "SELECT ... WHERE { "
     * <br />between [1] and [2]: the association type (see {@link DisgenetAssociationType})
     * <br />between [2] and [3]: 1 or more disease URIs
     * <br />[4]: closing "}" belonging to [0]
     */
    private static final String[] GDA_FOR_DISEASES = {"SELECT ?disease ?gene ?geneId ?geneTitle ?geneSymbolTitle ?gdaScoreNumber ?gdaSource ?evidence \n" +
            "WHERE { \n", // [0] -> [1]
            "?gda sio:SIO_000628 ?disease, ?gene ; \n" +
            "rdf:type ?type ; \n" +
            "sio:SIO_000216 ?gdaScore ; \n" +
            "sio:SIO_000253 ?gdaSource . \n" +
            "?type rdfs:subClassOf* ", " . \n" + // [1] -> [2]
            "?gene rdf:type ncit:C16612 ; \n" +
            "dcterms:identifier ?geneId ; \n" +
            "dcterms:title ?geneTitle ; \n" +
            "sio:SIO_000205 ?geneSymbol . \n" +
            "?geneSymbol rdf:type ncit:C43568 ; \n" +
            "dcterms:title ?geneSymbolTitle . \n" +
            "?gdaScore rdf:type ncit:C25338 ; \n" +
            "sio:SIO_000300 ?gdaScoreNumber . \n" +
            "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
            "VALUES ?disease ", " \n", // [2] -> [3] -> [4]
            "}"
    };

    public static String getPrefixes() {
        return PREFIXES;
    }

    public static QueryString getSources() {
        return new QueryString(PREFIXES + SOURCES);
    }

    /**
     * Generates a {@link QueryString} for the retrieval of an {@link URI} belonging to 1 or more {@link Phenotype}{@code s}.
     * @param phenotypes the {@link Phenotype}{@code s} to retrieve an {@link URI} for (these {@link Phenotype}{@code s}
     *                   can be created using the constructor that only stores an ID)
     * @return a {@link String} to be used for running the query
     */
    public static QueryString getIriForHpo(Set<Phenotype> phenotypes) {
        return new QueryString(PREFIXES + IRI_FOR_HPO[0] + IRI_FOR_HPO[1] + createHposStringFromPhenotypes(phenotypes) +
                IRI_FOR_HPO[2] + IRI_FOR_HPO[3]);
    }

    /**
     * Retrieves the children for 1 or more {@link Phenotype}{@code s}.
     * @param phenotypes the {@link Phenotype}{@code s} to retrieve children for
     * @param range the range to be used for child retrieval
     * @return a {@link String} to be used for running the query
     */
    public static QueryString getHpoChildren(Set<Phenotype> phenotypes, QueryStringPathRange range) {
        return new QueryString(PREFIXES + HPO_WITH_CHILDREN_FOR_HPO_IRI[0] + HPO_WITH_CHILDREN_FOR_HPO_IRI[1] + range.getRangeString() +
                HPO_WITH_CHILDREN_FOR_HPO_IRI[2] + createValuesStringForUris(phenotypes) + HPO_WITH_CHILDREN_FOR_HPO_IRI[3] +
                HPO_WITH_CHILDREN_FOR_HPO_IRI[4], range.getSyntax());
    }

    /**
     * Retrieves the phenotype-disease associations for given {@link Phenotype}{@code s}.
     * @param phenotypes the {@link Phenotype}{@code s} to retrieve phenotype-disease associations for
     * @return a {@link String} to be used for running the query
     */
    public static QueryString getPdas(Set<Phenotype> phenotypes) {
        return new QueryString(PREFIXES + PDA_FOR_MULTIPLE_HPO[0] + PDA_FOR_MULTIPLE_HPO[1] +
                createValuesStringForUris(phenotypes) + PDA_FOR_MULTIPLE_HPO[2] + PDA_FOR_MULTIPLE_HPO[3]);
    }

    /**
     * Retireves the gene-disease associations for the given diseases.
     * @param diseases the {@link Disease}{@code s} to retrieve gene-disease associations for
     * @param disgenetAssociationType the root to be used for the gene-disease association types
     * @return a {@link String} to be used for running the query
     */
    public static QueryString getGdas(Set<Disease> diseases, DisgenetAssociationType disgenetAssociationType) {
        return new QueryString(PREFIXES + GDA_FOR_DISEASES[0] + GDA_FOR_DISEASES[1] + disgenetAssociationType.getFormattedId() +
                GDA_FOR_DISEASES[2] + createValuesStringForUris(diseases) + GDA_FOR_DISEASES[3] + GDA_FOR_DISEASES[4]);
    }

    /**
     * Generates query-compatible {@link String} to be used as VALUES containing 1 or more {@link URI}{@code s}.
     * @param resourceUris the {@link URI}{@code s} to be used
     * @return a SPARQL VALUES usable {@link String}
     */
    private static String createValuesStringForUris(Set<? extends ResourceUri> resourceUris) {
        if(resourceUris.size() < 1) {
            throw new IllegalArgumentException("Set should at least contain 1 item.");
        }
        Iterator<? extends ResourceUri> resourceUriIterator = resourceUris.iterator();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{ <").append(resourceUriIterator.next().getUri());

        while (resourceUriIterator.hasNext()) {
            URI uri = resourceUriIterator.next().getUri();
            if(uri == null) {
                throw new IllegalArgumentException("Not all Objects have a valid URI (an uri was null).");
            }
            strBuilder.append("> <").append(uri);
        }
        return strBuilder.append("> }").toString();
    }

    /**
     * Generates query-compatible {@link String} to be used as VALUES containing 1 or more {@link Phenotype} {@code IDs}.
     * @param phenotypes the {@link Phenotype}{@code s} to be used
     * @return a SPARQL VALUES usable {@link String}
     */
    private static String createHposStringFromPhenotypes(Set<Phenotype> phenotypes) {
        if(phenotypes.size() < 1) {
            throw new IllegalArgumentException("Set should at least contain 1 item.");
        }
        Iterator<Phenotype> resourceUriIterator = phenotypes.iterator();

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{ \"").append(resourceUriIterator.next().getFormattedId());

        while (resourceUriIterator.hasNext()) {
            strBuilder.append("\"^^xsd:string \"").append(resourceUriIterator.next().getFormattedId());
        }
        return strBuilder.append("\"^^xsd:string }").toString();
    }
}
