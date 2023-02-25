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

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

/**
 * A class for testing against H2.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Slf4j
class Memory_H2_Test extends Memory_$_Test {

    static final String DATABASE_PRODUCT_NAME = "H2";

    private static final String DRIVER_NAME = "org.h2.Driver";

    private static final Class<?> DRIVER_CLASS;

    static {
        try {
            DRIVER_CLASS = Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException cnfe) {
            throw new InstantiationError(cnfe.getMessage());
        }
    }

    private static final String CONNECTION_URL = "jdbc:h2:mem:test"; //;DB_CLOSE_DELAY=-1";

    @Override
    protected Connection connect() throws SQLException {
        return getConnection(CONNECTION_URL);
    }
}
