package org.molgenis.vibe.exceptions;

import java.io.IOException;

public class InvalidStringFormatException extends IOException {
    /**
     * Constructs an {@code IOException} with {@code null}
     * as its error detail message.
     */
    public InvalidStringFormatException() {
        super();
    }

    public InvalidStringFormatException(String message) {
        super(message);
    }

    public InvalidStringFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStringFormatException(Throwable cause) {
        super(cause);
    }
}
