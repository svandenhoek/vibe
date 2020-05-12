package org.molgenis.vibe.cli.io.output.target;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

/**
 * Writer for writing output to a file.
 */
public class FileOutputWriter extends OutputWriter {
    /**
     * Path to write output to.
     */
    private Path path;

    /**
     * Writer to be used for file writing.
     */
    private BufferedWriter writer;

    public Path getPath() {
        return path;
    }

    public FileOutputWriter(Path path) {
        this.path = requireNonNull(path);
    }

    @Override
    public String target() {
        return getPath().toString();
    }

    @Override
    public void initialize() throws IOException {
        if(writer == null) {
            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        }
    }

    @Override
    public void close() throws IOException {
        if(writer != null) {
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void write(String output) throws IOException {
        writer.write(output);
    }

    @Override
    public void writeHeader(String output) throws IOException {
        writer.write(output);
    }

    @Override
    public void writeNewLine() throws IOException {
        writer.newLine();
    }
}
