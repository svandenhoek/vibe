package org.molgenis.vibe.rdf_processing;

import org.molgenis.vibe.TestData;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.*;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.*;

/**
 * Note that these tests use data from DisGeNET for validation. These files are not provided (though a bash download
 * script is present in the GitHub repository). For validation purposes some data (such as gene-disease association IDs)
 * are present within this test class. However, this was kept as minimal as possible while still being able to actually
 * test the functioning of the code and only reflects what is EXPECTED to be found within the DisGeNET dataset when using
 * the query (on a technical basis). The DisGeNET RDF dataset can be downloaded from http://rdf.disgenet.org/download/
 * and the license can be found on http://www.disgenet.org/ds/DisGeNET/html/legal.html .
 */
public class GenesForPhenotypeRetrieverTester {
    private ModelReader reader;
    private GenesForPhenotypeRetriever retriever;

    @BeforeClass
    public void beforeClass() {
        reader = new TripleStoreDbReader(TestData.TDB_MINI.getDir());

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        retriever = null;
    }

    @Test
    public void retrieveAllDiseasesForPhenotypeAndDirectChildren() throws CorruptDatabaseException {
        Gene[] genes = new Gene[]{
                new Gene("ncbigene:1311", "cartilage oligomeric matrix protein", "COMP", URI.create("http://identifiers.org/ncbigene/1311")),
                new Gene("ncbigene:10082", "glypican 6", "GPC6", URI.create("http://identifiers.org/ncbigene/10082")),
                new Gene("ncbigene:960", "CD44 molecule (Indian blood group)", "CD44", URI.create("http://identifiers.org/ncbigene/960"))
        };

        Disease[] diseases = new Disease[]{
                new Disease("umls:C0410538", "Pseudoachondroplasia", URI.create("http://linkedlifedata.com/resource/umls/id/C0410538")),
                new Disease("umls:C1850318", "Omodysplasia type 1", URI.create("http://linkedlifedata.com/resource/umls/id/C1850318"))
        };

        GeneDiseaseCombination[] geneDiseaseCombinations = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0]),
                new GeneDiseaseCombination(genes[1], diseases[1]),
                new GeneDiseaseCombination(genes[2], diseases[1])
        };

        Source[] sources = new Source[] {
                new Source("UniProt 2017 Dataset Distribution", Source.Level.CURATED, URI.create("http://rdf.disgenet.org/v5.0.0/void/UNIPROT")),
                new Source("CTD_human 2017 Dataset Distribution", Source.Level.CURATED, URI.create("http://rdf.disgenet.org/v5.0.0/void/CTD_human")),
                new Source("BeFree 2017 Dataset Distribution", Source.Level.LITERATURE, URI.create("http://rdf.disgenet.org/v5.0.0/void/BEFREE"))
        };

        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/11565064"));
        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/21922596"));
        geneDiseaseCombinations[1].add(sources[1], URI.create("http://identifiers.org/pubmed/19481194"));
        geneDiseaseCombinations[2].add(sources[2], URI.create("http://identifiers.org/pubmed/19481194"));

        retriever = new GenesForPhenotypeRetriever(reader, new HashSet<>(Arrays.asList(new Phenotype("hp:0001377"))));
        retriever.run();
        GeneDiseaseCollection actualCollection = retriever.getGeneDiseaseCollection();

        // Compares based on the id equality.
        Assert.assertEquals(actualCollection, new HashSet<>(Arrays.asList(geneDiseaseCombinations)));

        // Compares extra fields for geneDiseaseCombinations that are not in the equals().
        for(int i = 0; i < geneDiseaseCombinations.length; i++) {
            // The GeneDiseaseCombination to be compared.
            GeneDiseaseCombination expectedGdc = geneDiseaseCombinations[i];
            GeneDiseaseCombination actualGdc = actualCollection.get(geneDiseaseCombinations[i]);

            // Check gene fields.
            Assert.assertEquals(actualGdc.getGene().getName(), expectedGdc.getGene().getName());
            Assert.assertEquals(actualGdc.getGene().getSymbol(), expectedGdc.getGene().getSymbol());
            Assert.assertEquals(actualGdc.getGene().getUri(), expectedGdc.getGene().getUri());

            // Check disease fields.
            Assert.assertEquals(actualGdc.getDisease().getName(), expectedGdc.getDisease().getName());
            Assert.assertEquals(actualGdc.getDisease().getUri(), expectedGdc.getDisease().getUri());

            // Check other GeneDiseaseCombination fields.
            Assert.assertEquals(actualGdc.getSourcesCount(), expectedGdc.getSourcesCount());
            Assert.assertEquals(actualGdc.getSourcesWithEvidence(), expectedGdc.getSourcesWithEvidence());
        }
    }
}
