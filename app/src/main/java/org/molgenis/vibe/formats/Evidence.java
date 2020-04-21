package org.molgenis.vibe.formats;

import java.net.URI;

abstract class Evidence extends Entity {
    public Evidence(String id) {
        super(id);
    }

    public Evidence(URI uri) {
        super(uri);
    }
}
