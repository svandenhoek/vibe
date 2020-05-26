package org.molgenis.vibe.cli.properties;

/**
 * Properties (parsed by {@link VibePropertiesLoader}) stored in {@code application.properties} accessible anywhere in
 * the app (so {@link VibePropertiesLoader} should always be used first to parse the properties).
 */
public enum VibeProperties {
    APP_NAME,
    APP_VERSION;

    private String value;

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }
}
