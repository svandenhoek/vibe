package org.molgenis.vibe.rdf_processing;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetAssociationType;
import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetQueryStringGenerator;
import org.molgenis.vibe.rdf_processing.query_string_creation.QueryStringPathRange;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;

import java.net.URI;
import java.util.*;

public class GenesForHpoRetriever extends RdfDataRetriever {

    private Map<URI,Source> sources = new HashMap<>();
    private Set<Hpo> hpos = new HashSet<>();
    private Map<URI, Gene> genes = new HashMap<>();
    private Map<URI, Disease> diseases = new HashMap<>();
    private Map<Gene, Map<GeneDiseaseCombination, GeneDiseaseCombination>> gdcsByGene = new HashMap<>();

    public Set<Disease> getDiseases() {
        return Collections.unmodifiableSet(new HashSet<>(diseases.values()));
    }

    public Set<Gene> getGenes() {
        return Collections.unmodifiableSet(new HashSet<>(genes.values()));
    }

    public Set<GeneDiseaseCombination> getCombinationsforGene(Gene gene) {
        return Collections.unmodifiableSet(gdcsByGene.get(gene).keySet());
    }

    public GenesForHpoRetriever(OptionsParser appOptions, ModelReader modelReader) {
        super(appOptions, modelReader);
    }

    @Override
    public void run() throws CorruptDatabaseException {
        retrieveSources();
        retrieveHpos();
        retrieveHpoChildren();
        retrieveDiseases();
        retrieveGeneDiseaseAssociations();
    }

    //TODO: finish source retrieval.
    private void retrieveSources() throws CorruptDatabaseException {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getSources());

        while(query.hasNext()) {
            QuerySolution result = query.next();

            URI sourceUri = URI.create(result.get("source").asResource().getURI());
            try {
//                sources.put(sourceUri,
//                        new Source(result.get("sourceTitle").asLiteral().getString(),
//                                result.get("sourceLevel").asLiteral().getString(), sourceUri));
            } catch (InvalidStringFormatException e) {
                throw new CorruptDatabaseException(e.getLocalizedMessage());
            }

        }
    }

    private void retrieveHpos() throws CorruptDatabaseException {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getIriForHpo(getAppOptions().getHpos()));

        int counter = 0;
        while(query.hasNext()) {
            counter++;
            addHpoToHpos(query.next());
        }
        query.close();

        if(counter != getAppOptions().getHpos().size()) {
            throw new CorruptDatabaseException("the retrieved number of HPOs is not equal to the number of inserted HPOs.");
        }
    }

    private void retrieveHpoChildren() throws CorruptDatabaseException {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getHpoChildren(hpos, new QueryStringPathRange(QueryStringPathRange.Predefined.ZERO_OR_MORE)));

        while(query.hasNext()) {
            addHpoToHpos(query.next());
        }
        query.close();
    }

    private void addHpoToHpos(QuerySolution result) {
        hpos.add(new Hpo(result.get("hpoId").asLiteral().getString(),
                result.get("hpoTitle").asLiteral().getString(),
                URI.create(result.get("hpo").asResource().getURI())
        ));
    }

    private void retrieveDiseases() throws CorruptDatabaseException {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getPdas(hpos));

        while(query.hasNext()) {
            QuerySolution result = query.next();
            URI diseaseUri = URI.create(result.get("disease").asResource().getURI());

            diseases.put(diseaseUri,
                    new Disease(
                            result.get("diseaseId").asLiteral().getString(),
                            result.get("diseaseTitle").asLiteral().getString(),
                            diseaseUri
                    )
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
            GeneDiseaseCombination gdc = new GeneDiseaseCombination(gene, disease, score);

            // The currently stored gene-disease combinations for the query gene.
            Map<GeneDiseaseCombination, GeneDiseaseCombination> gdcsForSingleGene = gdcsByGene.get(gene);
            // If no results available for gene, generates empty map.
            if(gdcsForSingleGene == null) {
                gdcsForSingleGene = new HashMap<>();
            }

            // The gene-disease combination which should be updated.
            GeneDiseaseCombination storedGdc = gdcsForSingleGene.get(gdc);
            // if the gene-disease combination is not present yet, uses basic comparison gdc.
            if(storedGdc == null) {
                storedGdc = gdc;
            }

            //TODO: pre-load possible sources.
            //TODO: add ++ to counters/add evidence.
            if(result.get("evidence") != null) {
//                storedGdc.add(source, evidence);
            } else {
//                storedGdc.add(source);
            }

            gdcsForSingleGene.put(storedGdc, storedGdc);
            gdcsByGene.put(gene, gdcsForSingleGene);
        }
        query.close();
    }
}
