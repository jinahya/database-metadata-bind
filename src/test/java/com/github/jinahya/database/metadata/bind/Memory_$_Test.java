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

import io.vavr.CheckedFunction1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * An abstract test class for in-memory databases.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@Slf4j
abstract class Memory_$_Test {

    /**
     * Returns a connection
     *
     * @return a connection.
     * @throws SQLException if a database error occurs.
     */
    abstract Connection connect() throws SQLException;

    // ------------------------------------------------------------------------------------------------------ connection
    <R> R applyConnection(final CheckedFunction1<? super Connection, ? extends R> function) throws Throwable {
        return __JavaSqlTestUtils.applyConnection(this::connect, function);
    }

    // --------------------------------------------------------------------------------------------------------- context
    <R> R applyContext(final CheckedFunction1<? super Context, ? extends R> function) throws Throwable {
        return applyConnection(c -> {
            return function.apply(Context.from(c));
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void test() throws Throwable {
        applyConnection(connection -> {
            // Give the walkthrough a real foreign key to find. Without one, getCrossReference is exercised only for
            // the empty case and its row binding stays unverified. See _TODOS.asciidoc P-038.
            try (var statement = connection.createStatement()) {
                Context_ComparingInJdbcOrder_Test_Utils.preparePortedKeyTables(statement);
            } catch (final SQLException sqle) {
                log.warn("failed to prepare foreign-key tables; crossReference rows will not be exercised", sqle);
            }
            final var c = Context.from(connection);
            // walk all binding methods and write each metadata collection to target/<db>-<name>.xml and .json
            ContextMetadataWalkthrough.walk(c, (rootElementName, itemElementName, values) -> {
                final var types = values.stream().map(MetadataType.class::cast).toList();
                final var fileName = Context_Test_Utils.artifactFileNamePrefix(c) + "-" + rootElementName;
                __JakartaXmlBinding_Test_Utils.marshal(types, Path.of("target", fileName + ".xml"));
                __JakartaJsonBinding_Test_Utils.write(types, Path.of("target", fileName + ".json"));
            });
            return null;
        });
    }
}
