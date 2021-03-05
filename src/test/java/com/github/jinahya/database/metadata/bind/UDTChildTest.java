package com.github.jinahya.database.metadata.bind;

abstract class UDTChildTest<T extends UDTChild> extends MetadataTypeTest<T> {

    UDTChildTest(final Class<T> typeClass) {
        super(typeClass);
    }
}