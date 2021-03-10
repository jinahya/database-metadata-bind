package com.github.jinahya.database.metadata.bind;

import java.sql.Connection;

enum ConnectionTransactionIsolationLevel implements IntFieldEnum<ConnectionTransactionIsolationLevel> {

    TRANSACTION_NONE(Connection.TRANSACTION_NONE), // 0

    TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED), // 1

    TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED), // 2

    TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ), // 4

    TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE); // 8

    public static ConnectionTransactionIsolationLevel valueOfRawValue(final int rawValue) {
        return IntFieldEnums.valueOfRawValue(ConnectionTransactionIsolationLevel.class, rawValue);
    }

    ConnectionTransactionIsolationLevel(final int rawValue) {
        this.rawValue = rawValue;
    }

    @Override
    public int getRawValue() {
        return rawValue;
    }

    private final int rawValue;
}
