package org.molgenis.vibe.formats;

public interface EnumTypeDefiner {
    String getId();

    /**
     * Retrieves {@link Enum} type based on a given {@link String} (retrieved through {@link #getId()}
     * @param id the name that is linked to a specific {@link Enum} type
     * @param type the {@link Enum} class
     * @return an {@link Enum} type from the {@link Class} {@code type} or {@code null} if no match was found
     */
    static <T extends Enum&EnumTypeDefiner> T retrieve(String id, Class<T> type) {
        // Goes through all enym types.
        for(T constant : type.getEnumConstants()) {
            // Checks if input name is equal to the enum type id field. If so, returns enum type.
            if(id.equals(constant.getId())) {
                return constant;
            }
        }

        // Return null if no match was found.
        return null;
    }
}