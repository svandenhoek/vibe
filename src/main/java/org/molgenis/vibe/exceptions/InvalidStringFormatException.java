package org.molgenis.vibe.exceptions;

/**
 * Thrown to indicate that a method has been given a {@link String} that is formatted in an inappropriate/unexpected way.
 * For example, a {@link String} that is expected to adhere to a specific regular expression (but does not adhere to this).
 */
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
