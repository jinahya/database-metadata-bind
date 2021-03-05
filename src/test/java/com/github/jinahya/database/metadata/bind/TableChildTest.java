package com.github.jinahya.database.metadata.bind;

abstract class TableChildTest<T extends TableChild> extends AbstractChildTest<T, Table> {

    TableChildTest(final Class<T> typeClass) {
        super(typeClass, Table.class);
    }
}