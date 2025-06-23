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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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
@EqualsAndHashCode(callSuper = true)
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
    @Nullable
    public String getTableCat() {
        return tableCat;
    }

    public void setTableCat(@Nullable final String tableCat) {
        this.tableCat = tableCat;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    @Nullable
    public String getTableSchem() {
        return tableSchem;
    }

    public void setTableSchem(@Nullable final String tableSchem) {
        this.tableSchem = tableSchem;
    }

    // ------------------------------------------------------------------------------------------------------ tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------ columnSize
    @Nullable
    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(@Nullable final Integer columnSize) {
        this.columnSize = columnSize;
    }

    // --------------------------------------------------------------------------------------------------- bufferLength
    public Integer getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(final Integer bufferLength) {
        this.bufferLength = bufferLength;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits
    @Nullable
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(@Nullable final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ------------------------------------------------------------------------------------------------------ numPrecRadix
    public Integer getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(final Integer numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    @Nullable
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(@Nullable final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------------- columnDef
    @Nullable
    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(@Nullable final String columnDef) {
        this.columnDef = columnDef;
    }

    // ----------------------------------------------------------------------------------------------------- sqlDataType
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDatetimeSub
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // ------------------------------------------------------------------------------------------------- charOctetLength
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- scopeCatalog
    @Nullable
    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(@Nullable final String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    // ----------------------------------------------------------------------------------------------------- scopeSchema
    @Nullable
    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(@Nullable final String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    // ------------------------------------------------------------------------------------------------------ scopeTable
    @Nullable
    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(@Nullable final String scopeTable) {
        this.scopeTable = scopeTable;
    }

    // -------------------------------------------------------------------------------------------------- sourceDataType
    @Nullable
    public Integer getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(@Nullable final Integer sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // ------------------------------------------------------------------------------------------------- isAutoincrement
    public String getIsAutoincrement() {
        return isAutoincrement;
    }

    public void setIsAutoincrement(final String isAutoincrement) {
        this.isAutoincrement = isAutoincrement;
    }

    // ----------------------------------------------------------------------------------------------- isGeneratedcolumn
    public String getIsGeneratedcolumn() {
        return isGeneratedcolumn;
    }

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
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_SIZE")
    private Integer columnSize;

    @_NotUsedBySpecification
    @_ColumnLabel("BUFFER_LENGTH")
    private Integer bufferLength;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("COLUMN_DEF")
    private String columnDef;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @_NotUsedBySpecification
    @_ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @Positive
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    @Pattern(regexp = MetadataTypeConstants.PATTERN_REGEXP_YES_NO_OR_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_AUTOINCREMENT)
    private String isAutoincrement;

    @Pattern(regexp = MetadataTypeConstants.PATTERN_REGEXP_YES_NO_OR_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_GENERATEDCOLUMN)
    private String isGeneratedcolumn;

    // -----------------------------------------------------------------------------------------------------------------
    List<ColumnPrivilege> getColumnPrivileges() {
        if (columnPrivileges == null) {
            columnPrivileges = new ArrayList<>();
        }
        return columnPrivileges;
    }

    void setColumnPrivileges(final List<ColumnPrivilege> columnPrivileges) {
        this.columnPrivileges = columnPrivileges;
    }

    private List<ColumnPrivilege> columnPrivileges;
}
