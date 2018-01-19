package org.molgenis.vibe.formats;

import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hpo {
    private static final String PREFIX = "hp:";

    private int id;

//    private String name;

    public Hpo(String id) throws InvalidStringFormatException {
        this.id = retrieveIdNumbers(id);
    }

    public Hpo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFormattedId() {
        return PREFIX + Integer.toString(id);
    }

    /**
     * Converts a {@link String} defining the HPO id into the actual {@code int} containing the ID only.
     * @param hpoTerm a {@link String} containing the HPO id (with prefix)
     * @return an {@code int} with the HPO id without prefix (if present)
     * @throws InvalidStringFormatException if {@code hpoTerm} did not adhere to the regex: ^(hpo:)?([0-9]+)$
     */
    private int retrieveIdNumbers(String hpoTerm) throws InvalidStringFormatException {
        Matcher m = Pattern.compile("^(hpo:)?([0-9]+)$").matcher(hpoTerm);
        if(m.matches()) {
            return Integer.parseInt(m.group(2));
        } else {
            throw new InvalidStringFormatException(hpoTerm + " does not adhere the required format: ^(hpo:)?([0-9]+)$");
        }
    }
}
