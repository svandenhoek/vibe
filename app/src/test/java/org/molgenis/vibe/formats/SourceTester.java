package org.molgenis.vibe.formats;

import org.junit.Assert;
import org.junit.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

public class SourceTester {
    @Test
    public void useReadableSourceLevelName() throws InvalidStringFormatException {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "model");
        Assert.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    public void useDisgenetSourceLevelVitalPartOfUri() throws InvalidStringFormatException {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "source_evidence_predicted");
        Assert.assertEquals(Source.Level.MODEL, source.getLevel());
    }

    @Test
    public void useDisgenetSourceLevelFullUri() throws InvalidStringFormatException {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "http://rdf.disgenet.org/v5.0.0/void/source_evidence_predicted");
        Assert.assertEquals(Source.Level.MODEL, source.getLevel());
    }
}
