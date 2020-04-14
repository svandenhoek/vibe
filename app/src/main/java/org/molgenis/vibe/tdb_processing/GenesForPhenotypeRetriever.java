package org.molgenis.vibe.tdb_processing;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.input.ModelReader;
import org.molgenis.vibe.tdb_processing.query_string_creation.QueryStringGenerator;

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
        // Variables for storage of already found data (reduces creating identical objects).
        Map<Gene, Gene> genes = new HashMap<>();
        Map<Disease, Disease> diseases = new HashMap<>();
        Map<PubmedEvidence,PubmedEvidence> foundPubmedEvidence = new HashMap<>();

        // Prepares query.
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                QueryStringGenerator.getGenesForPhenotypes(phenotypes));

        // Processes query.
        while(query.hasNext()) {
            QuerySolution result = query.next();

            // Store new disease, or retrieves existing disease instance if already exists.
            Disease disease = processEntityOnAlreadyFound(
                new Disease(URI.create(result.get("disease").asResource().getURI()),
                        result.get("diseaseName").asLiteral().getString()),
                diseases
            );

            // Store new gene, or retrieves existing disease instance if already exists.
            Gene gene = processEntityOnAlreadyFound(
                new Gene(URI.create(result.get("gene").asResource().getURI()),
                        new GeneSymbol(URI.create(result.get("geneSymbol").asResource().getURI()))),
                genes
            );

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
                PubmedEvidence pubmedEvidence = processEntityOnAlreadyFound(
                    new PubmedEvidence(URI.create(result.get("evidence").asResource().getURI()),
                        result.get("evidenceYear").asLiteral().getInt()),
                    foundPubmedEvidence
                );
                gdc.add(source, pubmedEvidence);
            } else {
                gdc.add(source);
            }
        }

        query.close();
    }

    private <T extends Entity> T processEntityOnAlreadyFound(T entity, Map<T, T> foundEntities) {
        T foundEntity = foundEntities.get(entity);
        if(foundEntity == null) {
            foundEntities.put(entity, entity);
            foundEntity = entity;
        }
        return foundEntity;
    }
}
