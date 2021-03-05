package com.github.jinahya.database.metadata.bind;

import static java.util.Objects.requireNonNull;

abstract class AbstractChildTest<T extends AbstractChild<P>, P extends MetadataType> extends MetadataTypeTest<T> {

    AbstractChildTest(final Class<T> typeClass, final Class<P> parentClass) {
        super(typeClass);
        this.parentClass = requireNonNull(parentClass, "parentClass is null");
    }

    protected final Class<P> parentClass;
}