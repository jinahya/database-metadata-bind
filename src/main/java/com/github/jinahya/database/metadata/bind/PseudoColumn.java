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

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * A class for binding results of the {@link DatabaseMetaData#getPseudoColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */

@_ChildOf(Table.class)
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

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_YES = MetadataTypeConstants.YES;

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_NO = MetadataTypeConstants.NO;

    public static final String COLUMN_VALUE_COLUMN_IS_NULLABLE_EMPTY = MetadataTypeConstants.EMPTY;

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

    // -------------------------------------------------------------------------------------------------------- tableCat
    public String getTableCat() {
        return tableCat;
    }

    protected void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    public String getTableSchem() {
        return tableSchem;
    }

    protected void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName
    public String getTableName() {
        return tableName;
    }

    protected void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    protected void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public Integer getDataType() {
        return dataType;
    }

    protected void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize
    public Integer getColumnSize() {
        return columnSize;
    }

    protected void setColumnSize(final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    protected void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix
    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    protected void setNumPrecRadix(final Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // ----------------------------------------------------------------------------------------------------- columnUsage
    public String getColumnUsage() {
        return columnUsage;
    }

    protected void setColumnUsage(final String columnUsage) {
        this.columnUsage = columnUsage;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    protected void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // -------------------------------------------------------------------------------------------- characterOctetLength
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    protected void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable
    public String getIsNullable() {
        return isNullable;
    }

    protected void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_NUM_PREC_RADIX)
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_USAGE)
    private String columnUsage;

    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @_ColumnLabel(COLUMN_LABEL_CHARACTER_OCTET_LENGTH)
    private Integer charOctetLength;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_IS_NULLABLE)
    private String isNullable;
}
