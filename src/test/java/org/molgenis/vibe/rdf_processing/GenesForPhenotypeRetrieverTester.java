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

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Note that these tests use data from DisGeNET for validation. However, this was kept as minimal as possible while still
 * being able to actually test the functioning of the code and only reflects what is EXPECTED to be found within the
 * DisGeNET dataset when using the query (on a technical basis). Additionally, the actual data on which these tests are
 * executed on are available externally and are not included in the repository itself.
 *
 * The full DisGeNET RDF dataset can be downloaded from: http://rdf.disgenet.org/download/
 * The license can be found on: http://www.disgenet.org/ds/DisGeNET/html/legal.html
 */
public class GenesForPhenotypeRetrieverTester {
    private ModelReader reader;
    private GenesForPhenotypeRetriever retriever;

    @BeforeClass
    public void beforeClass() throws IOException {
        reader = new TripleStoreDbReader(TestData.TDB_MINI.getDir());

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        retriever = null;
    }

    @Test
    public void retrieveGeneDiseaseCollectionForMultiplePhenotypes() {
        Gene[] genes = new Gene[]{
                new Gene("ncbigene:1311", "cartilage oligomeric matrix protein", "COMP", 0.507872279859934E0, 0.607142857142857E0, URI.create("http://identifiers.org/ncbigene/1311")), // umls:C0410538
                new Gene("ncbigene:10082", "glypican 6", "GPC6", 0.596109001438773E0, 0.5E0, URI.create("http://identifiers.org/ncbigene/10082")), // umls:C1850318 & umls:C1968605
                new Gene("ncbigene:960", "CD44 molecule (Indian blood group)", "CD44", 0.383591614803352E0, 0.821428571428571E0, URI.create("http://identifiers.org/ncbigene/960")), // umls:C1850318
                new Gene("ncbigene:10075", "HECT, UBA and WWE domain containing 1, E3 ubiquitin protein ligase", "HUWE1", 0.625716589831481E0, 0.5E0, URI.create("http://identifiers.org/ncbigene/10075")), // umls:C1867103
                new Gene("ncbigene:2261", "fibroblast growth factor receptor 3", "FGFR3", 0.366634840656414E0, 0.821428571428571E0, URI.create("http://identifiers.org/ncbigene/2261")), // umls:C1867103
        };

        Disease[] diseases = new Disease[]{
                new Disease("umls:C0410538", "Pseudoachondroplasia", URI.create("http://linkedlifedata.com/resource/umls/id/C0410538")), // pda.ttl
                new Disease("umls:C1850318", "Omodysplasia type 1", URI.create("http://linkedlifedata.com/resource/umls/id/C1850318")), // pda.ttl
                new Disease("umls:C1867103", "Limited elbow extension", URI.create("http://linkedlifedata.com/resource/umls/id/C1867103")), // phenotype.ttl -> exactMatch
                new Disease("umls:C1968605", "Limited elbow flexion/extension", URI.create("http://linkedlifedata.com/resource/umls/id/C1968605")) // phenotype.ttl -> exactMatch
        };

        GeneDiseaseCombination[] geneDiseaseCombinations = new GeneDiseaseCombination[]{
                new GeneDiseaseCombination(genes[0], diseases[0], 0.702671298020428E0),
                new GeneDiseaseCombination(genes[1], diseases[1], 0.400824180352639E0),
                new GeneDiseaseCombination(genes[2], diseases[1], 2.747267842131E-4),
                new GeneDiseaseCombination(genes[3], diseases[2], 0.2E0),
                new GeneDiseaseCombination(genes[4], diseases[2], 0.2E0),
                new GeneDiseaseCombination(genes[1], diseases[3], 0.2E0)
        };

        Source[] sources = new Source[] {
                new Source("UniProt 2017 Dataset Distribution", Source.Level.CURATED, URI.create("http://rdf.disgenet.org/v5.0.0/void/UNIPROT")),
                new Source("CTD_human 2017 Dataset Distribution", Source.Level.CURATED, URI.create("http://rdf.disgenet.org/v5.0.0/void/CTD_human")),
                new Source("BeFree 2017 Dataset Distribution", Source.Level.LITERATURE, URI.create("http://rdf.disgenet.org/v5.0.0/void/BEFREE")),
                new Source("HPO", Source.Level.CURATED, URI.create("http://rdf.disgenet.org/v5.0.0/void/HPO"))
        };

        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/11565064"));
        geneDiseaseCombinations[0].add(sources[0], URI.create("http://identifiers.org/pubmed/21922596"));
        geneDiseaseCombinations[1].add(sources[1], URI.create("http://identifiers.org/pubmed/19481194"));
        geneDiseaseCombinations[2].add(sources[2], URI.create("http://identifiers.org/pubmed/19481194"));
        geneDiseaseCombinations[3].add(sources[3]);
        geneDiseaseCombinations[4].add(sources[3]);
        geneDiseaseCombinations[5].add(sources[3]);

        GeneDiseaseCollection expectedCollection = new GeneDiseaseCollection();
        expectedCollection.addAll(Arrays.asList(geneDiseaseCombinations));

        retriever = new GenesForPhenotypeRetriever(reader, new HashSet<>(Arrays.asList(new Phenotype("hp:0001377"), new Phenotype("hp:0005060"))));
        retriever.run();
        GeneDiseaseCollection actualCollection = retriever.getGeneDiseaseCollection();

        assertGeneDiseaseCombination(actualCollection, expectedCollection);
    }

    @Test
    public void retrieveGeneDiseaseCollectionWithDiseaseNotDirectlyLinkedToPhenotype() {

    }

    private void assertGeneDiseaseCombination(GeneDiseaseCollection actualCollection, GeneDiseaseCollection expectedCollection) {
        // General comparison (does not compare all content)
        Assert.assertEquals(actualCollection, expectedCollection);

        // Above assert only compares equals. Certain classes might store additional data that should not play a role
        // when validating equality but should be checked on whether they were loaded from the database correctly. An
        // example of this would be a score belonging to a Gene. While it should not make it a "different" Gene, it does
        // describe the Gene. For this reason, toString() is used as extra validation (with the assumption that these
        // extra fields are mentioned in toString()).
        Assert.assertEquals(actualCollection.toString(), expectedCollection.toString());
    }
}
