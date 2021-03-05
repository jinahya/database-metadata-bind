package com.github.jinahya.database.metadata.bind;

abstract class CatalogChildTest<T extends CatalogChild> extends MetadataTypeTest<T> {

    CatalogChildTest(final Class<T> typeClass) {
        super(typeClass);
    }
}

