package org.molgenis.vibe.rdf_processing;

import static java.util.Objects.requireNonNull;

import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.options_digestion.OptionsParser;

import java.io.IOException;

public abstract class RdfDataRetriever {
    private OptionsParser appOptions;
    private ModelReader modelReader;

    protected OptionsParser getAppOptions() {
        return appOptions;
    }

    protected ModelReader getModelReader() {
        return modelReader;
    }

    public RdfDataRetriever(OptionsParser appOptions, ModelReader modelReader) {
        this.appOptions = requireNonNull(appOptions);
        this.modelReader = requireNonNull(modelReader);
    }

    public abstract void run() throws IOException;
}
