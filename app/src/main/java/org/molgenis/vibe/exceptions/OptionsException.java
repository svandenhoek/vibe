package org.molgenis.vibe.exceptions;

import java.io.IOException;

/**
 * Thrown to indicate the user-input options were invalid. Specifically, cases such as missing arguments, invalid combination
 * of arguments and alike. It should not be thrown in cases such as the actual path to a file being invalid.
 */
public class OptionsException extends IOException {
    /**
     * Constructs an {@code OptionsException} with {@code null}
     * as its error detail message.
     */
    public OptionsException() {
        super();
    }

    public OptionsException(String message) {
        super(message);
    }

    public OptionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionsException(Throwable cause) {
        super(cause);
    }
}
