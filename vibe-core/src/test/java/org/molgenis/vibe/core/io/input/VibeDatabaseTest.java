package org.molgenis.vibe.core.io.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.TestData;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Certain tests fail on Jenkins, probably caused by issues setting file/dir permissions.
 */
class VibeDatabaseTest {
    @Test
    void testWritableDirWithIndex() {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();
        Assertions.assertDoesNotThrow(() -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT));
    }

    @Test
    void testWritableDirWithoutIndex() {
        Path hdtFile = TestData.FAKE_HDT_WITHOUT_INDEX.getFullPath();
        Assertions.assertDoesNotThrow(() -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT));
    }

    @Test
    @Tag("skipOnJenkins")
    void testUnreadableHdtInWritableDir() {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();

        try {
            hdtFile.toFile().setReadable(false);

            Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT));
            Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
        } finally { // Reset any permission changes made.
            hdtFile.toFile().setReadable(true);
        }
    }

    @Test
    @Tag("skipOnJenkins")
    void testUnreadableHdtInUnwritableDir() {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();

        try {
            hdtFile.toFile().setReadable(false);
            hdtFile.getParent().toFile().setWritable(false);

            Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT));
            Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
        } finally { // Reset any permission changes made.
            hdtFile.toFile().setReadable(true);
            hdtFile.getParent().toFile().setWritable(true);
        }
    }

    @Test
    @Tag("skipOnJenkins")
    void testReadonlyDirWithIndex() {
        Path hdtFile = TestData.FAKE_HDT_WITH_INDEX.getFullPath();

        try {
            hdtFile.getParent().toFile().setWritable(false);

            Assertions.assertDoesNotThrow(() -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT));
        } finally { // Reset any permission changes made.
            hdtFile.getParent().toFile().setWritable(true);
        }
    }

    @Test
    @Tag("skipOnJenkins")
    void testReadonlyDirWithoutIndex() {
        Path hdtFile = TestData.FAKE_HDT_WITHOUT_INDEX.getFullPath();

        try {
            hdtFile.getParent().toFile().setWritable(false);

            Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT));
            Assertions.assertEquals("Read-only directories require pre-made index file.", exception.getMessage());
        } finally { // Reset any permission changes made.
            hdtFile.getParent().toFile().setWritable(true);
        }
    }

    @Test
    void testOnlyIndexFileGivenAsInput() {
        Path hdtFile = TestData.FAKE_HDT_INDEX_ONLY_INDEX.getFullPath();

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
    }

    @Test
    void testOnlyIndexFileButNonExistingHdtGivenAsInput() {
        Path hdtFile = Paths.get(TestData.FAKE_HDT_INDEX_ONLY_INDEX.getFullPath().getParent().toString() + "/fake.hdt");

        Exception exception = Assertions.assertThrows(IOException.class, () -> new VibeDatabase(hdtFile, ModelReaderFactory.HDT) );
        Assertions.assertEquals("Invalid database. Please check if " + hdtFile.getFileName() + " is a readable .hdt file.", exception.getMessage());
    }
}
