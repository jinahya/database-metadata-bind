package com.github.jinahya.database.metadata.bind;

abstract class TableKeyTest<T extends TableKey> extends MetadataTypeTest<T> {

    TableKeyTest(final Class<T> typeClass) {
        super(typeClass);
    }
}