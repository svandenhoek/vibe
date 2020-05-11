package org.molgenis.vibe.cli.output.target;

/**
 * Writer for writing output to stdout.
 */
public class StdoutOutputWriter extends OutputWriter {
    @Override
    public String target() {
        return "stdout";
    }

    @Override
    public void initialize() {}

    @Override
    public void close() {}

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
