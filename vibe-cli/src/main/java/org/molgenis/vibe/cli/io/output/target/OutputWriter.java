package org.molgenis.vibe.cli.io.output.target;

import java.io.IOException;

/**
 * Superclass for the different types of output writers.
*
 */
public interface OutputWriter {
    /**
     * Returns a {@link String} describing the target to which the output is written (primarily for logging purposes).
     * @return a {@link String} describing the target to which the output is written to.
     */
    String target();

    /**
     * Makes preparations for writing output to an output target. The default is empty and should be overridden by the
     * implementation if an {@code initialize()} step is needed before actual data is written.
     * @throws IOException
     */
    default void initialize() throws IOException {}

    /**
     * Closes output target. The default is empty and should be overridden by the implementation if a {@code close()}
     * step is needed after the data is written (to cleanly close off the target opened during {@code initialize()}).
     * @throws IOException
     */
    default void close() throws IOException {}

    /**
     * Writes single piece of information to output target.
     * @param output the output to be written to the output target
     * @throws IOException
     */
    void write(String output) throws IOException;

    /**
     * Writes the header (if available) to the output target.
     * @param output
     * @throws IOException
     */
    void writeHeader(String output) throws IOException;

    /**
     * Generates a newline (if possible) in the output target.
     * @throws IOException
     */
    void writeNewLine() throws IOException;
}
