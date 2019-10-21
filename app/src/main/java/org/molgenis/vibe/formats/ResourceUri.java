package org.molgenis.vibe.formats;

import java.net.URI;

/**
 * An Object that also contains a reference to the unique ID within an RDF database (and this reference is an URI).
 */
public interface ResourceUri {
    URI getUri();
}
