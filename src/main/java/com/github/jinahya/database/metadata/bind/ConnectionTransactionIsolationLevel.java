package com.github.jinahya.database.metadata.bind;

import java.sql.Connection;

/**
 * Constants of transaction isolation level defined in {@link Connection} class.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
enum ConnectionTransactionIsolationLevel implements IntFieldEnum<ConnectionTransactionIsolationLevel> {

    /**
     * Constant for {@link Connection#TRANSACTION_NONE}({@value java.sql.Connection#TRANSACTION_NONE}).
     */
    TRANSACTION_NONE(Connection.TRANSACTION_NONE), // 0

    /**
     * Constant for {@link Connection#TRANSACTION_READ_UNCOMMITTED}({@value java.sql.Connection#TRANSACTION_READ_UNCOMMITTED}).
     */
    TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED), // 1

    /**
     * Constant for {@link Connection#TRANSACTION_READ_COMMITTED}({@value java.sql.Connection#TRANSACTION_READ_COMMITTED}).
     */
    TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED), // 2

    /**
     * Constant for {@link Connection#TRANSACTION_REPEATABLE_READ}({@value java.sql.Connection#TRANSACTION_REPEATABLE_READ}).
     */
    TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ), // 4

    /**
     * Constant for {@link Connection#TRANSACTION_SERIALIZABLE}({@value java.sql.Connection#TRANSACTION_SERIALIZABLE}).
     */
    TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE); // 8

    /**
     * Returns the value whose {@link #getRawValue() rawVaue} matches to specified value.
     *
     * @param rawValue the {@code rawValue} to match.
     * @return the value of {@code rawValue}.
     */
    public static ConnectionTransactionIsolationLevel valueOfRawValue(final int rawValue) {
        return IntFieldEnums.valueOfRawValue(ConnectionTransactionIsolationLevel.class, rawValue);
    }

    /**
     * Creates a new instance with specified raw value.
     *
     * @param rawValue the raw value.
     */
    ConnectionTransactionIsolationLevel(final int rawValue) {
        this.rawValue = rawValue;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getRawValue() {
        return rawValue;
    }

    /**
     * The raw value of this constant.
     */
    private final int rawValue;
}
