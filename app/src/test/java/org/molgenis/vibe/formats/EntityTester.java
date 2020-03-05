package org.molgenis.vibe.formats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

public class EntityTester {
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
            return "prefix:";
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
    public void invalidIdWithoutPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("a") );
    }

    @Test
    public void invalidIdWithPrefix() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("prefix:a") );
    }

    @Test
    public void invalidPrefixWithValidId() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("invalid:1") );
    }

    @Test
    public void invalidPrefixWithInValidId() {
        Assertions.assertThrows(InvalidStringFormatException.class, () -> new EntityImpl("invalid:a") );
    }

    @Test
    public void invalidUri() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityImpl(URI.create("https://test2.com/path/1")) );
    }

    @Test
    public void uriMissingId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityImpl(URI.create("https://test.com/path/")) );
    }

    @Test
    public void testIdWithName() {
        Entity entity = new EntityImpl("prefix:0", validName);
        Assertions.assertEquals(validName, entity.getName());
    }

    @Test
    public void testUriWithName() {
        Entity entity = new EntityImpl(URI.create("https://test.com/path/0"), validName);
        Assertions.assertEquals(validName, entity.getName());
    }

    @Test
    public void testIdWithNameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new EntityImpl("prefix:0", null) );
    }

    @Test
    public void testUriWithNameNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new EntityImpl(URI.create("https://test.com/path/0"), null) );
    }
}
