package com.github.jinahya.database.metadata.bind;

abstract class SchemaChildTest<T extends SchemaChild> extends MetadataTypeTest<T> {

    SchemaChildTest(final Class<T> typeClass) {
        super(typeClass);
    }
}