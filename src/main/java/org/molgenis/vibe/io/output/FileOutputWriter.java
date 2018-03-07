package org.molgenis.vibe.io.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class FileOutputWriter {
    private Path path;

    private BufferedWriter writer;

    public Path getPath() {
        return path;
    }

    protected BufferedWriter getWriter() throws IOException {
        if(writer == null) {
            prepareWriter();
        }
        return writer;
    }

    public FileOutputWriter(Path path) {
        this.path = path;
    }

    protected void prepareWriter() throws IOException {
        writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
    }

    public abstract void run() throws IOException;
}
