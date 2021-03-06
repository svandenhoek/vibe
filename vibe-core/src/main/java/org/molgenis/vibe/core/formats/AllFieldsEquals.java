package org.molgenis.vibe.core.formats;

/**
 * A deep equals that compares all fields of a {@link Class}, also ones that are not used by
 * {@link Class#equals(Object)} (for example due to design-reasons, though validity for all fields still needs to be
 * checked in unit-tests to assess everything functions as it should).
 */
public interface AllFieldsEquals {
    boolean allFieldsEquals(Object o);
}
