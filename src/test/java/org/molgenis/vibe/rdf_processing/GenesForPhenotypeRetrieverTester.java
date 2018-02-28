package org.molgenis.vibe.rdf_processing;

import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.rdf_processing.query_string_creation.QueryStringPathRange;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

public class GenesForPhenotypeRetrieverTester {
    private ModelReader reader;
    private GenesForPhenotypeRetriever retriever;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        reader = new TripleStoreDbReader(TestFilesDir.TDB_MINI.getDir());

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        retriever = null;
    }

    @Test
    public void retrieveAllDiseasesForPhenotypeAndDirectChildren() throws CorruptDatabaseException {
        OptionsParser options = new OptionsParser() {
            // Instance initializer.
            {
                setPhenotypes(new String[]{"hp:0001377"});
                setQueryStringPathRange(new QueryStringPathRange(QueryStringPathRange.Predefined.ZERO_OR_MORE));
            }
        };

        Gene[] genes = new Gene[]{
                new Gene("1311"),
                new Gene("10082"),
                new Gene("960")
        };

        Disease[] diseases = new Disease[]{
                new Disease("umls:C0410538"),
                new Disease("umls:C1850318")
        };

        GeneDiseaseCombination[] geneDiseaseCombinations = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0]),
                new GeneDiseaseCombination(genes[1], diseases[1]),
                new GeneDiseaseCombination(genes[2], diseases[1])
        };

        Source[] sources = new Source[] {
                new Source("UniProt 2017 Dataset Distribution"),
                new Source("CTD_human 2017 Dataset Distribution"),
                new Source("BeFree 2017 Dataset Distribution")
        };

        URI[] pubmedIds = new URI[] {
                URI.create("http://identifiers.org/pubmed/11565064"),
                URI.create("http://identifiers.org/pubmed/21922596"),
                URI.create("http://identifiers.org/pubmed/19481194"),
                URI.create("http://identifiers.org/pubmed/19481194")
        };

        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/11565064"));
        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/21922596"));
        geneDiseaseCombinations[1].add(sources[1], URI.create("http://identifiers.org/pubmed/19481194"));
        geneDiseaseCombinations[2].add(sources[2], URI.create("http://identifiers.org/pubmed/19481194"));

        retriever = new GenesForPhenotypeRetriever(options, reader);
        retriever.run();
        GeneDiseaseCollection actualCollection = retriever.getGeneDiseaseCollection();

        // Compares based on the id equality.
        Assert.assertEquals(actualCollection, new HashSet<>(Arrays.asList(geneDiseaseCombinations)));

        // Compares extra fields for geneDiseaseCombinations that are not in the equals().
        for(int i = 0; i < geneDiseaseCombinations.length; i++) {
            Assert.assertEquals(actualCollection.get(geneDiseaseCombinations[i]).getSourcesCount(), geneDiseaseCombinations[i].getSourcesCount());
            Assert.assertEquals(actualCollection.get(geneDiseaseCombinations[i]).getSourcesWithEvidence(), geneDiseaseCombinations[i].getSourcesWithEvidence());
        }
    }
}
