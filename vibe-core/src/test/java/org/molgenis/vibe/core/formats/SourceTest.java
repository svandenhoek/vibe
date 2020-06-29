package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

class SourceTest {
    @Test
    void useReadableSourceLevelName() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "model");
        Assertions.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    void useDisgenetSourceLevelVitalPartOfUri() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "source_evidence_predicted");
        Assertions.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    void useDisgenetSourceLevelFullUri() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "http://rdf.disgenet.org/v5.0.0/void/source_evidence_predicted");
        Assertions.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    void testGetName() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "model");
        Assertions.assertEquals("MGD", source.getName());
    }

    @Test
    void testGetFullName() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "model");
        Assertions.assertEquals("MGD 2017 Dataset Distribution", source.getFullName());
    }
}
