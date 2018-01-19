package org.molgenis.vibe.options_digestion;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines the specific version of the RDF dataset. If there are specific differences (such as the availability of certain
 * files or fields), these can be specified here (with accompanying getters) so that readers do not need to check for this
 * themselves.
 */
public enum DisgenetRdfVersion {
    V5(new String[]{"disease-disease.ttl", "disease-group.ttl", "disease-phenotype.ttl", "gda_SIO_001119.ttl",
            "gda_SIO_001120.ttl", "gda_SIO_001121.ttl", "gda_SIO_001122.ttl", "gda_SIO_001123.ttl", "gda_SIO_001124.ttl",
            "gda_SIO_001342.ttl", "gda_SIO_001343.ttl", "gda_SIO_001344.ttl", "gda_SIO_001345.ttl", "gda_SIO_001346.ttl",
            "gda_SIO_001347.ttl", "gda_SIO_001348.ttl", "gda_SIO_001349.ttl", "gene.ttl", "pda.ttl", "phenotype.ttl",
            "sio-release.owl"}),
    UNSUPPORTED(null);

    String[] requiredFiles;

    DisgenetRdfVersion(String[] requiredFiles) {
        this.requiredFiles = requiredFiles;
    }

    public String[] getRequiredFiles() {
        return requiredFiles;
    }

    /**
     * Generates an array that contains the full {@link Path}{@code s} of the required files based on the given {@code dir}.
     * @param dir the directory that should contain all the required files
     * @return an array containing the expected {@link Path}{@code s} to all required files
     */
    public Path[] getRequiredFilePaths(Path dir) {
        Path[] filePaths = new Path[requiredFiles.length];
        for(int i = 0; i < requiredFiles.length;i++) {
            filePaths[i] = dir.resolve(requiredFiles[i]);
        }
        return filePaths;
    }

    /**
     * Retrieves the {@link DisgenetRdfVersion} based on a {@link String}. If version is not supported, returns
     * {@link DisgenetRdfVersion#UNSUPPORTED}. However, if {@code versionNumber} does not even adhere to the expected
     * version number format, an {@link InvalidStringFormatException} is thrown instead.
     * @param versionNumber {@link String} from which the version number should be retrieved
     * @return an {@link DisgenetRdfVersion} enum of the specific version
     * @throws InvalidStringFormatException if {@code versionNumber} does not adhere to the regex "^[v|V]?([0-9]+).*$"
     */
    public static DisgenetRdfVersion retrieveVersion(String versionNumber) throws InvalidStringFormatException {
        Matcher m = Pattern.compile("^[v|V]?([0-9]+).*$").matcher(versionNumber);
        if(m.matches()) {
            return retrieveVersion(Integer.parseInt(m.group(1)));
        } else {
            throw new InvalidStringFormatException(versionNumber + " does not adhere the required format: ^[v|V]?([0-9]+).*$");
        }
    }

    /**
     * Retrieves the {@link DisgenetRdfVersion} based on a {@code int}. If version is not supported, returns
     * {@link DisgenetRdfVersion#UNSUPPORTED}.
     * @param versionNumber a number as {@code int} describing the version
     * @return an {@link DisgenetRdfVersion} enum of the specific version
     */
    public static DisgenetRdfVersion retrieveVersion(int versionNumber) {
        switch(versionNumber) {
            case 5:
                return V5;
            default:
                return UNSUPPORTED;
        }
    }
}
