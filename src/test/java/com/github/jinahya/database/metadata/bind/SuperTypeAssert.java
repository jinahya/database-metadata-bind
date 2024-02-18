package com.github.jinahya.database.metadata.bind;

import static org.assertj.core.api.Assertions.assertThat;

class SuperTypeAssert
        extends AbstractMetadataTypeAssert<SuperTypeAssert, SuperType> {

    SuperTypeAssert(final SuperType actual) {
        super(actual, SuperTypeAssert.class);
    }

    SuperTypeAssert isOf(final UDT udt) {
        return isNotNull()
                .satisfies(a -> {
                    assertThat(a.getTypeCat()).isEqualTo(udt.getTypeCat());
                    assertThat(a.getTypeSchem()).isEqualTo(udt.getTypeSchem());
                    assertThat(a.getTypeName()).isEqualTo(udt.getTypeName());
                });
    }
}