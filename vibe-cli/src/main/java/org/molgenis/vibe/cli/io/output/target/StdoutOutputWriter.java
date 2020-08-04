package org.molgenis.vibe.cli.io.output.target;

/**
 * Writer for writing output to stdout.
 */
public class StdoutOutputWriter implements OutputWriter {
    @Override
    public String target() {
        return "stdout";
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
