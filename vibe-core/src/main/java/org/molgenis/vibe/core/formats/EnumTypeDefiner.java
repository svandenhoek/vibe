package org.molgenis.vibe.core.formats;

public interface EnumTypeDefiner {
    String getId();

    /**
     * Retrieves {@link Enum} type based on a given {@link String} (using {@link #getId()} for comparison). Ignores case
     * during comparison. Any {@code null} values (either as parameter or from the {@link Enum} types used for
     * comparison) will be ignored.
     * @param id the name that is linked to a specific {@link Enum} type
     * @param type the {@link Enum} class for which the the type should be retrieved
     * @return the {@link Enum} type that matched with {@code id}
     * @throws EnumConstantNotPresentException if {@link Enum} type does not exist (no match was found) or the {@code id}
     * is {@code null}
     * @throws IllegalArgumentException if {@code type} is null
     */
    static <T extends Enum&EnumTypeDefiner> T retrieve(String id, Class<T> type) throws EnumConstantNotPresentException, IllegalArgumentException {
        // Type should not be null as otherwise no comparison can be made.
        if(type == null) {
            throw new IllegalArgumentException("EnumTypeDefiner should not receive null as comparison Enum class.");
        }

        // Skips checking when an ID is null (if Enum constants are present with null ID, these will not match).
        if(id != null) {
            // Goes through all enym types.
            for (T constant : type.getEnumConstants()) {
                // Skips Enum types with id null.
                if(constant.getId() == null) {
                    continue;
                }
                // Checks if input name is equal to the enum type id field. If so, returns enum type.
                if (id.equalsIgnoreCase(constant.getId())) {
                    return constant;
                }
            }
        }

        // Throws an Exception if looking for an enum type that does not exist.
        throw new EnumConstantNotPresentException(type, id);
    }
}