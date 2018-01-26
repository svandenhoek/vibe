package org.molgenis.vibe.io;

import org.apache.jena.rdf.model.Model;
import org.molgenis.vibe.TestFile;
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
        modelFileReader = new ModelFilesReader(new String[]{TestFile.GDA1_RDF.getFilePath(),
                TestFile.GENE_RDF.getFilePath(),
                TestFile.DISEASE_DISEASE_RDF.getFilePath(),
                TestFile.PHENOTYPE_RDF.getFilePath(),
                TestFile.PDA_RDF.getFilePath(),
                TestFile.ONTOLOGY.getFilePath()});
        modelFileModel = modelFileReader.getModel();

        tripleStoreReader = new TripleStoreDbReader(TestFile.TDB.getFilePath());
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
