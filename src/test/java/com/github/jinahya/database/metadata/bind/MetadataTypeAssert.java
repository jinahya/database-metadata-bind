package com.github.jinahya.database.metadata.bind;

import org.assertj.core.api.Assert;

interface MetadataTypeAssert<SELF extends MetadataTypeAssert<SELF, ACTUAL>, ACTUAL extends MetadataType>
        extends Assert<SELF, ACTUAL> {

}