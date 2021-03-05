package com.github.jinahya.database.metadata.bind;

abstract class PrivilegeTest<T extends Privilege> extends TableChildTest<T> {

    PrivilegeTest(final Class<T> typeClass) {
        super(typeClass);
    }
}