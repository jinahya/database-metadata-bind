package com.github.jinahya.database.metadata.bind;

final class _Assertions {

    static SuperTypeAssert assertType(final SuperType actual) {
        return new SuperTypeAssert(actual);
    }

    private _Assertions() {
        throw new AssertionError("instantiation is not allowed");
    }
}
