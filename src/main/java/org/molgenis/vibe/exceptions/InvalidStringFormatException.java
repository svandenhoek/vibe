package org.molgenis.vibe.exceptions;

public class InvalidStringFormatException extends IllegalArgumentException {
    /**
     * Constructs an {@code InvalidStringFormatException} with {@code null}
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
