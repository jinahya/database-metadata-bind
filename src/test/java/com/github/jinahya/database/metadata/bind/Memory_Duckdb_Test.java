package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Tests for DuckDB.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class Memory_Duckdb_Test
        extends Memory_$_Test {

    private static final String CONNECTION_URL = "jdbc:duckdb:";

    @Override
    protected Connection connect() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Disabled for DuckDB: its {@link java.sql.DatabaseMetaData#getTypeInfo()} does <em>not</em> return rows ordered by
     * {@code DATA_TYPE}, contrary to the JDBC specification. DuckDB emits them in an internal type order
     * ({@code BOOLEAN(-7)}, {@code TINYINT(-6)}, {@code SMALLINT(5)}, {@code INTEGER(4)}, {@code BIGINT(-5)},
     * &hellip;), so the shared {@code isSortedAccordingTo(TypeInfo.comparingInSpecifiedOrder())} assertion fails. This
     * is a genuine driver deviation, not a binding defect.
     */
    @Disabled("DuckDB getTypeInfo() is not ordered by DATA_TYPE (JDBC spec deviation)")
    @Test
    @Override
    void test() throws SQLException {
        super.test();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden for DuckDB: it does not implement
     * {@link java.sql.DatabaseMetaData#getColumnPrivileges(String, String, String, String) getColumnPrivileges(...)}
     * and throws {@link SQLFeatureNotSupportedException}. The shared implementation invokes it per table without
     * tolerating that, so this override swallows the (expected) unsupported-feature exception.
     */
    @Test
    @Override
    void tables() throws SQLException, IOException {
        try {
            super.tables();
        } catch (final SQLFeatureNotSupportedException sqlfnse) {
            log.warn("not supported by DuckDB", sqlfnse);
        }
    }
}
