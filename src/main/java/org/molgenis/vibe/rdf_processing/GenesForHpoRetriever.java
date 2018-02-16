package org.molgenis.vibe.rdf_processing;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCombination;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.rdf_processing.querying.DisgenetQueryGenerator;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;

import java.util.HashSet;
import java.util.Set;

public class GenesForHpoRetriever extends RdfDataRetriever {

    private HashSet<Hpo> hpos;
    private Set<Gene> genes;
    private Set<Disease> diseases = new HashSet<>();
    private Set<GeneDiseaseCombination> gdas;

    public Set<Disease> getDiseases() {
        return diseases;
    }

    public GenesForHpoRetriever(OptionsParser appOptions, ModelReader modelReader) {
        super(appOptions, modelReader);
    }

    @Override
    public void run() throws CorruptDatabaseException {
        retrieveDiseases();
        retrieveGenes();
    }

    private void retrieveHpos() throws CorruptDatabaseException {
//        QueryRunner query = new QueryRunner(getModelReader().getModel(),
//                DisgenetQueryGenerator.getIriForHpo();
    }

    private void retrieveDiseases() throws CorruptDatabaseException {
//        QueryRunner query = new QueryRunner(getModelReader().getModel(),
//                DisgenetQueryGenerator.getPdas(, getAppOptions().getSparqlRange()));
//
//        while(query.hasNext()) {
//            QuerySolution result = query.next();
//            try {
//                diseases.add(new Disease(
//                        result.get("disease").asResource().getURI(),
//                        result.get("diseaseTitle").asLiteral().getString()
//                ));
//            } catch (IllegalArgumentException e) {
//                throw new CorruptDatabaseException(e);
//            }
//        }
//
//        query.close();
    }

    private void retrieveGenes() {

    }
}
