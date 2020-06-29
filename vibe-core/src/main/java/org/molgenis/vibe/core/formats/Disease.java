package org.molgenis.vibe.core.formats;

import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;

/**
 * A disease. Note that equality is determined through {@link URI}{@code s} from the domain
 * <a href="http://linkedlifedata.com">http://linkedlifedata.com</a> describing an identifier from the
 * Unified Medical Language System (UMLS).
 */
public class Disease extends BiologicalEntity {
    public static final String ID_PREFIX = "umls";
    private static final String ID_REGEX = "^(umls|UMLS):(C[0-9]{7})$";
    private static final int REGEX_ID_GROUP = 2;
    private static final String URI_PREFIX = "http://linkedlifedata.com/resource/umls/id/";

    @Override
    protected String getIdPrefix() {
        return ID_PREFIX;
    }

    @Override
    protected String getIdRegex() {
        return ID_REGEX;
    }

    @Override
    protected int getRegexIdGroup() {
        return REGEX_ID_GROUP;
    }

    @Override
    protected String getUriPrefix() {
        return URI_PREFIX;
    }

    public Disease(String id) {
        super(id);
    }

    public Disease(URI uri) {
        super(uri);
    }

    public Disease(String id, String name) throws InvalidStringFormatException {
        super(id, name);
    }

    public Disease(URI uri, String name) throws InvalidStringFormatException {
        super(uri, name);
    }

    @Override
    public String toString() {
        return "Disease{" + super.toString() + '}';
    }
}
