package com.github.jinahya.database.metadata.bind;

abstract class FunctionChildTest<T extends FunctionChild>
        extends AbstractChildTest<T, Function> {

    FunctionChildTest(final Class<T> typeClass) {
        super(typeClass, Function.class);
    }
}