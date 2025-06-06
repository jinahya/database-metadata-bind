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

@Slf4j
final class DatabaseProductNames {

    static final String APACHE_DERBY = "Apache Derby";

    static final String H2 = "H2";

    static final String HSQL_DATABASE_ENGINE = "HSQL Database Engine";

    static final String MARIA_DB = "MariaDB";

    static final String MICROSOFT_SQL_SERVER = "Microsoft SQL Server";

    static final String MY_SQL = "MySQL";

    static final String ORACLE = "Oracle";

    static final String POSTGRE_SQL = "PostgreSQL";

    static final String SQ_LITE = "SQLite";

    private DatabaseProductNames() {
        throw new AssertionError("instantiation is not allowed");
    }
}
