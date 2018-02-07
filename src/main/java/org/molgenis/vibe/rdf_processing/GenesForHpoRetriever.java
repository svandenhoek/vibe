package org.molgenis.vibe.rdf_processing;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.formats.GeneDiseaseCombination;
import org.molgenis.vibe.formats.Hpo;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.rdf_processing.querying.DisgenetQueryGenerator;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;
import org.molgenis.vibe.rdf_processing.querying.SparqlRange;

import java.util.HashSet;

public class GenesForHpoRetriever extends RdfDataRetriever {

//    private HashSet<Hpo> hpos;
    private HashSet<Gene> genes;
    private HashSet<Disease> diseases;
//    private HashSet<GeneDiseaseCombination> gdas;

    public GenesForHpoRetriever(OptionsParser appOptions, ModelReader modelReader) {
        super(appOptions, modelReader);
    }

    @Override
    public void run() {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
        DisgenetQueryGenerator.getPdas(getAppOptions().getHpoTerms()[0], new SparqlRange(0, true)));

        while(query.hasNext()) {
            QuerySolution result = query.next();
//            new Hpo(result.get("hpoId").asLiteral().getString());
        }

        query.close();
    }
}
