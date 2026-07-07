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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

/**
 * A test class for binding a remote database.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
@EnabledIfSystemProperty(named = AbstractExternalIT.PROPERTY_NAME_URL,
                         matches = AbstractExternalIT.PROPERTY_EXPRESSION_NON_BLANK)
@EnabledIfSystemProperty(named = AbstractExternalIT.PROPERTY_NAME_USER,
                         matches = AbstractExternalIT.PROPERTY_EXPRESSION_NON_BLANK)
@EnabledIfSystemProperty(named = AbstractExternalIT.PROPERTY_NAME_PASSWORD,
                         matches = AbstractExternalIT.PROPERTY_EXPRESSION_NON_BLANK)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public abstract class AbstractExternalIT {

    static final String PROPERTY_NAME_URL = "url";

    static final String PROPERTY_NAME_USER = "user";

    static final String PROPERTY_NAME_PASSWORD = "password";

    static final String PROPERTY_EXPRESSION_NON_BLANK = ".*\\S.*";

    // -----------------------------------------------------------------------------------------------------------------
    String url() {
        return System.getProperty(PROPERTY_NAME_URL);
    }

    String user() {
        return System.getProperty(PROPERTY_NAME_USER);
    }

    String password() {
        return System.getProperty(PROPERTY_NAME_PASSWORD);
    }

    private void withContext(final ContextConsumer action) throws SQLException {
        __JavaSqlTestUtils.applyConnection(url(), user(), password(), connection -> {
            try {
                return Context_Test_Utils.applyContext(connection, context -> {
                    action.accept(context);
                    return null;
                });
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    private static void write(final Context context, final String rootElementName, final String itemElementName,
                              final List<?> values)
            throws SQLException {
        final var fileName = Context_Test_Utils.artifactFileNamePrefix(context) + "-" + rootElementName;
        final var xml = Path.of("target", fileName + ".xml");
        final var json = Path.of("target", fileName + ".json");
        __JakartaXmlBinding_Test_Utils.write(rootElementName, itemElementName, values, xml);
        __JakartaJsonBinding_Test_Utils.write(values, json);
        log.info("wrote {} {} to {} and {}", values.size(), rootElementName, xml, json);
    }

    private static void writeOrLog(final Context context, final String rootElementName, final String itemElementName,
                                   final ContextListSupplier supplier) {
        try {
            write(context, rootElementName, itemElementName, supplier.get(context));
        } catch (final SQLException sqle) {
            log.warn("failed to retrieve {}", rootElementName, sqle);
        }
    }

    @FunctionalInterface
    private interface ContextConsumer {

        void accept(Context context);
    }

    @FunctionalInterface
    private interface ContextListSupplier {

        List<?> get(Context context) throws SQLException;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void catalogs() throws SQLException {
        withContext(context -> writeOrLog(context, "catalogs", "catalog", Context::getCatalogs));
    }

    @Test
    void attributes() throws SQLException {
        withContext(context -> writeOrLog(context, "attributes", "attribute", Context::getAllAttributes));
    }

    @Test
    void clientInfoProperties() throws SQLException {
        withContext(context -> writeOrLog(context, "clientInfoProperties", "clientInfoProperty",
                                          Context::getClientInfoProperties));
    }

    @Test
    void columns() throws SQLException {
        withContext(context -> writeOrLog(context, "columns", "column", Context::getAllColumns));
    }

    @Test
    void functions() throws SQLException {
        withContext(context -> writeOrLog(context, "functions", "function", Context::getAllFunctions));
    }

    @Test
    void functionColumns() throws SQLException {
        withContext(context -> writeOrLog(context, "functionColumns", "functionColumn",
                                          Context::getAllFunctionColumns));
    }

    @Test
    void numericFunctions() throws SQLException {
        withContext(context -> writeOrLog(context, "numericFunctions", "numericFunction",
                                          Context::getNumericFunctions));
    }

    @Test
    void procedures() throws SQLException {
        withContext(context -> writeOrLog(context, "procedures", "procedure", Context::getAllProcedures));
    }

    @Test
    void procedureColumns() throws SQLException {
        withContext(context -> writeOrLog(context, "procedureColumns", "procedureColumn",
                                          Context::getAllProcedureColumns));
    }

    @Test
    void pseudoColumns() throws SQLException {
        withContext(context -> writeOrLog(context, "pseudoColumns", "pseudoColumn", Context::getAllPseudoColumns));
    }

    @Test
    void schemas() throws SQLException {
        withContext(context -> writeOrLog(context, "schemas", "schema", Context::getSchemas));
    }

    @Test
    void sqlKeywords() throws SQLException {
        withContext(context -> writeOrLog(context, "sqlKeywords", "sqlKeyword", Context::getSQLKeywords));
    }

    @Test
    void stringFunctions() throws SQLException {
        withContext(context -> writeOrLog(context, "stringFunctions", "stringFunction",
                                          Context::getStringFunctions));
    }

    @Test
    void superTables() throws SQLException {
        withContext(context -> writeOrLog(context, "superTables", "superTable", Context::getAllSuperTables));
    }

    @Test
    void superTypes() throws SQLException {
        withContext(context -> writeOrLog(context, "superTypes", "superType", Context::getAllSuperTypes));
    }

    @Test
    void systemFunctions() throws SQLException {
        withContext(context -> writeOrLog(context, "systemFunctions", "systemFunction",
                                          Context::getSystemFunctions));
    }

    @Test
    void tables() throws SQLException {
        withContext(context -> writeOrLog(context, "tables", "table", Context::getAllTables));
    }

    @Test
    void tablePrivileges() throws SQLException {
        withContext(context -> writeOrLog(context, "tablePrivileges", "tablePrivilege",
                                          Context::getAllTablePrivileges));
    }

    @Test
    void tableTypes() throws SQLException {
        withContext(context -> writeOrLog(context, "tableTypes", "tableType", Context::getTableTypes));
    }

    @Test
    void timeDateFunctions() throws SQLException {
        withContext(context -> writeOrLog(context, "timeDateFunctions", "timeDateFunction",
                                          Context::getTimeDateFunctions));
    }

    @Test
    void typeInfo() throws SQLException {
        withContext(context -> writeOrLog(context, "typeInfo", "typeInfo", Context::getTypeInfo));
    }

    @Test
    void udts() throws SQLException {
        withContext(context -> writeOrLog(context, "udts", "udt", Context::getAllUDTs));
    }
}
