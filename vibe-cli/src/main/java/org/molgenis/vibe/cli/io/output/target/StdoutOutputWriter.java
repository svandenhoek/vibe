package org.molgenis.vibe.cli.io.output.target;

/**
 * Writer for writing output to stdout.
 */
public class StdoutOutputWriter extends OutputWriter {
    @Override
    public String target() {
        return "stdout";
    }

    @Override
    public void initialize() {
        // Do nothing as no initialization is needed.
    }

    @Override
    public void close() {
        // Do nothing as nothing needs to be closed afterwards.
    }

    @Override
    public void write(String output) {
        System.out.print(output);
    }

    @Override
    public void writeHeader(String output) {
        System.out.print(output);
    }

    @Override
    public void writeNewLine() {System.out.print(System.lineSeparator());}
}
