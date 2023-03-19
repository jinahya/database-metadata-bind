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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@Setter
@Getter
@ToString(callSuper = true)
public class PseudoColumn extends AbstractMetadataType {

    private static final long serialVersionUID = -5612575879670895510L;

    static final Comparator<PseudoColumn> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(PseudoColumn::tableCatNonNull, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(PseudoColumn::tableSchemNonNull, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(PseudoColumn::getTableName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(PseudoColumn::getColumnName, nullsFirst(String.CASE_INSENSITIVE_ORDER));

    static final Comparator<PseudoColumn> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(PseudoColumn::tableCatNonNull, nullsFirst(naturalOrder()))
                    .thenComparing(PseudoColumn::tableSchemNonNull, nullsFirst(naturalOrder()))
                    .thenComparing(PseudoColumn::getTableName, nullsFirst(naturalOrder()))
                    .thenComparing(PseudoColumn::getColumnName, nullsFirst(naturalOrder()));

    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PseudoColumn)) return false;
        final PseudoColumn that = (PseudoColumn) obj;
        return Objects.equals(tableCatNonNull(), that.tableCatNonNull()) &&
               Objects.equals(tableSchemNonNull(), that.tableSchemNonNull()) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                tableCatNonNull(),
                tableSchemNonNull(),
                tableName,
                columnName
        );
    }

    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)
    private String tableCat;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)
    private String tableSchem;

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    @_NotNull
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_NotNull
    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @_ColumnLabel("COLUMN_USAGE")
    private String columnUsage;

    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @_NotNull
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    String tableCatNonNull() {
        final String tableCat_ = getTableCat();
        if (tableCat_ == null) {
            return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
        }
        return tableCat_;
    }

    String tableSchemNonNull() {
        final String tableSchem_ = getTableSchem();
        if (tableSchem_ == null) {
            return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
        }
        return tableSchem_;
    }
}
