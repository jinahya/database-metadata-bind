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

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getColumns(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getColumns(String, String, String, String)
 */
@_ChildOf(Table.class)
@_ParentOf(ColumnPrivilege.class)
public class Column
        extends AbstractMetadataType {

    private static final long serialVersionUID = -409653682729081530L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Column> comparingInSpecifiedOrder(final Comparator<? super String> comparator) {
        return Comparator
                .comparing(Column::getTableCat, comparator)
                .thenComparing(Column::getTableSchem, comparator)
                .thenComparing(Column::getTableName, comparator)
                .thenComparing(Column::getOrdinalPosition, Comparator.naturalOrder());
    }

    static Comparator<Column> comparingInSpecifiedOrder(final Context context,
                                                        final Comparator<? super String> comparator)
            throws SQLException {
        return comparingInSpecifiedOrder(
                ContextUtils.nullPrecedence(context, comparator)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_CAT = "TABLE_CAT";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_SCHEM = "TABLE_SCHEM";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TABLE_NAME = "TABLE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_NAME = "COLUMN_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public static final int COLUMN_VALUE_NULLABLE_COLUMN_NO_NULLS = DatabaseMetaData.columnNoNulls;

    public static final int COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE = DatabaseMetaData.columnNullable;

    public static final int COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE_UNKNOWN = DatabaseMetaData.columnNullableUnknown;

    static final List<Integer> COLUMN_VALUES_NULLABLE = List.of(
            COLUMN_VALUE_NULLABLE_COLUMN_NO_NULLS,        // 0
            COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE,        // 1
            COLUMN_VALUE_NULLABLE_COLUMN_NULLABLE_UNKNOWN // 2
    );

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_AUTOINCREMENT = "IS_AUTOINCREMENT";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_GENERATEDCOLUMN = "IS_GENERATEDCOLUMN";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_SIZE = "COLUMN_SIZE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_BUFFER_LENGTH = "BUFFER_LENGTH";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_NUM_PREC_RADIX = "NUM_PREC_RADIX";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_COLUMN_DEF = "COLUMN_DEF";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATA_TYPE = "SQL_DATA_TYPE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATETIME_SUB = "SQL_DATETIME_SUB";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_ORDINAL_POSITION = "ORDINAL_POSITION";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE_CATALOG = "SCOPE_CATALOG";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE_SCHEMA = "SCOPE_SCHEMA";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE_TABLE = "SCOPE_TABLE";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";

    // ------------------------------------------------------------------------------------------ STATIC_FACTORY_METHODS
    static Column of(final String tableCat, final String tableSchem, final String tableName, final String columnName) {
        final var instance = new Column();
        instance.setTableCat(tableCat);
        instance.setTableSchem(tableSchem);
        instance.setTableName(tableName);
        instance.setColumnName(columnName);
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    public Column() {
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
               ",typeName=" + typeName +
               ",columnSize=" + columnSize +
               ",bufferLength=" + bufferLength +
               ",decimalDigits=" + decimalDigits +
               ",numPrecRadix=" + numPrecRadix +
               ",nullable=" + nullable +
               ",remarks=" + remarks +
               ",columnDef=" + columnDef +
               ",sqlDataType=" + sqlDataType +
               ",sqlDatetimeSub=" + sqlDatetimeSub +
               ",charOctetLength=" + charOctetLength +
               ",ordinalPosition=" + ordinalPosition +
               ",isNullable=" + isNullable +
               ",scopeCatalog=" + scopeCatalog +
               ",scopeSchema=" + scopeSchema +
               ",scopeTable=" + scopeTable +
               ",sourceDataType=" + sourceDataType +
               ",isAutoincrement=" + isAutoincrement +
               ",isGeneratedcolumn=" + isGeneratedcolumn +
               '}';
    }

    private String getTableCatEffective() {
        return Optional.ofNullable(getTableCat())
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .map(String::toUpperCase)
                .orElse(null);
    }

    private String getTableSchemEffective() {
        return Optional.ofNullable(getTableSchem())
                .map(String::strip)
                .filter(v -> !v.isBlank())
                .map(String::toUpperCase)
                .orElse(null);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final var that = (Column) obj;
        return Objects.equals(getTableCatEffective(), that.getTableCatEffective()) &&
               Objects.equals(getTableSchemEffective(), that.getTableSchemEffective()) &&
               Objects.equals(tableName, that.tableName) &&
               Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                getTableCatEffective(),
                getTableSchemEffective(),
                tableName,
                columnName
        );
    }

    // ------------------------------------------------------------------------------------------------- Bean-Validation
    @AssertTrue(message = "nullable must be 1, 2, or 3")
    private boolean isNullableValid() {
        if (nullable == null) {
            return true;
        }
        return COLUMN_VALUES_NULLABLE.contains(nullable);
    }

    @AssertTrue(message = "scopeCatalog must be null when dataType is not REF")
    private boolean isScopeCatalogValid() {
        if (scopeCatalog != null) {
            return true;
        }
        return dataType == null || !Objects.equals(dataType, Types.REF);
    }

    @AssertTrue(message = "scopeSchema must be null when dataType is not REF")
    private boolean isScopeSchemaValid() {
        if (scopeSchema != null) {
            return true;
        }
        return dataType == null || !Objects.equals(dataType, Types.REF);
    }

    @AssertTrue(message = "scopeTable must be null when dataType is not REF")
    private boolean isScopeTableValid() {
        if (scopeTable != null) {
            return true;
        }
        return dataType == null || !Objects.equals(dataType, Types.REF);
    }

    @AssertTrue(message = "sourceDataType must be null if DATA_TYPE is not DISTINCT or user-generated REF")
    private boolean isSourceDataTypeValid() {
        if (sourceDataType != null) {
            return true;
        }
        return dataType == null ||
               (!Objects.equals(dataType, Types.DISTINCT) ||
                Objects.equals(dataType, Types.REF));
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    public String getTableCat() {
        return tableCat;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     *
     * @param tableCat the value of {@value #COLUMN_LABEL_TABLE_CAT} column.
     */
    public void setTableCat(final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    public String getTableSchem() {
        return tableSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     *
     * @param tableSchem the value of {@value #COLUMN_LABEL_TABLE_SCHEM} column.
     */
    public void setTableSchem(final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------- tableName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     *
     * @param tableName the value of {@value #COLUMN_LABEL_TABLE_NAME} column.
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName

    /**
     * Returns the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     *
     * @param columnName the value of {@value #COLUMN_LABEL_COLUMN_NAME} column.
     */
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@code DATA_TYPE} column.
     *
     * @return the value of {@code DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@code DATA_TYPE} column.
     *
     * @param dataType the value of {@code DATA_TYPE} column.
     */
    public void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@code TYPE_NAME} column.
     *
     * @return the value of {@code TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@code TYPE_NAME} column.
     *
     * @param typeName the value of {@code TYPE_NAME} column.
     */
    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize

    /**
     * Returns the value of {@code COLUMN_SIZE} column.
     *
     * @return the value of {@code COLUMN_SIZE} column.
     */
    public Integer getColumnSize() {
        return columnSize;
    }

    /**
     * Sets the value of {@code COLUMN_SIZE} column.
     *
     * @param columnSize the value of {@code COLUMN_SIZE} column.
     */
    public void setColumnSize(final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // --------------------------------------------------------------------------------------------------- bufferLength

    /**
     * Returns the value of {@code BUFFER_LENGTH} column.
     *
     * @return the value of {@code BUFFER_LENGTH} column.
     */
    public Integer getBufferLength() {
        return bufferLength;
    }

    /**
     * Sets the value of {@code BUFFER_LENGTH} column.
     *
     * @param bufferLength the value of {@code BUFFER_LENGTH} column.
     */
    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits

    /**
     * Returns the value of {@code DECIMAL_DIGITS} column.
     *
     * @return the value of {@code DECIMAL_DIGITS} column.
     */
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    /**
     * Sets the value of {@code DECIMAL_DIGITS} column.
     *
     * @param decimalDigits the value of {@code DECIMAL_DIGITS} column.
     */
    public void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ------------------------------------------------------------------------------------------------------ numPrecRadix

    /**
     * Returns the value of {@code NUM_PREC_RADIX} column.
     *
     * @return the value of {@code NUM_PREC_RADIX} column.
     */
    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    /**
     * Sets the value of {@code NUM_PREC_RADIX} column.
     *
     * @param numPrecRadix the value of {@code NUM_PREC_RADIX} column.
     */
    public void setNumPrecRadix(final Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable

    /**
     * Returns the value of {@value #COLUMN_LABEL_NULLABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_NULLABLE} column.
     */
    public Integer getNullable() {
        return nullable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_NULLABLE} column.
     *
     * @param nullable the value of {@value #COLUMN_LABEL_NULLABLE} column.
     */
    public void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@code REMARKS} column.
     *
     * @return the value of {@code REMARKS} column.
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@code REMARKS} column.
     *
     * @param remarks the value of {@code REMARKS} column.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------------- columnDef

    /**
     * Returns the value of {@code COLUMN_DEF} column.
     *
     * @return the value of {@code COLUMN_DEF} column.
     */
    public String getColumnDef() {
        return columnDef;
    }

    /**
     * Sets the value of {@code COLUMN_DEF} column.
     *
     * @param columnDef the value of {@code COLUMN_DEF} column.
     */
    public void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
    }

    // ----------------------------------------------------------------------------------------------------- sqlDataType

    /**
     * Returns the value of {@code SQL_DATA_TYPE} column.
     *
     * @return the value of {@code SQL_DATA_TYPE} column.
     */
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    /**
     * Sets the value of {@code SQL_DATA_TYPE} column.
     *
     * @param sqlDataType the value of {@code SQL_DATA_TYPE} column.
     */
    public void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDatetimeSub

    /**
     * Returns the value of {@code SQL_DATETIME_SUB} column.
     *
     * @return the value of {@code SQL_DATETIME_SUB} column.
     */
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    /**
     * Sets the value of {@code SQL_DATETIME_SUB} column.
     *
     * @param sqlDatetimeSub the value of {@code SQL_DATETIME_SUB} column.
     */
    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // ------------------------------------------------------------------------------------------------- charOctetLength

    /**
     * Returns the value of {@code CHAR_OCTET_LENGTH} column.
     *
     * @return the value of {@code CHAR_OCTET_LENGTH} column.
     */
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    /**
     * Sets the value of {@code CHAR_OCTET_LENGTH} column.
     *
     * @param charOctetLength the value of {@code CHAR_OCTET_LENGTH} column.
     */
    public void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition

    /**
     * Returns the value of {@code ORDINAL_POSITION} column.
     *
     * @return the value of {@code ORDINAL_POSITION} column.
     */
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * Sets the value of {@code ORDINAL_POSITION} column.
     *
     * @param ordinalPosition the value of {@code ORDINAL_POSITION} column.
     */
    public void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable

    /**
     * Returns the value of {@code IS_NULLABLE} column.
     *
     * @return the value of {@code IS_NULLABLE} column.
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * Sets the value of {@code IS_NULLABLE} column.
     *
     * @param isNullable the value of {@code IS_NULLABLE} column.
     */
    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- scopeCatalog

    /**
     * Returns the value of {@code SCOPE_CATALOG} column.
     *
     * @return the value of {@code SCOPE_CATALOG} column.
     */
    public String getScopeCatalog() {
        return scopeCatalog;
    }

    /**
     * Sets the value of {@code SCOPE_CATALOG} column.
     *
     * @param scopeCatalog the value of {@code SCOPE_CATALOG} column.
     */
    public void setScopeCatalog(final String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    // ----------------------------------------------------------------------------------------------------- scopeSchema

    /**
     * Returns the value of {@code SCOPE_SCHEMA} column.
     *
     * @return the value of {@code SCOPE_SCHEMA} column.
     */
    public String getScopeSchema() {
        return scopeSchema;
    }

    /**
     * Sets the value of {@code SCOPE_SCHEMA} column.
     *
     * @param scopeSchema the value of {@code SCOPE_SCHEMA} column.
     */
    public void setScopeSchema(final String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    // ------------------------------------------------------------------------------------------------------ scopeTable

    /**
     * Returns the value of {@code SCOPE_TABLE} column.
     *
     * @return the value of {@code SCOPE_TABLE} column.
     */
    public String getScopeTable() {
        return scopeTable;
    }

    /**
     * Sets the value of {@code SCOPE_TABLE} column.
     *
     * @param scopeTable the value of {@code SCOPE_TABLE} column.
     */
    public void setScopeTable(final String scopeTable) {
        this.scopeTable = scopeTable;
    }

    // -------------------------------------------------------------------------------------------------- sourceDataType

    /**
     * Returns the value of {@code SOURCE_DATA_TYPE} column.
     *
     * @return the value of {@code SOURCE_DATA_TYPE} column.
     */
    public Integer getSourceDataType() {
        return sourceDataType;
    }

    /**
     * Sets the value of {@code SOURCE_DATA_TYPE} column.
     *
     * @param sourceDataType the value of {@code SOURCE_DATA_TYPE} column.
     */
    public void setSourceDataType(final Integer sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // ------------------------------------------------------------------------------------------------- isAutoincrement

    /**
     * Returns the value of {@value #COLUMN_LABEL_IS_AUTOINCREMENT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_IS_AUTOINCREMENT} column.
     */
    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_IS_AUTOINCREMENT} column.
     *
     * @param isAutoincrement the value of {@value #COLUMN_LABEL_IS_AUTOINCREMENT} column.
     */
    public void setIsAutoincrement(final String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    // ----------------------------------------------------------------------------------------------- isGeneratedcolumn

    /**
     * Returns the value of {@value #COLUMN_LABEL_IS_GENERATEDCOLUMN} column.
     *
     * @return the value of {@value #COLUMN_LABEL_IS_GENERATEDCOLUMN} column.
     */
    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_IS_GENERATEDCOLUMN} column.
     *
     * @param isGeneratedcolumn the value of {@value #COLUMN_LABEL_IS_GENERATEDCOLUMN} column.
     */
    public void setIsGeneratedcolumn(final String isGeneratedcolumn) {
        this.isGeneratedcolumn = isGeneratedcolumn;
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

    @_ColumnLabel(COLUMN_LABEL_TABLE_NAME)
    private String tableName;

    @_ColumnLabel(COLUMN_LABEL_COLUMN_NAME)
    private String columnName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_SIZE)
    private Integer columnSize;

    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_BUFFER_LENGTH)
    private Integer bufferLength;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_DECIMAL_DIGITS)
    private Integer decimalDigits;

    @_ColumnLabel(COLUMN_LABEL_NUM_PREC_RADIX)
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_REMARKS)
    private String remarks;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_COLUMN_DEF)
    private String columnDef;

    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATA_TYPE)
    private Integer sqlDataType;

    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATETIME_SUB)
    private Integer sqlDatetimeSub;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_CHAR_OCTET_LENGTH)
    private Integer charOctetLength;

    @Positive
    @_ColumnLabel(COLUMN_LABEL_ORDINAL_POSITION)
    private Integer ordinalPosition;

    @_ColumnLabel(COLUMN_LABEL_IS_NULLABLE)
    private String isNullable;

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCOPE_CATALOG)
    private String scopeCatalog;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCOPE_SCHEMA)
    private String scopeSchema;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SCOPE_TABLE)
    private String scopeTable;

    // -----------------------------------------------------------------------------------------------------------------

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SOURCE_DATA_TYPE)
    private Integer sourceDataType;

    @Pattern(regexp = MetadataTypeConstants.PATTERN_REGEXP_YES_NO_OR_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @Pattern(regexp = MetadataTypeConstants.PATTERN_REGEXP_YES_NO_OR_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;
}
