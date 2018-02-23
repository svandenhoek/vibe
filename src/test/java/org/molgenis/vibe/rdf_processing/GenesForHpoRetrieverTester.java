package org.molgenis.vibe.rdf_processing;

import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.formats.Gene;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.rdf_processing.query_string_creation.QueryStringPathRange;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

public class GenesForHpoRetrieverTester {
    private ModelReader reader;
    private GenesForHpoRetriever retriever;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        reader = new TripleStoreDbReader(TestFilesDir.TDB_MINI.getDir());

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        retriever = null;
    }

    @Test
    public void retrieveAllDiseasesForHpoAndDirectChildren() throws CorruptDatabaseException {
        OptionsParser options = new OptionsParser() {
            // Instance initializer.
            {
                setHpos(new String[]{"hp:0001377"});
                setQueryStringPathRange(new QueryStringPathRange(1, false));
            }
        };

        retriever = new GenesForHpoRetriever(options, reader);

        Set<Disease> expectedOutputDiseases = new HashSet<>();
        expectedOutputDiseases.addAll(Arrays.asList(
                new Disease("umls:C0410538"),
                new Disease("umls:C1850318")
        ));

        Set<Gene> expectedOutputGenes = new HashSet<>();
        expectedOutputGenes.addAll(Arrays.asList(
                new Gene("1311"),
                new Gene("10082"),
                new Gene("960")
        ));

        retriever.run();
        Assert.fail("finish test");
//        Assert.assertEquals(retriever.getDiseases(), expectedOutputDiseases);
//        Assert.assertEquals(retriever.getGenes(), expectedOutputGenes);
    }
}
