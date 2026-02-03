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
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.Nullable;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getAttributes(String, String, String, String) getAttributes(catalog, schemaPattern,
 * typeNamePattern, attributeNamePattern)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see DatabaseMetaData#getAttributes(String, String, String, String)
 * @see Context#getAttributes(String, String, String, String)
 */
@_ChildOf(UDT.class)
public class Attribute
        extends AbstractMetadataType {

    private static final long serialVersionUID = 1913681105410440186L;

    // ----------------------------------------------------------------------------------------------------- COMPARATORS

    /**
     * Returns a comparator comparing values in the specified order.
     * <blockquote>
     * They are ordered by <code>TYPE_CAT</code>, <code>TYPE_SCHEM</code>, <code>TYPE_NAME</code> and
     * <code>ORDINAL_POSITION</code>.
     * </blockquote>
     *
     * @param operator   a null-safe unary operator for adjusting string values.
     * @param comparator a null-safe string comparator for comparing attributes.
     * @return a comparator comparing values in the specified order.
     * @see ContextUtils#nullOrdered(Context, Comparator)
     */
    static Comparator<Attribute> comparingInSpecifiedOrder(final UnaryOperator<String> operator,
                                                           final Comparator<? super String> comparator) {
        Objects.requireNonNull(operator, "operator is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator
                .<Attribute, String>comparing(v -> operator.apply(v.getTypeCat()), comparator)
                .thenComparing(v -> operator.apply(v.getTypeSchem()), comparator)
                .thenComparing(v -> operator.apply(v.getTypeName()), comparator)
                .thenComparing(Attribute::getOrdinalPosition, Comparator.naturalOrder());
    }

    // -------------------------------------------------------------------------------------------------------- TYPE_CAT

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // ------------------------------------------------------------------------------------------------------ TYPE_SCHEM

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // ------------------------------------------------------------------------------------------------------- TYPE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // ------------------------------------------------------------------------------------------------------- ATTR_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ATTR_NAME = "ATTR_NAME";
    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -------------------------------------------------------------------------------------------------------- NULLABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    // ----------------------------------------------------------------------------------------------------- IS_NULLABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // -------------------------------------------------------------------------------------------------- ATTR_TYPE_NAME

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ATTR_TYPE_NAME = "ATTR_TYPE_NAME";

    // ------------------------------------------------------------------------------------------------------- ATTR_SIZE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ATTR_SIZE = "ATTR_SIZE";

    // -------------------------------------------------------------------------------------------------- DECIMAL_DIGITS

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_DECIMAL_DIGITS = "DECIMAL_DIGITS";

    // -------------------------------------------------------------------------------------------------- NUM_PREC_RADIX

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_NUM_PREC_RADIX = "NUM_PREC_RADIX";

    // --------------------------------------------------------------------------------------------------------- REMARKS

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_REMARKS = "REMARKS";

    // -------------------------------------------------------------------------------------------------------- ATTR_DEF

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ATTR_DEF = "ATTR_DEF";

    // --------------------------------------------------------------------------------------------------- SQL_DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATA_TYPE = "SQL_DATA_TYPE";

    // ------------------------------------------------------------------------------------------------ SQL_DATETIME_SUB

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SQL_DATETIME_SUB = "SQL_DATETIME_SUB";

    // ----------------------------------------------------------------------------------------------- CHAR_OCTET_LENGTH

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";

    // ------------------------------------------------------------------------------------------------ ORDINAL_POSITION

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_ORDINAL_POSITION = "ORDINAL_POSITION";

    // --------------------------------------------------------------------------------------------------- SCOPE_CATALOG

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE_CATALOG = "SCOPE_CATALOG";

    // ---------------------------------------------------------------------------------------------------- SCOPE_SCHEMA

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE_SCHEMA = "SCOPE_SCHEMA";

    // ----------------------------------------------------------------------------------------------------- SCOPE_TABLE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SCOPE_TABLE = "SCOPE_TABLE";

    // ------------------------------------------------------------------------------------------------ SOURCE_DATA_TYPE

    /**
     * A column label of {@value}.
     */
    public static final String COLUMN_LABEL_SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    Attribute() {
        super();
    }

    // ------------------------------------------------------------------------------------------------ java.lang.Object

    @Override
    public String toString() {
        return super.toString() + '{' +
               "typeCat=" + typeCat +
               ",typeSchem=" + typeSchem +
               ",typeName=" + typeName +
               ",attrName=" + attrName +
               ",dataType=" + dataType +
               ",attrTypeName=" + attrTypeName +
               ",attrSize=" + attrSize +
               ",decimalDigits=" + decimalDigits +
               ",numPrecRadix=" + numPrecRadix +
               ",nullable=" + nullable +
               ",remarks=" + remarks +
               ",attrDef=" + attrDef +
               ",sqlDataType=" + sqlDataType +
               ",sqlDatetimeSub=" + sqlDatetimeSub +
               ",charOctetLength=" + charOctetLength +
               ",ordinalPosition=" + ordinalPosition +
               ",isNullable=" + isNullable +
               ",scopeCatalog=" + scopeCatalog +
               ",scopeSchema=" + scopeSchema +
               ",scopeTable=" + scopeTable +
               ",sourceDataType=" + sourceDataType +
               '}';
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
        final var that = (Attribute) obj;
        return Objects.equals(typeCat, that.typeCat) &&
               Objects.equals(typeSchem, that.typeSchem) &&
               Objects.equals(typeName, that.typeName) &&
               Objects.equals(attrName, that.attrName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeCat, typeSchem, typeName, attrName);
    }

    // ---------------------------------------------------------------------------------------------- Jakarta-Validation
    @AssertTrue
    // Correct: null if DATA_TYPE isn't REF
    private boolean isScopeCatalogNullWhenDataTypeIsNotRef() {
        if (dataType == null) {
            return true;
        }
        if (dataType != java.sql.Types.REF) {
            return scopeCatalog == null;
        }
        return true;
    }

    @AssertTrue
    // Correct: null if DATA_TYPE isn't REF
    private boolean isScopeSchemaNullWhenDataTypeIsNotRef() {
        if (dataType == null) {
            return true;
        }
        if (dataType != java.sql.Types.REF) {
            return scopeSchema == null;
        }
        return true;
    }

    @AssertTrue
    // Correct: null if DATA_TYPE isn't REF
    private boolean isScopeTableNullWhenDataTypeIsNotRef() {
        if (dataType == null) {
            return true;
        }
        if (dataType != java.sql.Types.REF) {
            return scopeTable == null;
        }
        return true;
    }

    //    @AssertTrue
    // null if DATA_TYPE isn't DISTINCT or user-generated REF
    // Note: This validation uses Types.REF without distinguishing user-generated vs system-generated REF.
    //       This is slightly more permissive than the spec, but JDBC doesn't provide an easy way to distinguish them.
    private boolean isSourceDataTypeNullWhenDataTypeIsNotDistinctOrUserGeneratedRef() {
        if (dataType == null) {
            return true;
        }
        if (dataType != java.sql.Types.DISTINCT && dataType != java.sql.Types.REF) {
            return sourceDataType == null;
        }
        return true;
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns current value of {@value #COLUMN_LABEL_TYPE_CAT} property.
     *
     * @return current value of the {@value #COLUMN_LABEL_TYPE_CAT} property.
     * @see #setTypeCat(String)
     */

    @Nullable
    public String getTypeCat() {
        return typeCat;
    }

    /**
     * Replaces current value of {@value #COLUMN_LABEL_TYPE_CAT} property with specified value.
     *
     * @param typeCat new value for the {@value #COLUMN_LABEL_TYPE_CAT} property.
     * @see #getTypeCat()
     */
    void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */

    @Nullable
    public String getTypeSchem() {
        return typeSchem;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @param typeSchem the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     *
     * @param typeName the value of {@value #COLUMN_LABEL_TYPE_NAME} column.
     */
    void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // -------------------------------------------------------------------------------------------------------- attrName

    /**
     * Returns the value of {@value #COLUMN_LABEL_ATTR_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_ATTR_NAME} column.
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_ATTR_NAME} column.
     *
     * @param attrName the value of {@value #COLUMN_LABEL_ATTR_NAME} column.
     */
    void setAttrName(final String attrName) {
        this.attrName = attrName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType

    /**
     * Returns the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    public Integer getDataType() {
        return dataType;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     *
     * @param dataType the value of {@value #COLUMN_LABEL_DATA_TYPE} column.
     */
    void setDataType(final Integer dataType) {
        this.dataType = dataType;
    }

    // ---------------------------------------------------------------------------------------------------- attrTypeName

    /**
     * Returns the value of {@code ATTR_TYPE_NAME} column.
     *
     * @return the value of {@code ATTR_TYPE_NAME} column.
     */
    public String getAttrTypeName() {
        return attrTypeName;
    }

    /**
     * Sets the value of {@code ATTR_TYPE_NAME} column.
     *
     * @param attrTypeName the value of {@code ATTR_TYPE_NAME} column.
     */
    void setAttrTypeName(final String attrTypeName) {
        this.attrTypeName = attrTypeName;
    }

    // -------------------------------------------------------------------------------------------------------- attrSize

    /**
     * Returns the value of {@code ATTR_SIZE} column.
     *
     * @return the value of {@code ATTR_SIZE} column.
     */
    public Integer getAttrSize() {
        return attrSize;
    }

    /**
     * Sets the value of {@code ATTR_SIZE} column.
     *
     * @param attrSize the value of {@code ATTR_SIZE} column.
     */
    void setAttrSize(final Integer attrSize) {
        this.attrSize = attrSize;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits

    /**
     * Returns the value of {@code DECIMAL_DIGITS} column.
     *
     * @return the value of {@code DECIMAL_DIGITS} column.
     */
    @Nullable
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    /**
     * Sets the value of {@code DECIMAL_DIGITS} column.
     *
     * @param decimalDigits the value of {@code DECIMAL_DIGITS} column.
     */
    void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix

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
    void setNumPrecRadix(final Integer numPrecRadix) {
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
    void setNullable(final Integer nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks

    /**
     * Returns the value of {@code REMARKS} column.
     *
     * @return the value of {@code REMARKS} column.
     */
    @Nullable
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets the value of {@code REMARKS} column.
     *
     * @param remarks the value of {@code REMARKS} column.
     */
    void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------------- attrDef

    /**
     * Returns the value of {@code ATTR_DEF} column.
     *
     * @return the value of {@code ATTR_DEF} column.
     */
    @Nullable
    public String getAttrDef() {
        return attrDef;
    }

    /**
     * Sets the value of {@code ATTR_DEF} column.
     *
     * @param attrDef the value of {@code ATTR_DEF} column.
     */
    void setAttrDef(final String attrDef) {
        this.attrDef = attrDef;
    }

    // ----------------------------------------------------------------------------------------------------- sqlDataType

    /**
     * Returns the value of {@code SQL_DATA_TYPE} column.
     *
     * @return the value of {@code SQL_DATA_TYPE} column.
     */
    @Nullable
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    /**
     * Sets the value of {@code SQL_DATA_TYPE} column.
     *
     * @param sqlDataType the value of {@code SQL_DATA_TYPE} column.
     */
    void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // -------------------------------------------------------------------------------------------------- sqlDatetimeSub

    /**
     * Returns the value of {@code SQL_DATETIME_SUB} column.
     *
     * @return the value of {@code SQL_DATETIME_SUB} column.
     */
    @Nullable
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    /**
     * Sets the value of {@code SQL_DATETIME_SUB} column.
     *
     * @param sqlDatetimeSub the value of {@code SQL_DATETIME_SUB} column.
     */
    void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
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
    void setCharOctetLength(final Integer charOctetLength) {
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
    void setOrdinalPosition(final Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // ------------------------------------------------------------------------------------------------------ isNullable

    /**
     * Returns the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     *
     * @return the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     */
    public String getIsNullable() {
        return isNullable;
    }

    /**
     * Sets the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     *
     * @param isNullable the value of {@value #COLUMN_LABEL_IS_NULLABLE} column.
     */
    void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ---------------------------------------------------------------------------------------------------- scopeCatalog

    /**
     * Returns the value of {@code SCOPE_CATALOG} column.
     *
     * @return the value of {@code SCOPE_CATALOG} column.
     */
    @Nullable
    public String getScopeCatalog() {
        return scopeCatalog;
    }

    /**
     * Sets the value of {@code SCOPE_CATALOG} column.
     *
     * @param scopeCatalog the value of {@code SCOPE_CATALOG} column.
     */
    void setScopeCatalog(final String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    // ----------------------------------------------------------------------------------------------------- scopeSchema

    /**
     * Returns the value of {@code SCOPE_SCHEMA} column.
     *
     * @return the value of {@code SCOPE_SCHEMA} column.
     */
    @Nullable
    public String getScopeSchema() {
        return scopeSchema;
    }

    /**
     * Sets the value of {@code SCOPE_SCHEMA} column.
     *
     * @param scopeSchema the value of {@code SCOPE_SCHEMA} column.
     */
    void setScopeSchema(final String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    // ------------------------------------------------------------------------------------------------------ scopeTable

    /**
     * Returns the value of {@code SCOPE_TABLE} column.
     *
     * @return the value of {@code SCOPE_TABLE} column.
     */
    @Nullable
    public String getScopeTable() {
        return scopeTable;
    }

    /**
     * Sets the value of {@code SCOPE_TABLE} column.
     *
     * @param scopeTable the value of {@code SCOPE_TABLE} column.
     */
    void setScopeTable(final String scopeTable) {
        this.scopeTable = scopeTable;
    }

    // -------------------------------------------------------------------------------------------------- sourceDataType

    /**
     * Returns the value of {@code SOURCE_DATA_TYPE} column.
     *
     * @return the value of {@code SOURCE_DATA_TYPE} column.
     */
    @Nullable
    public Integer getSourceDataType() {
        return sourceDataType;
    }

    /**
     * Sets the value of {@code SOURCE_DATA_TYPE} column.
     *
     * @param sourceDataType the value of {@code SOURCE_DATA_TYPE} column.
     */
    void setSourceDataType(final Integer sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    private String typeName;

    @_ColumnLabel(COLUMN_LABEL_ATTR_NAME)
    private String attrName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel(COLUMN_LABEL_ATTR_TYPE_NAME)
    private String attrTypeName;

    @_ColumnLabel(COLUMN_LABEL_ATTR_SIZE)
    private Integer attrSize;

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
    @_ColumnLabel(COLUMN_LABEL_ATTR_DEF)
    private String attrDef;

    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATA_TYPE)
    private Integer sqlDataType;

    @Nullable
    @_NotUsedBySpecification
    @_ColumnLabel(COLUMN_LABEL_SQL_DATETIME_SUB)
    private Integer sqlDatetimeSub;

    @_ColumnLabel(COLUMN_LABEL_CHAR_OCTET_LENGTH)
    private Integer charOctetLength;

    @Positive
    @_ColumnLabel(COLUMN_LABEL_ORDINAL_POSITION)
    private Integer ordinalPosition;

    @_ColumnLabel(COLUMN_LABEL_IS_NULLABLE)
    private String isNullable;

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

    @Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_SOURCE_DATA_TYPE)
    private Integer sourceDataType;
}
