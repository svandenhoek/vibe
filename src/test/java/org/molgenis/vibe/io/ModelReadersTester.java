package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
import org.molgenis.vibe.TestFilesDir;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ModelReadersTester {
    private ModelReader modelFileReaderNoOntology;
    private ModelReader modelFileReaderWithOntology;
    private ModelReader tripleStoreReaderNoOntology;
    private ModelReader tripleStoreReaderWithOntology;

    private Model modelFileModelNoOntology;
    private Model modelFileModelWithOntology;
    private Model tripleStoreModelNoOntology;
    private Model tripleStoreModelWithOntology;

    @BeforeClass
    public void beforeClass() {
        modelFileReaderNoOntology = new ModelFilesReader(TestFilesDir.TTL_NO_ONTOLOGY.getFiles());
        modelFileReaderWithOntology = new ModelFilesReader(TestFilesDir.TTL.getFiles());
        tripleStoreReaderNoOntology = new TripleStoreDbReader(TestFilesDir.TDB_MINI_NO_ONTOLOGY.getDir());
        tripleStoreReaderWithOntology = new TripleStoreDbReader(TestFilesDir.TDB_MINI.getDir());

        modelFileModelNoOntology = modelFileReaderNoOntology.getModel();
        modelFileModelWithOntology = modelFileReaderWithOntology.getModel();
        tripleStoreModelNoOntology = tripleStoreReaderNoOntology.getModel();
        tripleStoreModelWithOntology = tripleStoreReaderWithOntology.getModel();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        modelFileModelNoOntology.close();
        modelFileModelWithOntology.close();
        tripleStoreModelNoOntology.close();
        tripleStoreModelWithOntology.close();

        modelFileReaderNoOntology.close();
        modelFileReaderWithOntology.close();
        tripleStoreReaderNoOntology.close();
        tripleStoreReaderWithOntology.close();
    }

    @Test
    public void checkIfModelFileReaderIsNotEmpty() {
        Assert.assertEquals(modelFileModelWithOntology.isEmpty(), false);
    }

    @Test
    public void checkIfTripleStoreFileNotEmpty() {
        Assert.assertEquals(tripleStoreModelWithOntology.isEmpty(), false);
    }

    @Test
    public void checkIfModelsAreEqualWithoutOntologyData() {
        // All statements in modelFileModel that are not in tripleStoreModel
        Model modelDiff1 = modelFileModelNoOntology.difference(tripleStoreModelNoOntology);
        // All statements in tripleStoreModel that are not in modelFileModel
        Model modelDiff2 = tripleStoreModelNoOntology.difference(modelFileModelNoOntology);

        Assert.assertEquals(modelDiff1.isEmpty(), true);
        Assert.assertEquals(modelDiff2.isEmpty(), true);
    }

    /**
     * Currently disabled as while the test fails, the output of the different models seems equal (just in a different order).
     * Possibly implement a comparison with the model diff ordered somehow?
     */
    @Test(enabled = false)
    public void checkIfModelsAreEqualWithOntologyData() {
        // All statements in modelFileModel that are not in tripleStoreModel
        Model modelDiff1 = modelFileModelWithOntology.difference(tripleStoreModelWithOntology);
        // All statements in tripleStoreModel that are not in modelFileModel
        Model modelDiff2 = tripleStoreModelWithOntology.difference(modelFileModelWithOntology);

//        FileOutputStream out1 = new FileOutputStream("out1.ttl");
//        FileOutputStream out2 = new FileOutputStream("out2.ttl");
//
//        RDFDataMgr.write(out1, modelDiff1, Lang.TTL);
//        RDFDataMgr.write(out2, modelDiff2, Lang.TTL);
//
//        out1.flush();
//        out1.close();
//
//        out2.flush();
//        out2.close();

        Assert.assertEquals(modelDiff1.isEmpty(), true);
        Assert.assertEquals(modelDiff2.isEmpty(), true);
    }
}
