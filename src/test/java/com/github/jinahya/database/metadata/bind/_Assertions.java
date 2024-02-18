package com.github.jinahya.database.metadata.bind;

final class _Assertions {

    static SuperTypeAssert assertType(final SuperType actual) {
        return new SuperTypeAssert(actual);
    }

    static TableAssert assertType(final Table actual) {
        return new TableAssert(actual);
    }

    private _Assertions() {
        throw new AssertionError("instantiation is not allowed");
    }
}
