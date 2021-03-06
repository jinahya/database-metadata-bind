package com.github.jinahya.database.metadata.bind;

abstract class PrivilegeTest<T extends Privilege<P>, P extends MetadataType> extends AbstractChildTest<T, P> {

    PrivilegeTest(final Class<T> typeClass, final Class<P> parentClass) {
        super(typeClass, parentClass);
    }
}