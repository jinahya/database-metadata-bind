package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2026 Jinahya, Inc.
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

import org.junit.jupiter.api.Test;

class Context_ComparingInJdbcOrder_Test {

    @Test
    void acceptEachEmitsValuesInSpecifiedOrder() throws Throwable {
        __JavaSqlTestUtils.acceptConnection("jdbc:h2:mem:comparing;DB_CLOSE_DELAY=-1", connection -> {
            try (var statement = connection.createStatement()) {
                Context_ComparingInJdbcOrder_Test_Utils.preparePortedKeyTables(statement);
            }
            Context_ComparingInJdbcOrder_Test_Utils.assertComparingInJdbcOrder(
                    Context.newInstance(connection),
                    getClass().getSimpleName()
            );
        });
    }
}
