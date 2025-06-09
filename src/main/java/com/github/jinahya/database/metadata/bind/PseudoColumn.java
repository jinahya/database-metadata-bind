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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */

@_ChildOf(Table.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PseudoColumn
        extends AbstractMetadataType {

    private static final long serialVersionUID = -5612575879670895510L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<PseudoColumn> comparing(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(PseudoColumn::getTableCat, comparator)
                .thenComparing(PseudoColumn::getTableSchem, comparator)
                .thenComparing(PseudoColumn::getTableName, comparator)
                .thenComparing(PseudoColumn::getColumnName, comparator);
    }

    static Comparator<PseudoColumn> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        return comparing(ContextUtils.nullPrecedence(context, comparator));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    public static final String COLUMN_LABEL_COLUMN_SIZE = "COLUMN_SIZE";

    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    public static final String COLUMN_LABEL_NUM_PREC_RADIX = "NUM_PREC_RADIX";

    public static final String COLUMN_LABEL_USAGE = "COLUMN_USAGE";

    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    public static final String COLUMN_LABEL_CHARACTER_OCTET_LENGTH = "CHAR_OCTET_LENGTH";

    // ----------------------------------------------------------------------------------------------------- IS_NULLABLE
    public static final String COLUMN_LABEL_COLUMN_IS_NULLABLE = "IS_NULLABLE";

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_YES = YesNoConstants.YES;

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_NO = YesNoConstants.NO;

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_EMPTY = YesNoEmptyConstants.EMPTY;

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    @SuppressWarnings({
            "java:S2637" // "@NonNull" values should not be set to null
    })
    protected PseudoColumn() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "tableCat=" + tableCat +
               ",tableSchem=" + tableSchem +
               ",tableName=" + tableName +
               ",columnName=" + columnName +
               ",dataType=" + dataType +
               ",columnSize=" + columnSize +
               ",decimalDigits=" + decimalDigits +
               ",numPrecRadix=" + numPrecRadix +
               ",columnUsage=" + columnUsage +
               ",remarks=" + remarks +
               ",charOctetLength=" + charOctetLength +
               ",isNullable=" + isNullable +
               '}';
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    protected void setTableCat(@Nullable final String tableCat) {
        this.tableCat = tableCat;
    }

    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    protected void setTableSchem(@Nullable final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    @Nonnull
    public String getTableName() {
        return tableName;
    }

    protected void setTableName(@Nonnull final String tableName) {
        this.tableName = tableName;
    }

    @Nonnull
    public String getColumnName() {
        return columnName;
    }

    protected void setColumnName(@Nonnull String columnName) {
        this.columnName = columnName;
    }

    public Integer getDataType() {
        return dataType;
    }

    protected void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    @Nullable
    public Integer getColumnSize() {
        return columnSize;
    }

    protected void setColumnSize(@Nullable Integer columnSize) {
        this.columnSize = columnSize;
    }

    @Nullable
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    protected void setDecimalDigits(@Nullable Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    protected void setNumPrecRadix(Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    public String getColumnUsage() {
        return columnUsage;
    }

    protected void setColumnUsage(String columnUsage) {
        this.columnUsage = columnUsage;
    }

    @Nullable
    public String getRemarks() {
        return remarks;
    }

    protected void setRemarks(@Nullable String remarks) {
        this.remarks = remarks;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    protected void setCharOctetLength(Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public String getIsNullable() {
        return isNullable;
    }

    protected void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_CAT)

    private String tableCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TABLE_SCHEM)

    private String tableSchem;

    @Nonnull
    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)

    private String tableName;

    @Nonnull
    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)

    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_NUM_PREC_RADIX)
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_USAGE)
    private String columnUsage;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_ColumnLabel(COLUMN_LABEL_CHARACTER_OCTET_LENGTH)
    private Integer charOctetLength;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_IS_NULLABLE)
    private String isNullable;

    // -----------------------------------------------------------------------------------------------------------------
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @SuppressWarnings({
            "java:S116" // Field names should comply with a naming convention
    })
    private Catalog tableCatalog_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @SuppressWarnings({
            "java:S116" // Field names should comply with a naming convention
    })
    private Schema tableSchema_;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @SuppressWarnings({
            "java:S116" // Field names should comply with a naming convention
    })
    private Table table_;

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    Catalog getTableCatalog_() {
        if (tableCatalog_ == null) {
            tableCatalog_ = Catalog.of(tableCat);
        }
        return tableCatalog_;
    }

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    void setTableCatalog_(final Catalog tableCatalog_) {
        this.tableCatalog_ = tableCatalog_;
        setTableCat(
                Optional.ofNullable(this.tableCatalog_)
                        .map(Catalog::getTableCat)
                        .orElse(null)
        );
    }

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    Schema getTableSchema_() {
        if (tableSchema_ == null) {
            tableSchema_ = Schema.of(getTableCatalog_(), tableSchem);
        }
        return tableSchema_;
    }

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    void setTableSchema_(final Schema tableSchema_) {
        this.tableSchema_ = tableSchema_;
        setTableCatalog_(
                Optional.ofNullable(this.tableSchema_)
                        .map(Schema::getTableCatalog_)
                        .orElse(null)
        );
    }

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    Table getTable_() {
        return table_;
    }

    @SuppressWarnings({
            "java:S100", // Method names should comply with a naming convention
            "java:S117"  // Local variable and method parameter names should comply with a naming convention
    })
    void setTable_(final Table parent_) {
        this.table_ = parent_;
        setTableSchema_(
                Optional.ofNullable(this.table_)
                        .map(Table::getTableSchema_)
                        .orElse(null)
        );
    }
}
