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

/**
 * A class for binding results of {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@_ChildOf(Table.class)
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class PseudoColumn extends AbstractMetadataType {

    private static final long serialVersionUID = -5612575879670895510L;

    static final Comparator<PseudoColumn> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(PseudoColumn::getPseudoColumnId, PseudoColumnId.CASE_INSENSITIVE_ORDER);

    static final Comparator<PseudoColumn> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(PseudoColumn::getPseudoColumnId, PseudoColumnId.LEXICOGRAPHIC_ORDER);

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PseudoColumn)) return false;
        final PseudoColumn that = (PseudoColumn) obj;
        return Objects.equals(tableCat, that.tableCat) &&
               Objects.equals(tableSchem, that.tableSchem) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableCat,
                tableSchem,
                tableName,
                columnName
        );
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
        pseudoColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------- tableShem

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
        pseudoColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public void setTableName(final String tableName) {
        this.tableName = tableName;
        pseudoColumnId = null;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
        pseudoColumnId = null;
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

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @_NullableBySpecification
    @ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @ColumnLabel("COLUMN_USAGE")
    private String columnUsage;

    @_NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    // -----------------------------------------------------------------------------------------------------------------
    String getTableCatNonNull() {
        if (tableCat == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return tableCat;
    }

    String getTableSchemNonNull() {
        if (tableSchem == null) {
            return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
        }
        return tableSchem;
    }

    PseudoColumnId getPseudoColumnId() {
        if (pseudoColumnId == null) {
            pseudoColumnId = PseudoColumnId.of(
                    TableId.of(
                            getTableCatNonNull(),
                            getTableSchemNonNull(),
                            getTableName()
                    ),
                    getColumnName()
            );
        }
        return pseudoColumnId;
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient PseudoColumnId pseudoColumnId;
}
