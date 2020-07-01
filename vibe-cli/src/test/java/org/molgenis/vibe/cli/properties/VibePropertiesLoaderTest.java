package org.molgenis.vibe.cli.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Note that {@link VibePropertiesLoader#loadProperties()} only needs to be run once app-wide, so any pre-loading tests
 * could cause issues if other tests elsewhere would also load the properties file due to needing values from it.
 */
class VibePropertiesLoaderTest {
    @Test
    void testIfVibePropertiesSetAfterLoading() throws IOException {
        VibePropertiesLoader.loadProperties();

        Assertions.assertAll(
                () -> Assertions.assertNotNull(VibeProperties.APP_NAME.getValue()),
                () -> Assertions.assertNotNull(VibeProperties.APP_VERSION.getValue())
        );
    }
}
