package org.molgenis.vibe.io.output.format;

import org.molgenis.vibe.io.output.target.OutputWriter;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Abstract class defining a complete writer (using {@link OutputWriter} as target to write to).
 */
public abstract class OutputFormatWriter {
    private OutputWriter outputWriter;

    protected OutputWriter getOutputWriter() {
        return outputWriter;
    }

    public OutputFormatWriter(OutputWriter outputWriter) {
        this.outputWriter = requireNonNull(outputWriter);
    }

    protected abstract void generateOutput() throws IOException;

    public void run() throws IOException {
        try {
            outputWriter.initialize();
            generateOutput();
        } finally {
            outputWriter.close();
        }

    }
}
