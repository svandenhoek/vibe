package org.molgenis.vibe.core.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.core.exceptions.InvalidStringFormatException;

import java.net.URI;

class EntityTest {
    private static final String validName = "Entity 0";

    private class EntityImpl extends Entity {
        public EntityImpl(String id) {
            super(id);
        }

        public EntityImpl(URI uri) {
            super(uri);
        }

        public EntityImpl(String id, String name) {
            super(id, name);
        }

        public EntityImpl(URI uri, String name) {
            super(uri, name);
        }

        @Override
        protected String getIdPrefix() {
            return "prefix";
        }

        @Override
        protected String getIdRegex() {
            return "^prefix:([0-9]+)$";
        }

        @Override
        protected int getRegexIdGroup() {
            return 1;
        }

        @Override
        protected String getUriPrefix() {
            return "https://test.com/path/";
        }
    }

    @Test
    void invalidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("a") );
    }

    @Test
    void invalidIdWithPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("prefix:a") );
    }

    @Test
    void invalidPrefixWithValidId() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("invalid:1") );
    }

    @Test
    void invalidPrefixWithInValidId() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("invalid:a") );
    }

    @Test
    void invalidUri() {
        URI uri = URI.create("https://test2.com/path/1");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityImpl(uri) );
    }

    @Test
    void uriMissingId() {
        URI uri = URI.create("https://test.com/path/");
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityImpl(uri) );
    }

    @Test
    void testIdWithName() {
        Entity entity = new EntityImpl("prefix:0", validName);
        Assertions.assertEquals(validName, entity.getName());
    }

    @Test
    void testUriWithName() {
        Entity entity = new EntityImpl(URI.create("https://test.com/path/0"), validName);
        Assertions.assertEquals(validName, entity.getName());
    }

    @Test
    void testIdWithNameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new EntityImpl("prefix:0", null) );
    }

    @Test
    void testUriWithNameNull() {
        URI uri = URI.create("https://test.com/path/0");
        Assertions.assertThrows(NullPointerException.class, () -> new EntityImpl(uri, null) );
    }
}
