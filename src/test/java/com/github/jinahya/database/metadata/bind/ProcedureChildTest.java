package com.github.jinahya.database.metadata.bind;

abstract class ProcedureChildTest<T extends ProcedureChild> extends AbstractChildTest<T, Procedure> {

    ProcedureChildTest(final Class<T> typeClass) {
        super(typeClass, Procedure.class);
    }
}