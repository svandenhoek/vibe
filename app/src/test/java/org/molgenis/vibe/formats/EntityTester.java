package org.molgenis.vibe.formats;

import org.junit.Test;
import org.molgenis.vibe.exceptions.InvalidStringFormatException;

import java.net.URI;

public class EntityTester {
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

    @Test(expected = InvalidStringFormatException.class)
    public void invalidIdWithoutPrefix() {
        new EntityImpl("a");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void invalidIdWithPrefix() {
        new EntityImpl("prefix:a");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void invalidPrefixWithValidId() {
        new EntityImpl("invalid:1");
    }

    @Test(expected = InvalidStringFormatException.class)
    public void invalidPrefixWithInValidId() {
        new EntityImpl("invalid:a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidUri() {
        new EntityImpl(URI.create("https://test2.com/path/1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void uriMissingId() {
        new EntityImpl(URI.create("https://test.com/path/"));
    }
}
