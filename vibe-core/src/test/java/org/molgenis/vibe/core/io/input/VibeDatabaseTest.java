package org.molgenis.vibe.core.io.input;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.TestData;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VibeDatabaseTest {
    @AfterEach
    void afterEach() {
        TestData.FAKE_HDT_WITH_INDEX.getFullPath().getParent().toFile().setWritable(true);
        TestData.FAKE_HDT_WITH_INDEX.getFullPath().toFile().setReadable(true);

        TestData.FAKE_HDT_WITHOUT_INDEX.getFullPath().getParent().toFile().setWritable(true);
    }

    @Test
    void testUnreadableHdtInWritableDir() throws IOException {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();
        hdtFile.toFile().setReadable(false);

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
    }

    @Test
    void testUnreadableHdtInUnwritableDir() throws IOException {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();
        hdtFile.toFile().setReadable(false);
        hdtFile.getParent().toFile().setWritable(false);

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
    }

    @Test
    void testReadonlyDirWithIndex() throws IOException {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();
        hdtFile.getParent().toFile().setWritable(false);

        new VibeDatabase(hdtFile, ModelReaderFactory.HDT);
    }

    @Test
    void testReadonlyDirWithoutIndex() throws IOException {
        Path hdtFile = TestData.FAKE_HDT_WITHOUT_INDEX.getFullPath();
        hdtFile.getParent().toFile().setWritable(false);

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Read-only directories require pre-made index file.", exception.getMessage());
    }

    @Test
    void testOnlyIndexFileGivenAsInput() throws IOException {
        Path hdtFile = TestData.FAKE_HDT_INDEX_ONLY_INDEX.getFullPath();

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
    }

    @Test
    void testOnlyIndexFileButNonExistingHdtGivenAsInput() throws IOException {
        Path hdtFile = Paths.get(TestData.FAKE_HDT_INDEX_ONLY_INDEX.getFullPath().getParent().toString() + "/fake.hdt");

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
    }
}
