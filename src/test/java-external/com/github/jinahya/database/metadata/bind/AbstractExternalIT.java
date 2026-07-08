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
                action.accept(Context.newInstance(connection));
                return null;
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    private static void write(final Context context, final String rootElementName, final List<?> values)
            throws Exception {
        final var fileName = Context_Test_Utils.artifactFileNamePrefix(context) + "-" + rootElementName;
        final var xml = Path.of("target", fileName + ".xml");
        final var json = Path.of("target", fileName + ".json");
        final var types = values.stream().map(MetadataType.class::cast).toList();
        __JakartaXmlBinding_Test_Utils.marshal(types, xml);
        __JakartaJsonBinding_Test_Utils.write(types, json);
        log.info("wrote {} {} to {} and {}", values.size(), rootElementName, xml, json);
    }

    @FunctionalInterface
    private interface ContextConsumer {

        void accept(Context context);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void walkthrough() throws SQLException {
        withContext(c -> ContextMetadataWalkthrough.walk(c, (rootElementName, itemElementName, values) -> {
            write(c, rootElementName, values);
        }));
    }
}
