/**
 * Defines classes for binding results of methods defined in {@link java.sql.DatabaseMetaData}.
 * <p>
 * {@snippet :
 * try (java.sql.Connection connection = connect()) {
 *     var metadata = connection.getMetaData();
 *     var context = new Context(metadata);
 *     var catalogs = context.getCatalogs();
 *     var schemas = context.getSchemas();
 *     var tables = context.getTables(null, null, "%", null);
 * }
 *}
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see com.github.jinahya.database.metadata.bind.Context
 * @see com.github.jinahya.database.metadata.bind.MetadataType
 * @see <a
 * href="https://docs.oracle.com/en/java/javase/25/docs/api/java.sql/java/sql/DatabaseMetaData.html">java.sql.DatabaseMetaData</a>
 */
@org.jspecify.annotations.NullMarked
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
