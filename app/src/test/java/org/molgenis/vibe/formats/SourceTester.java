package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

public class SourceTester {
    @Test
    public void useReadableSourceLevelName() throws InvalidStringFormatException {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "model");
        Assert.assertEquals(source.getLevel(), Source.Level.MODEL);
    }

    @Test
    public void useDisgenetSourceLevelVitalPartOfUri() throws InvalidStringFormatException {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "source_evidence_predicted");
        Assert.assertEquals(source.getLevel(), Source.Level.MODEL);
    }

    @Test
    public void useDisgenetSourceLevelFullUri() throws InvalidStringFormatException {
        Source source = new Source(URI.create("http://rdf.disgenet.org/v5.0.0/void/MGD"), "MGD 2017 Dataset Distribution", "http://rdf.disgenet.org/v5.0.0/void/source_evidence_predicted");
        Assert.assertEquals(source.getLevel(), Source.Level.MODEL);
    }
}
