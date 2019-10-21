package org.molgenis.vibe.io.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An output writer for files.
 */
public abstract class FileOutputWriter {
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

    protected BufferedWriter getWriter() throws IOException {
        if(writer == null) {
            prepareWriter();
        }
        return writer;
    }

    protected void closeWriter() throws IOException {
        if(writer != null) {
            writer.flush();
            writer.close();
        }
    }

    public FileOutputWriter(Path path) {
        this.path = path;
    }

    protected void prepareWriter() throws IOException {
        writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
    }

    public abstract void run() throws IOException;
}
