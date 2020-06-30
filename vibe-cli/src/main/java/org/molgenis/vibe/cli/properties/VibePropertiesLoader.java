package org.molgenis.vibe.cli.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads the properties stored in {@code application.properties} for usage by {@link VibeProperties}. Should be the first
 * class called in the app before anything else by the {@code start-class}.
 */
public class VibePropertiesLoader {
    private static final String PROPERTIES_FILE_NAME = "application.properties";

    public static void loadProperties() throws IOException {
        Properties properties = new Properties();

        try ( InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME) ) {
            // Load properties.
            properties.load(inputStream);

            // Set variables.
            VibeProperties.APP_NAME.setValue(properties.getProperty("app.name"));
            VibeProperties.APP_VERSION.setValue(properties.getProperty("app.version"));
        }
    }
}
