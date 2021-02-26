package com.github.jinahya.database.metadata.bind;

import static java.util.Objects.requireNonNull;

abstract class MetadataTypeTest<T extends MetadataType> {

    protected MetadataTypeTest(final Class<T> typeClass) {
        this.typeClass = requireNonNull(typeClass, "typeClass is null");
    }

    protected final Class<T> typeClass;
}