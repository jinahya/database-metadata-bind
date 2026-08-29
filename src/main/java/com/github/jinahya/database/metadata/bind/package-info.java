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
 * <p>
 * Binding types whose corresponding {@link java.sql.DatabaseMetaData} method specifies a result order provide
 * package-private {@code comparingInJdbcOrder(...)} factories that model the explicit comparison keys exposed by the
 * result set and use the supplied {@link com.github.jinahya.database.metadata.bind.Context} for database-reported
 * {@code null} ordering. Two properties of these comparators are worth noting:
 * <ul>
 *   <li>String key values are compared with the supplied comparator after {@code null} values have been ordered with
 *       {@link com.github.jinahya.database.metadata.bind.ContextUtils#withDatabaseNullOrdering(com.github.jinahya.database.metadata.bind.Context,
 *       java.util.Comparator, com.github.jinahya.database.metadata.bind.ContextConstants.SortDirection)}. The supplied
 *       comparator is therefore responsible for non-{@code null} string collation only. The library imposes no
 *       case/collation policy of its own; callers choose it via the {@code comparator} (e.g. {@link java.text.Collator}
 *       for locale-aware ordering).</li>
 *   <li>They impose a <em>partial</em> order: distinct rows may compare equal (the JDBC-specified order does not key on
 *       every bound column). They are intended for sorting {@link java.util.List}s, not for use as
 *       {@link java.util.TreeSet}/{@link java.util.TreeMap} keys, where equal-comparing rows would be dropped.</li>
 * </ul>
 * <h2>Jakarta XML Binding</h2>
 * <p>
 * XML binding is field-based and namespace-qualified. Each concrete metadata type supplies its own
 * {@code @XmlRootElement} name, and the package namespace is
 * {@link com.github.jinahya.database.metadata.bind.JakartaXmlBindingConstants#NAMESPACE_URI}. Lists are represented by
 * {@link com.github.jinahya.database.metadata.bind.MetadataTypeWrapper} because Jakarta XML Binding cannot marshal a
 * bare {@link java.util.List} as a document.
 * </p>
 * <p>
 * A {@link jakarta.xml.bind.JAXBContext} can be bootstrapped by package name (this package ships
 * {@code src/main/resources/com/github/jinahya/database/metadata/bind/jaxb.index} for that purpose). The public wrapper
 * exposes {@link com.github.jinahya.database.metadata.bind.MetadataTypeWrapper#ROOT_ELEMENT_NAME}, a public no-argument
 * constructor, and a modifiable {@link com.github.jinahya.database.metadata.bind.MetadataTypeWrapper#getElements()}
 * list.
 * </p>
 * {@snippet lang = "java":
 * var context = jakarta.xml.bind.JAXBContext.newInstance(MetadataType.class.getPackageName());
 * var wrapper = new MetadataTypeWrapper<Schema>();
 * wrapper.getElements().addAll(schemas);
 *
 * var marshaller = context.createMarshaller();
 * marshaller.marshal(wrapper, path.toFile());
 *}
 * <h2>Jakarta JSON Binding</h2>
 * <p>
 * JSON binding is also field-based. The package-level
 * {@link jakarta.json.bind.annotation.JsonbVisibility @JsonbVisibility} lets a default
 * {@link jakarta.json.bind.Jsonb} instance serialize and deserialize binding types despite their private fields,
 * non-public setters, and protected no-argument constructors. The visibility strategy type is public only so a Jakarta
 * JSON Binding provider can instantiate it reflectively from the package annotation; application code normally does not
 * need to reference it directly.
 * </p>
 * <p>
 * Unlike XML binding, Jakarta JSON Binding can write a {@link java.util.List} directly as a JSON array, so no
 * {@link com.github.jinahya.database.metadata.bind.MetadataTypeWrapper} is used.
 * </p>
 * {@snippet lang = "java":
 * try (var jsonb = jakarta.json.bind.JsonbBuilder.create()) {
 *     var json = jsonb.toJson(schemas);
 *     var schema = jsonb.fromJson("{\"tableSchem\":\"PUBLIC\"}", Schema.class);
 * }
 *}
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see com.github.jinahya.database.metadata.bind.Context
 * @see com.github.jinahya.database.metadata.bind.MetadataType
 * @see <a
 * href="https://docs.oracle.com/en/java/javase/25/docs/api/java.sql/java/sql/DatabaseMetaData.html">java.sql.DatabaseMetaData</a>
 */
// ------------------------------------------------------------------------------------------------- Jakarta XML Binding
@jakarta.xml.bind.annotation.XmlAccessorType(jakarta.xml.bind.annotation.XmlAccessType.FIELD)
@jakarta.xml.bind.annotation.XmlSchema(
        namespace = com.github.jinahya.database.metadata.bind.JakartaXmlBindingConstants.NAMESPACE_URI
)
// ------------------------------------------------------------------------------------------------ Jakarta JSON Binding
@jakarta.json.bind.annotation.JsonbVisibility(
        com.github.jinahya.database.metadata.bind.JakartaJsonBindingUtils.FieldAccessVisibilityStrategy.class
)
// ------------------------------------------------------------------------------------------------------------ JSpecify
@org.jspecify.annotations.NullMarked
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
