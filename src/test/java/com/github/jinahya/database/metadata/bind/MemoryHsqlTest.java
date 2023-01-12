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

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

/**
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@org.junit.jupiter.api.condition.EnabledIfSystemProperty(named = "memory", matches = "true")
@Slf4j
class MemoryHsqlTest extends MemoryTest {

    private static final String DRIVER_NAME = "org.hsqldb.jdbc.JDBCDriver";

    private static final Class<?> DRIVER_CLASS;

    static {
        try {
            DRIVER_CLASS = Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException cnfe) {
            throw new InstantiationError(cnfe.getMessage());
        }
    }

    private static final String CONNECTION_URL = "jdbc:hsqldb:mem:test";

    @Override
    protected Connection connect() throws SQLException {
        return getConnection(CONNECTION_URL);
    }

    @Override
    Context context(Connection connection) throws SQLException {
        return super.context(connection)
                ;
    }

    @Disabled("feature not supported")
    @Override
    void getSchemas__() throws Exception {
        super.getSchemas__();
    }

    @Disabled("feature not supported")
    @Override
    void getTables__() throws Exception {
        super.getTables__();
    }

    @Disabled("feature not supported")
    @Override
    void getPseudoColumns__() throws Exception {
        super.getPseudoColumns__();
    }
}
