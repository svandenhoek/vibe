package org.molgenis.vibe.rdf_processing;

import org.molgenis.vibe.TestFilesDir;
import org.molgenis.vibe.exceptions.CorruptDatabaseException;
import org.molgenis.vibe.formats.Disease;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.io.TripleStoreDbReader;
import org.molgenis.vibe.options_digestion.OptionsParser;
import org.molgenis.vibe.rdf_processing.querying.SparqlRange;
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
                setHpoTerms(new String[]{"hp:0001376"});
                setSparqlRange(new SparqlRange(1, false));
            }
        };

        retriever = new GenesForHpoRetriever(options, reader);

        Set<Disease> expectedOutput = new HashSet<>();
        expectedOutput.addAll(Arrays.asList(
                new Disease("http://linkedlifedata.com/resource/umls/id/C0015773", "Felty Syndrome"),
                new Disease("http://linkedlifedata.com/resource/umls/id/C1853733", "HEMOCHROMATOSIS, TYPE 4"),
                new Disease("http://linkedlifedata.com/resource/umls/id/C1834674", "Bethlem myopathy")
        ));

        retriever.run();
        Assert.assertEquals(retriever.getDiseases(), expectedOutput);
    }
}
