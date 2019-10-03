package org.molgenis.vibe.rdf_processing;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetQueryStringGenerator;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;

import java.net.URI;
import java.util.*;

/**
 * Retrieves all required information for further processing regarding the genes belonging to a given phenotype.
 */
public class GenesForPhenotypeRetriever extends DisgenetRdfDataRetriever {
    /**
     * The {@link Phenotype}{@code s} to be processed.
     */
    private Set<Phenotype> phenotypes;

    /**
     * {@link Disease}{@code s} storage for easy retrieval.
     */
    private Map<URI, Disease> diseasesByUri = new HashMap<>();

    /**
     * The final output to be retrieved for further usage after querying.
     */
    private GeneDiseaseCollection geneDiseaseCollection = new GeneDiseaseCollection();

    public GeneDiseaseCollection getGeneDiseaseCollection() {
        return geneDiseaseCollection;
    }

    public GenesForPhenotypeRetriever(ModelReader modelReader, Set<Phenotype> phenotypes) {
        super(modelReader);
        this.phenotypes = requireNonNull(phenotypes);
    }

    @Override
    public void run() {
        retrieveSources();
        retrieveData();
    }

    private void retrieveData() {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getGenesForPhenotypes(phenotypes));

        // Create initial variables before while loop.
        URI prevGeneUri = null;
        URI prevDiseaseUri = null;
        Gene gene = null;
        GeneDiseaseCombination gdc = null;

        while(query.hasNext()) {
            QuerySolution result = query.next();

            // Check if new gene is being processed and creates new Gene object if this is the case.
            // Approach requires SPARQL query to be ordered by gene first!
            URI geneUri = URI.create(result.get("gene").asResource().getURI());
            if(geneUri != prevGeneUri) {
                gene = new Gene(result.get("geneId").asLiteral().getString(),
                        result.get("geneTitle").asLiteral().getString(),
                        result.get("geneSymbolTitle").asLiteral().getString(),
                        result.get("dsiValue").asLiteral().getDouble(),
                        result.get("dpiValue").asLiteral().getDouble(),
                        geneUri);

                prevGeneUri = geneUri;
            }

            // Checks if new disease is being processed. If not, skips certain unnecessary steps.
            // Approach requires SPARQL query to be ordered by disease second (and gene first)!
            URI diseaseUri = URI.create(result.get("disease").asResource().getURI());
            if(diseaseUri != prevDiseaseUri) {
                // Check if disease is already stored, and if not, stores it (using URI as key).
                Disease disease = diseasesByUri.get(diseaseUri);

                if(disease == null) {
                    disease = new Disease(result.get("diseaseId").asLiteral().getString(),
                            result.get("diseaseTitle").asLiteral().getString(),
                            diseaseUri);

                    diseasesByUri.put(diseaseUri, disease);
                }

                // Retrieves score belonging to the gene-disease combination.
                double score = result.get("gdaScoreNumber").asLiteral().getDouble();

                // The gene-disease combination belonging to the single query result.
                GeneDiseaseCombination comparisonGdc = new GeneDiseaseCombination(gene, disease, score);

                // Retrieves it from the collection (if it already exists).
                gdc = geneDiseaseCollection.get(comparisonGdc);

                // If the gene-disease combination is not present yet, uses the comparison gdc and also adds it to the collection.
                if(gdc == null) {
                    gdc = comparisonGdc;
                    geneDiseaseCollection.add(gdc);
                }

                prevDiseaseUri = diseaseUri;
            }

            // Retrieves source belonging to match. If this causes an error, this might indicate a corrupt database (as
            // retrieveSources() should retrieve all possible sources available).
            Source source = getSources().get(URI.create(result.get("gdaSource").asResource().getURI()));

            // Adds source to gene-disease combination (with evidence if available).
            if(result.get("evidence") != null) {
                gdc.add(source, URI.create(result.get("evidence").asResource().getURI()));
            } else {
                gdc.add(source);
            }
        }

        query.close();
    }
}
