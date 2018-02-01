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
    V5,
    UNSUPPORTED;

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
