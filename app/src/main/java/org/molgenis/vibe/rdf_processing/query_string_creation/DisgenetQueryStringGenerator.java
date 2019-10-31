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
     * Default DisGeNET prefixes. See "DisGeNET NAMESPACES" on <a href=http://www.disgenet.org/web/DisGeNET/menu/rdf#sparql-queries>http://www.disgenet.org/web/DisGeNET/menu/rdf#sparql-queries</a>
     * <br />Some namespaces contained 1 or more additional "http://". These were removed.
     */
    private static final String PREFIXES_DISGENET = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
            "PREFIX dcterms: <http://purl.org/dc/terms/>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
            "PREFIX void: <http://rdfs.org/ns/void#>\n" +
            "PREFIX sio: <http://semanticscience.org/resource/>\n" +
            "PREFIX so: <http://www.sequenceontology.org/miso/current_svn/term/SO:>\n" +
            "PREFIX ncit: <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#>\n" +
            "PREFIX up: <http://purl.uniprot.org/core/>\n" +
            "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n" +
            "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n" +
            "PREFIX wi: <http://purl.org/ontology/wi/core#>\n" +
            "PREFIX eco: <http://purl.obolibrary.org/obo/eco.owl#>\n" +
            "PREFIX prov: <http://www.w3.org/ns/prov#>\n" +
            "PREFIX pav: <http://purl.org/pav/>\n" +
            "PREFIX obo: <http://purl.obolibrary.org/obo/>\n" ;

    /**
     * Custom prefixes (as also used in custom TDB).
     */
    private static final String PREFIXES_CUSTOM = "PREFIX umls: <http://linkedlifedata.com/resource/umls/id/> # DisGeNET\n" +
            "PREFIX ncbigene: <http://identifiers.org/ncbigene/> # DisGeNET\n" +
            "PREFIX pda: <http://rdf.disgenet.org/resource/pda/> # DisGeNET\n" +
            "PREFIX gda: <http://rdf.disgenet.org/resource/gda/> # DisGeNET\n" +
            "PREFIX ordo: <http://www.orpha.net/ORDO/> # DisGeNET / Orphanet\n" +
            "PREFIX hoom: <http://www.semanticweb.org/ontology/HOOM#> # Orphanet\n" +
            "PREFIX void5: <http://rdf.disgenet.org/v5.0.0/void/> # DisGeNET\n" +
            "PREFIX void6: <http://rdf.disgenet.org/v6.0.0/void/> # DisGeNET\n";

    /**
     * Full collection of all prefixes.
     */
    private static final String PREFIXES = PREFIXES_DISGENET + PREFIXES_CUSTOM;

    /**
     * <p>Retrieves all the source IRIs with their longest available title (MAX) and their source level (MAX, though 1 only should exist)</p>
     *
     * <p>The {@code GROUP BY} will group the results per source, where MAX(sourceTitleGr) will use the longest title. Other items need to be grouped
     * as well (even if not needed) because otherwise a "{@code Non-group key variable in SELECT}" error is thrown.</p>
     */
    private static final String SOURCES = "SELECT ?source ?sourceTitle ?sourceLevel \n" +
            "WHERE {\n" +
            "\t?source rdf:type dctypes:Dataset , dcat:Distribution ;\n" +
            "\twi:evidence ?sourceLevel ;\n" +
            "\tdcterms:title ?sourceTitle .\n" +
            "}";

    /**
     * <p>Retrieves the genes belonging to certain HPO phenotypes.</p>
     *
     * <br />between [0] and [1]: the HPO terms (URIs) to filter on (see {@link #createValuesStringForUris(Set)}
     * <br />between [1] and [2]: the gene-disease association type (see {@link DisgenetAssociationType})
     */
    private static final String[] GENES_FOR_PHENOTYPES = {"SELECT ?hpo ?disease ?gene ?gdaScoreNumber ?gdaSource ?evidence\n" +
            "WHERE {\n" +
            "\tVALUES ?hpo ", "\n" + // [0] -> [1]
            "\t{\n" +
            "\t\t# Diseases that are UMLS phenotypes.\n" +
            "\t\t?hpo skos:exactMatch ?disease .\n" +
            "\t}\n" +
            "\tUNION\n" +
            "\t{\n" +
            "\t\t# Diseases found through phenotype-disease associations.\n" +
            "\t\t?hpo sio:SIO_000212/sio:SIO_000628 ?disease .\n" +
            "\t}\n" +
            "\tUNION\n" +
            "\t{\n" +
            "\t\t# Diseases found through Orphanet (HPO - ORDO Ontological Module).\n" +
            "\t\t?hpo sio:SIO_000001/skos:exactMatch ?disease .\n" +
            "\t}\n" +
            "\n" +
            "\t?disease sio:SIO_000212 ?gda .\n" +
            "\t\n" +
            "\t?gda rdf:type/rdfs:subClassOf* ", " ;\n" + // [1] -> [2]
            "\tsio:SIO_000628 ?gene ;\n" +
            "\tsio:SIO_000216 ?gdaScoreNumber ;\n" +
            "\tsio:SIO_000253 ?gdaSource .\n" +
            "\tOPTIONAL { ?gda sio:SIO_000772 ?evidence }\n" +
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
