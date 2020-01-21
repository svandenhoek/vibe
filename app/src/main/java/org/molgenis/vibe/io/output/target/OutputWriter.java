package org.molgenis.vibe.io.output.target;

import java.io.IOException;

/**
 * Superclass for the different types of output writers.
*
 */
public abstract class OutputWriter {
    /**
     * Returns a {@link String} describing the target to which the output is written (primarily for logging purposes).
     * @return a {@link String} describing the target to which the output is written to.
     */
    public abstract String target();

    /**
     * Makes preparations for writing output to an output target.
     * @throws IOException
     */
    public abstract void initialize() throws IOException;

    /**
     * Closes output target.
     * @throws IOException
     */
    public abstract void close() throws IOException;

    /**
     * Writes single piece of information to output target.
     * @param output the output to be written to the output target
     * @throws IOException
     */
    public abstract void write(String output) throws IOException;

    /**
     * Writes the header (if available) to the output target.
     * @param output
     * @throws IOException
     */
    public abstract void writeHeader(String output) throws IOException;

    /**
     * Generates a newline (if possible) in the output target.
     * @throws IOException
     */
    public abstract void writeNewLine() throws IOException;
}
