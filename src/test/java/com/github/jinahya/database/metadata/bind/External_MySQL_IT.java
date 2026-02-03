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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.util.Map;

/**
 * A test class for binding a remote database.
 *
 * @author Jin Kwon &lt;onacit at gmail.com&gt;
 */
//@Disabled
@EnabledIfSystemProperty(named = External_MySQL_IT.PROPERTY_NAME_URL, matches = ".+")
@EnabledIfSystemProperty(named = External_MySQL_IT.PROPERTY_NAME_USER, matches = ".+")
@EnabledIfSystemProperty(named = External_MySQL_IT.PROPERTY_NAME_PASSWORD, matches = ".+")
@Slf4j
class External_MySQL_IT {

    static final String PROPERTY_NAME_URL = "url";

    static final String PROPERTY_NAME_USER = "user";

    static final String PROPERTY_NAME_PASSWORD = "password";

    private static final String PROPERTY_NAME_CATALOG = "catalog";

    @Test
    void columns() throws Exception {
        final var clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        log.debug("loaded: {}", clazz);
        final String url = System.getProperty("url");
        final String user = System.getProperty("user");
        final String password = System.getProperty("password");
        log.info("connecting to {}", url);
        try (var connection = DriverManager.getConnection(url, user, password)) {
            log.info("connected: {}", connection);
            final var context = Context.newInstance(connection);
            final var mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            final var name = "columns.json";
            try (var writer = Files.newBufferedWriter(Paths.get(name), StandardCharsets.UTF_8)) {
                try (var jsonWriter = mapper.writerWithDefaultPrettyPrinter().writeValuesAsArray(writer)) {
                    context.getColumnsAndAcceptEach(null, null, "%", "%", c -> {
                        log.debug("column: {}", c);
                        c.getUnknownColumns().clear();
                        try {
                            jsonWriter.write(c);
                        } catch (final IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    });
                    jsonWriter.flush();
                }
            }
        }
    }

    @Test
    void importedKeys() throws Exception {
        final var clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        log.debug("loaded: {}", clazz);
        final String url = System.getProperty("url");
        final String user = System.getProperty("user");
        final String password = System.getProperty("password");
        log.info("connecting to {}", url);
        try (var connection = DriverManager.getConnection(url, user, password)) {
            log.info("connected: {}", connection);
            final var context = Context.newInstance(connection);
            final var tables = context.getTables((String) null, null, "%", null);
            log.debug("tables.size: {}", tables.size());
            final var mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            for (final var table : tables) {
                final var name = String.valueOf(table.getTableCat()) + '_' + table.getTableSchem() + '_' +
                                 table.getTableName() + "_imported_keys.json";
                try (var writer = Files.newBufferedWriter(Paths.get(name), StandardCharsets.UTF_8)) {
                    try (var jsonWriter = mapper.writerWithDefaultPrettyPrinter().writeValuesAsArray(writer)) {
                        context.getImportedKeysAndAcceptEach(
                                table.getTableCat(), table.getTableSchem(), table.getTableName(),
                                ik -> {
                                    Map<String, Object> cleanMap = mapper.convertValue(ik, new TypeReference<>() {
                                    });
                                    cleanMap.remove("unknownColumns");
                                    try {
                                        jsonWriter.write(cleanMap);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );
                        jsonWriter.flush();
                    }
                }
            }
        }
    }

    @Test
    void exportedKeys() throws Exception {
        final var clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        log.debug("loaded: {}", clazz);
        final String url = System.getProperty("url");
        final String user = System.getProperty("user");
        final String password = System.getProperty("password");
        log.info("connecting to {}", url);
        try (var connection = DriverManager.getConnection(url, user, password)) {
            log.info("connected: {}", connection);
            final var context = Context.newInstance(connection);
            final var tables = context.getTables((String) null, null, "%", null);
            log.debug("tables.size: {}", tables.size());
            final var mapper = new ObjectMapper();
            for (final var table : tables) {
                final var name = String.valueOf(table.getTableCat())
                                 + '_' + table.getTableSchem()
                                 + '_' + table.getTableName() +
                                 "_exported_keys.json";
                try (var writer = Files.newBufferedWriter(Paths.get(name), StandardCharsets.UTF_8)) {
                    try (var jsonWriter = mapper.writerWithDefaultPrettyPrinter().writeValuesAsArray(writer)) {
                        context.getExportedKeysAndAcceptEach(
                                table.getTableCat(),
                                table.getTableSchem(),
                                table.getTableName(),
                                ek -> {
                                    log.debug("exportedKey: {}", ek);
                                    ek.getUnknownColumns().clear();
                                    try {
                                        jsonWriter.write(ek);
                                    } catch (final IOException ioe) {
                                        throw new RuntimeException(ioe);
                                    }
                                });
                        jsonWriter.flush();
                    }
                }
            }
        }
    }

//    @org.junit.jupiter.api.Disabled
////    @EnabledIfSystemProperty(named = PROPERTY_NAME_URL, matches = ".+")
////    @EnabledIfSystemProperty(named = PROPERTY_NAME_USER, matches = ".+")
////    @EnabledIfSystemProperty(named = PROPERTY_NAME_PASSWORD, matches = ".+")
//    @Test
//    void crossReferences() throws Exception {
//        final var clazz = Class.forName("com.mysql.cj.jdbc.Driver");
//        log.debug("loaded: {}", clazz);
//        final String url = System.getProperty("url");
//        final String user = System.getProperty("user");
//        final String password = System.getProperty("password");
//        log.info("connecting to {}", url);
//        try (var connection = DriverManager.getConnection(url, user, password)) {
//            log.info("connected: {}", connection);
//            final var context = Context.newInstance(connection);
//            final var schemaIdsAndTableLists =
//                    context.getTables((String) null, null, "%", null).stream()
//                            .collect(Collectors.groupingBy(t -> SchemaId.of(t.getTableSchem(), t.getTableCat())));
//            final var mapper = new ObjectMapper();
//            final var factory = mapper.getFactory();
//            for (final var entry : schemaIdsAndTableLists.entrySet()) {
//                final var schemaId = entry.getKey();
//                log.info("schemaId: {}", schemaId);
//                final var tableList = entry.getValue();
//                log.info("tableList.size: {}", tableList.size());
//                final var name = schemaId.getTableCatalog() + '_' + schemaId.getTableSchem() + "_cross_references.json";
//                try (final var stream = new FileOutputStream(name)) {
//                    final var generator = factory.createGenerator(stream);
//                    generator.writeStartArray();
//                    for (final var t1 : tableList) {
//                        for (final var t2 : tableList) {
//                            context.getCrossReferenceAndAcceptEach(
//                                    t1.getTableCat(), t1.getTableCat(), t1.getTableName(),
//                                    t2.getTableCat(), t2.getTableSchem(), t2.getTableName(), cr -> {
////                                        log.debug("crossReference: {}", cr);
//                                        try {
//                                            generator.writeObject(cr);
//                                        } catch (final IOException ioe) {
//                                            throw new RuntimeException(ioe);
//                                        }
//                                    });
//                        }
//                    }
//                    generator.writeEndArray();
//                }
//            }
//        }
//    }
}
