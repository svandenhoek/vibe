package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defines a HPO term.
 */
public class Hpo {
    private static final String PREFIX = "hp:";

    /**
     * The numbers representing the ID.
     */
    private int id;

    public Hpo(String id) throws InvalidStringFormatException {
        this.id = retrieveIdNumbers(id);
    }

    public Hpo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Retrieves the ID as a formatted {@link String} including 0's.
     * @return a {@link String} containing a formatted HPO ID.
     */
    public String getFormattedId() {
        String idString = Integer.toString(id);
        return PREFIX + ("0000000" + idString).substring(idString.length());
    }

    /**
     * Converts a {@link String} defining the HPO id into the actual {@code int} containing the ID only.
     * @param hpoTerm a {@link String} containing the HPO id (with prefix)
     * @return an {@code int} with the HPO id without prefix (if present)
     * @throws InvalidStringFormatException if {@code hpoTerm} does not adhere to the regex: ^(hp:)?([0-9]{7})$
     */
    private int retrieveIdNumbers(String hpoTerm) throws InvalidStringFormatException {
        Matcher m = Pattern.compile("^(hp:)?([0-9]{7})$").matcher(hpoTerm);
        if(m.matches()) {
            return Integer.parseInt(m.group(2));
        } else {
            throw new InvalidStringFormatException(hpoTerm + " does not adhere the required format: ^(hp:)?([0-9]{7})$");
        }
    }
}
