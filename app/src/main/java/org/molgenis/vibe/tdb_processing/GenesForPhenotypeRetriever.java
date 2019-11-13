package org.molgenis.vibe.tdb_processing;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.input.ModelReader;
import org.molgenis.vibe.tdb_processing.query_string_creation.QueryStringGenerator;
import org.molgenis.vibe.tdb_processing.query_runner.QueryRunner;

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
     * {@link Gene}{@code s} storage for easy retrieval.
     */
    private Map<Gene, Gene> genes = new HashMap<>();

    /**
     * {@link Disease}{@code s} storage for easy retrieval.
     */
    private Map<Disease, Disease> diseases = new HashMap<>();

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
                QueryStringGenerator.getGenesForPhenotypes(phenotypes));

        while(query.hasNext()) {
            QuerySolution result = query.next();

            // Store new disease, or retrieves existing disease instance if already exists.
            Disease retrievedDisease = new Disease(URI.create(result.get("disease").asResource().getURI()));
            Disease disease = diseases.put(retrievedDisease, retrievedDisease);
            if(disease == null) {
                disease = retrievedDisease;
            }

            // Store new gene, or retrieves existing disease instance if already exists.
            Gene retrievedGene = new Gene(URI.create(result.get("gene").asResource().getURI()));
            Gene gene = genes.put(retrievedGene, retrievedGene);
            if(gene == null) {
                gene = retrievedGene;
            }

            // Retrieves score belonging to the gene-disease combination.
            double score = result.get("gdaScoreNumber").asLiteral().getDouble();

            // The gene-disease combination belonging to the single query result.
            GeneDiseaseCombination comparisonGdc = new GeneDiseaseCombination(gene, disease, score);

            // Retrieves it from the collection (if it already exists).
            GeneDiseaseCombination gdc = geneDiseaseCollection.get(comparisonGdc);

            // If the gene-disease combination is not present yet, uses the comparison gdc and also adds it to the collection.
            if(gdc == null) {
                gdc = comparisonGdc;
                geneDiseaseCollection.add(gdc);
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
