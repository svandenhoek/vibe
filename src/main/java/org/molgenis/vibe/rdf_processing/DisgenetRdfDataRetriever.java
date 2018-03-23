package org.molgenis.vibe.rdf_processing;

import static java.util.Objects.requireNonNull;

import org.apache.jena.query.QuerySolution;
import org.molgenis.vibe.formats.Source;
import org.molgenis.vibe.io.ModelReader;
import org.molgenis.vibe.rdf_processing.query_string_creation.DisgenetQueryStringGenerator;
import org.molgenis.vibe.rdf_processing.querying.QueryRunner;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves data for further usage from the DisGeNET database.
 */
public abstract class DisgenetRdfDataRetriever {
    private ModelReader modelReader;
    private Map<URI,Source> sources = new HashMap<>();

    protected ModelReader getModelReader() {
        return modelReader;
    }

    /**
     * Requires {@link #retrieveSources()} to be run first!
     * @param uri
     * @return
     */
    protected Source getSource(URI uri) {
        return sources.get(uri);
    }

    /**
     * Requires {@link #retrieveSources()} to be run first!
     * @return all available {@link Source}{@code s}
     */
    protected Map<URI, Source> getSources() {
        return sources;
    }

    public DisgenetRdfDataRetriever(ModelReader modelReader) {
        this.modelReader = requireNonNull(modelReader);
    }

    public abstract void run() throws IOException;

    /**
     * Retrieves sources from the database.
     */
    protected void retrieveSources() {
        QueryRunner query = new QueryRunner(getModelReader().getModel(),
                DisgenetQueryStringGenerator.getSources());

        while(query.hasNext()) {
            QuerySolution result = query.next();

            URI sourceUri = URI.create(result.get("source").asResource().getURI());
            sources.put(sourceUri,
                    new Source(result.get("sourceTitle").asLiteral().getString(),
                            result.get("sourceLevel").asResource().getURI(),
                            sourceUri)
            );
        }
        query.close();
    }
}
