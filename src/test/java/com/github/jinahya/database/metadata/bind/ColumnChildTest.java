package com.github.jinahya.database.metadata.bind;

abstract class ColumnChildTest<T extends ColumnChild> extends MetadataTypeTest<T> {

    ColumnChildTest(final Class<T> typeClass) {
        super(typeClass);
    }
}