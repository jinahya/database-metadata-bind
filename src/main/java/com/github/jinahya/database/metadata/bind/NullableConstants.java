package com.github.jinahya.database.metadata.bind;

final class NullableConstants {

    static final int NO_NULLS = 0;

    static final int NULLABLE = 1;

    static final int NULLABLE_UNKNOWN = 2;

    private NullableConstants() {
        throw new AssertionError("instantiation is not allowed");
    }
}
