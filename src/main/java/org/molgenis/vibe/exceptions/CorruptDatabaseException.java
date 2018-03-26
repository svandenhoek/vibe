package org.molgenis.vibe.exceptions;

import java.io.IOException;

/**
 * Indicates the database did not return the expected output. Note that this does not regard the individual results, but
 * what type of output is returned for a given request (for example the retrieved output contains completely different
 * output or is formatted in an unexpected way).
 */
public class CorruptDatabaseException extends IOException {
    /**
     * Constructs an {@code CorruptDatabaseException} with {@code null}
     * as its error detail message.
     */
    public CorruptDatabaseException() {
        super();
    }

    public CorruptDatabaseException(String message) {
        super(message);
    }

    public CorruptDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorruptDatabaseException(Throwable cause) {
        super(cause);
    }
}
