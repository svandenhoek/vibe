package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
import org.molgenis.vibe.TestFilesDir;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ModelReadersTester {
    private ModelReader modelFileReader;
    private ModelReader tripleStoreReader;

    private Model modelFileModel;
    private Model tripleStoreModel;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        modelFileReader = new ModelFilesReader(TestFilesDir.TTL.getFiles());
        modelFileModel = modelFileReader.getModel();

        tripleStoreReader = new TripleStoreDbReader(TestFilesDir.TDB_MINI.getDir());
        tripleStoreModel = tripleStoreReader.getModel();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        modelFileReader.close();
        tripleStoreReader.close();
    }

    @Test
    public void checkIfModelFileReaderIsNotEmpty() {
        Assert.assertEquals(modelFileModel.isEmpty(), false);
    }

    @Test
    public void checkIfTripleStoreFileNotEmpty() {
        Assert.assertEquals(tripleStoreModel.isEmpty(), false);
    }

    @Test
    public void checkIfModelsAreEqual() {
        // All statements in modelFileModel that are not in tripleStoreModel
        Model modelDiff1 = modelFileModel.difference(tripleStoreModel);
        // All statements in tripleStoreModel that are not in modelFileModel
        Model modelDiff2 = tripleStoreModel.difference(modelFileModel);

        Assert.assertEquals(modelDiff1.isEmpty(), true);
        Assert.assertEquals(modelDiff2.isEmpty(), true); // tdbloader2 adds data?
    }
}
