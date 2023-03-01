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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of {@link DatabaseMetaData#getPrimaryKeys(String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@_ChildOf(Table.class)
@Setter
@Getter
//@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class PrimaryKey extends AbstractMetadataType {

    private static final long serialVersionUID = 3159826510060898330L;

    static final Comparator<PrimaryKey> CASE_INSENSITIVE_ORDER
            = Comparator.comparing(PrimaryKey::getColumnName, String.CASE_INSENSITIVE_ORDER);

    static final Comparator<PrimaryKey> LEXICOGRAPHIC_ORDER
            = Comparator.comparing(PrimaryKey::getColumnName, nullsFirst(naturalOrder()));

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    public static final String COLUMN_LABEL_KEY_SEQ = "KEY_SEQ";

    public static final String COLUMN_LABEL_PK_NAME = "PK_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PrimaryKey)) return false;
        final PrimaryKey that = (PrimaryKey) obj;
        return Objects.equals(getPrimaryKeyId(), that.getPrimaryKeyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrimaryKeyId());
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    String getTableCatNonNull() {
        return Optional.ofNullable(getTableCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
        primaryKeyId = null;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    String getTableSchemNonNull() {
        return Optional.ofNullable(getTableSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
        primaryKeyId = null;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public void setTableName(String tableName) {
        this.tableName = tableName;
        primaryKeyId = null;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
        primaryKeyId = null;
    }

    // ---------------------------------------------------------------------------------------------------- primaryKeyId
    PrimaryKeyId getPrimaryKeyId() {
        if (primaryKeyId == null) {
            primaryKeyId = PrimaryKeyId.of(
                    TableId.of(
                            getTableCatNonNull(),
                            getTableSchemNonNull(),
                            getTableName()
                    ),
                    getColumnName()
            );
        }
        return primaryKeyId;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @ColumnLabel(COLUMN_LABEL_KEY_SEQ)
    private int keySeq;

    @_NullableBySpecification
    @ColumnLabel(COLUMN_LABEL_PK_NAME)
    private String pkName;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient PrimaryKeyId primaryKeyId;
}
