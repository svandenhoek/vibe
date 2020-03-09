package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

public class SourceTest {
    @Test
    public void useReadableSourceLevelName() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "model");
        Assertions.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    public void useDisgenetSourceLevelVitalPartOfUri() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "source_evidence_predicted");
        Assertions.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    public void useDisgenetSourceLevelFullUri() {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "http://rdf.disgenet.org/v5.0.0/void/source_evidence_predicted");
        Assertions.assertEquals(Source.Level.MODEL, source.getLevel());
    }
}
