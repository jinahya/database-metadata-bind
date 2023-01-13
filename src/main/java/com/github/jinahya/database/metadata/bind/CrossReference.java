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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding the result of
 * {@link DatabaseMetaData#getCrossReference(String, String, String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class CrossReference
        implements MetadataType {

    private static final long serialVersionUID = -5343386346721125961L;

    /**
     * Retrieves cross-references for specified tables.
     *
     * @param context      a context.
     * @param parentTable  the parent table.
     * @param foreignTable the foreign table.
     * @param collection   the collection to the results are added.
     * @throws SQLException if a database error occurs.
     */
    public static void get(final Context context, final Table parentTable, final Table foreignTable,
                           final Collection<? super CrossReference> collection)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(parentTable, "parentTable is null");
        Objects.requireNonNull(foreignTable, "foreignTable is null");
        context.getCrossReference(
                parentTable.getTableCat(),
                parentTable.getTableSchem(),
                parentTable.getTableName(),
                foreignTable.getTableCat(),
                foreignTable.getTableSchem(),
                foreignTable.getTableName(),
                collection::add
        );
    }

    /**
     * Retrieves cross-references between tables retrieved with specified arguments.
     *
     * @param context          a context
     * @param catalog          a value for {@code catalog} parameter.
     * @param schemaPattern    a value for {@code schemaPattern} parameter.
     * @param tableNamePattern a value for {@code tableNamePattern} parameter.
     * @param types            a value for {@code types} parameter.
     * @param collection       a collection to which results are added.
     * @param <C>              collection type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     */
    public static <C extends Collection<? super CrossReference>> C getInstances(
            final Context context, final String catalog, final String schemaPattern, final String tableNamePattern,
            final String[] types, final C collection)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        final List<Table> tables = context.getTables(
                catalog, schemaPattern, tableNamePattern, types);
        for (final Table parentTable : tables) {
            for (final Table foreignTable : tables) {
                get(context, parentTable, foreignTable, collection);
            }
        }
        return collection;
    }

    /**
     * Retrieves all cross-references for all tables.
     *
     * @param context    a context.
     * @param collection a collection to which results are added.
     * @param <C>        collection type parameter
     * @return given {@code collection}.
     * @throws SQLException if a database error occurs.
     * @implNote This method invokes
     * {@link #getInstances(Context, String, String, String, String[], Collection) getInstances(context, null, null,
     * "%", null, collection)} and returns the result.
     */
    public static <C extends Collection<? super CrossReference>> C getAllInstances(final Context context,
                                                                                   final C collection)
            throws SQLException {
        return getInstances(context, null, null, "%", null, collection);
    }

    public static final String COLUMN_LABEL_UPDATE_RULE = "UPDATE_RULE";

    public static final String ATTRIBUTE_NAME_UPDATE_RULE = "updateRule";

    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        // no children.
    }

    public Column extractPkColumn() {
        return Column.builder()
                .tableCat(getPktableCat())
                .tableSchem(getPktableSchem())
                .tableName(getPktableName())
                .columnName(getPkcolumnName())
                .build();
    }

    public Column extractFkColumn() {
        return Column.builder()
                .tableCat(getFktableCat())
                .tableSchem(getFktableSchem())
                .tableName(getFktableName())
                .columnName(getFkcolumnName())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel("PKTABLE_CAT")
    private String pktableCat;

    @NullableBySpecification
    @ColumnLabel("PKTABLE_SCHEM")
    private String pktableSchem;

    @ColumnLabel("PKTABLE_NAME")
    private String pktableName;

    @ColumnLabel("PKCOLUMN_NAME")
    private String pkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel("FKTABLE_CAT")
    private String fktableCat;

    @NullableBySpecification
    @ColumnLabel("FKTABLE_NAME")
    private String fktableSchem;

    @ColumnLabel("FKTABLE_NAME")
    private String fktableName;

    @ColumnLabel("FKCOLUMN_NAME")
    private String fkcolumnName;

    // -----------------------------------------------------------------------------------------------------------------
    @ColumnLabel("FKCOLUMN_NAME")
    private int keySeq;

    @ColumnLabel("UPDATE_RULE")
    private int updateRule;

    @ColumnLabel("DELETE_RULE")
    private int deleteRule;

    @NullableBySpecification
    @ColumnLabel("FK_NAME")
    private String fkName;

    @NullableBySpecification
    @ColumnLabel("PK_NAME")
    private String pkName;

    @ColumnLabel("DEFERRABILITY")
    private int deferrability;
}
