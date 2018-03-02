package org.molgenis.vibe.rdf_processing;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetAssociationType;
import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetQueryStringGenerator;
import org.molgenis.vibe.rdf_processing.query_string_creation.QueryStringPathRange;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;

import java.net.URI;
import java.util.*;

public class GenesForPhenotypeRetriever extends DisgenetRdfDataRetriever {

    private Set<Phenotype> inputPhenotypes;
    private Set<Phenotype> phenotypes = new HashSet<>();
    private Map<URI, Gene> genes = new HashMap<>();
    private Map<URI, Disease> diseases = new HashMap<>();
    private GeneDiseaseCollection geneDiseaseCollection = new GeneDiseaseCollection();

    public GeneDiseaseCollection getGeneDiseaseCollection() {
        return geneDiseaseCollection;
    }

    public GenesForPhenotypeRetriever(ModelReader modelReader, Set<Phenotype> phenotypes) {
        super(modelReader);
        this.inputPhenotypes = requireNonNull(phenotypes);
    }

    @Override
    public void run() throws CorruptDatabaseException {
        retrieveSources();
        retrieveHpoUris();
        retrievePhenotypeWithChildren();
        retrieveDiseases();
        retrieveGeneDiseaseAssociations();
    }

    private void retrieveHpoUris() throws CorruptDatabaseException {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getIriForHpo(inputPhenotypes));
        addToPhenotypes(query);

        // Checks if for all input phenotypes a new phenotype was created that includes an URI.
        if(inputPhenotypes.size() != phenotypes.size()) {
            throw new CorruptDatabaseException("the retrieved number of phenotypes is not equal to the number of inserted phenotypes.");
        }
    }

    private void retrievePhenotypeWithChildren() {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getHpoChildren(phenotypes, new QueryStringPathRange(QueryStringPathRange.Predefined.ZERO_OR_MORE)));
        addToPhenotypes(query);
    }

    private void addToPhenotypes(QueryRunner query) {
        while(query.hasNext()) {
            QuerySolution result = query.next();

            phenotypes.add(new Phenotype(result.get("hpoId").asLiteral().getString(),
                    result.get("hpoTitle").asLiteral().getString(),
                    URI.create(result.get("hpo").asResource().getURI()))
            );
        }
        query.close();
    }

    private void retrieveDiseases() {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getPdas(phenotypes));

        while(query.hasNext()) {
            QuerySolution result = query.next();
            URI diseaseUri = URI.create(result.get("disease").asResource().getURI());

            diseases.put(diseaseUri,
                    new Disease(result.get("diseaseId").asLiteral().getString(),
                            result.get("diseaseTitle").asLiteral().getString(),
                            diseaseUri)
            );
        }
        query.close();
    }

    private void retrieveGeneDiseaseAssociations() {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getGdas(new HashSet<>(diseases.values()), DisgenetAssociationType.GENE_DISEASE));

        while(query.hasNext()) {
            // Retrieve single result.
            QuerySolution result = query.next();

            // Check if gene is already stored, and if not, stores it (using URI as key).
            URI geneUri = URI.create(result.get("gene").asResource().getURI());
            Gene gene = genes.get(geneUri);
            if(gene == null) {
                gene = new Gene(result.get("geneId").asLiteral().getString(),
                        result.get("geneTitle").asLiteral().getString(),
                        result.get("geneSymbolTitle").asLiteral().getString(),
                        geneUri);

                genes.put(geneUri, gene);
            }

            // Retrieves disease.
            URI diseaseUri = URI.create(result.get("disease").asResource().getURI());
            Disease disease = diseases.get(diseaseUri);

            // Retrieves score belonging to the gene-disease combination.
            double score = Double.parseDouble(result.get("gdaScoreNumber").asLiteral().getString());

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
