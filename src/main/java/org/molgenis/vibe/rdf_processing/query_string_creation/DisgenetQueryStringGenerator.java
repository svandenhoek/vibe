package org.molgenis.vibe.rdf_processing.query_string_creation;

import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.Phenotype;
import org.molgenis.vibe.formats.ResourceUri;

import java.net.URI;
import java.util.Iterator;
import java.util.Set;

/**
 * Generates SPARQL queries specific for the DisGeNET RDF dataset.
 */
public final class DisgenetQueryStringGenerator extends QueryStringGenerator {
    /**
     * Prefixes for querying. See "DisGeNET NAMESPACES" on <a href=http://www.disgenet.org/web/DisGeNET/menu/rdf#sparql-queries>http://www.disgenet.org/web/DisGeNET/menu/rdf#sparql-queries</a>
     * <br />Some namespaces contained 1 or more additional "http://". These were removed.
     * <br />Lines: 18 (for debugging SPARQL queries)
     */
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
            "?source rdf:type dctypes:Dataset , dcat:Distribution ; \n" +
            "dcterms:title ?sourceTitleGrouped . \n" +
            "} \n" +
            "GROUP BY ?source \n" +
            "} \n" +
            "} \n";

    /**
     * <p>Retrieves the genes belonging to certain HPO phenotypes.</p>
     *
     * <br />between [0] and [1]: the HPO terms (URIs) to filter on (see {@link #createValuesStringForUris(Set)}
     * <br />between [1] and [2]: the gene-disease association type (see {@link DisgenetAssociationType})
     */
    private static final String[] GENES_FOR_PHENOTYPES = { "SELECT ?gene ?geneId ?geneTitle ?geneSymbolTitle ?disease ?diseaseId ?diseaseTitle ?dsiValue ?dpiValue ?gdaScoreNumber ?gdaSource ?evidence \n" +
            "WHERE { \n" +
            "VALUES ?hpo ", " \n" + // [0] -> [1]
            "?disease rdf:type ncit:C7057 ; \n" +
            "skos:exactMatch ?hpo ; \n" +
            "dcterms:identifier ?diseaseId ; \n" +
            "dcterms:title ?diseaseTitle . \n" +
            "?gda sio:SIO_000628 ?disease , ?gene ; \n" +
            "rdf:type ?type ; \n" +
            "sio:SIO_000216 ?gdaScore ; \n" +
            "sio:SIO_000253 ?gdaSource . \n" +
            "?type rdfs:subClassOf* ", " . \n" + // [1] -> [2]
            "?gene rdf:type ncit:C16612 ; \n" +
            "dcterms:identifier ?geneId ; \n" +
            "dcterms:title ?geneTitle ; \n" +
            "sio:SIO_000205 ?geneSymbol ; \n" +
            "sio:SIO_000216 ?dsi, ?dpi . \n" +
            "?geneSymbol rdf:type ncit:C43568 ; \n" +
            "dcterms:title ?geneSymbolTitle . \n" +
            "?gdaScore rdf:type ncit:C25338 ; \n" +
            "sio:SIO_000300 ?gdaScoreNumber . \n" +
            "?dsi rdf:type sio:SIO_001351 ; \n" +
            "sio:SIO_000300 ?dsiValue . \n" +
            "?dpi rdf:type sio:SIO_001352 ; \n" +
            "sio:SIO_000300 ?dpiValue . \n" +
            "OPTIONAL { ?gda sio:SIO_000772 ?evidence } \n" +
            "}"
    };

    public static String getPrefixes() {
        return PREFIXES;
    }

    public static QueryString getSources() {
        return new QueryString(PREFIXES + SOURCES);
    }

    public static QueryString getGenesForPhenotypes(Set<Phenotype> phenotypes) {
        return new QueryString(PREFIXES + GENES_FOR_PHENOTYPES[0] + createValuesStringForUris(phenotypes) + GENES_FOR_PHENOTYPES[1] +
        DisgenetAssociationType.GENE_DISEASE.getFormattedId() + GENES_FOR_PHENOTYPES[2]);
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
}
