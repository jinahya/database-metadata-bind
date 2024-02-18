package com.github.jinahya.database.metadata.bind;

import org.assertj.core.api.AbstractAssert;

abstract class AbstractMetadataTypeAssert<
        SELF extends AbstractMetadataTypeAssert<SELF, ACTUAL>, ACTUAL extends AbstractMetadataType>
        extends AbstractAssert<SELF, ACTUAL>
        implements MetadataTypeAssert<SELF, ACTUAL> {

    AbstractMetadataTypeAssert(final ACTUAL actual, final Class<SELF> selfType) {
        super(actual, selfType);
    }
}