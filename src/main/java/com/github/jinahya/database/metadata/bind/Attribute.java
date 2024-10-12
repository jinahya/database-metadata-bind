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

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the
 * {@link DatabaseMetaData#getAttributes(String, String, String, String) getAttributes(catalog, schemaPattern,
 * typeNamePattern, attributeNamePattern)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getAttributes(String, String, String, String)
 * @see Nullable
 */

@_ChildOf(UDT.class)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Attribute
        extends AbstractMetadataType
        implements HasIsNullableEnum {

    private static final long serialVersionUID = 1913681105410440186L;

    // -----------------------------------------------------------------------------------------------------------------
    static Comparator<Attribute> comparing(final Context context, final Comparator<? super String> comparator)
            throws SQLException {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(comparator, "comparator is null");
        return Comparator.comparing(Attribute::getTypeCat, ContextUtils.nulls(context, comparator))
                .thenComparing(Attribute::getTypeSchem, ContextUtils.nulls(context, comparator))
                .thenComparing(Attribute::getTypeName, ContextUtils.nulls(context, comparator))
                .thenComparing(Attribute::getOrdinalPosition, ContextUtils.nulls(context, Comparator.naturalOrder()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // ------------------------------------------------------------------------------------------------------ TYPE_SCHEM
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // ------------------------------------------------------------------------------------------------------- TYPE_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_TYPE_NAME = "TYPE_NAME";

    // ------------------------------------------------------------------------------------------------------- ATTR_NAME

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_ATTR_NAME = "ATTR_NAME";
    // ------------------------------------------------------------------------------------------------------- DATA_TYPE

    /**
     * The column label of {@value}.
     */
    public static final String COLUMN_LABEL_DATA_TYPE = "DATA_TYPE";

    // -------------------------------------------------------------------------------------------------------- NULLABLE
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    /**
     * Constants for {@value #COLUMN_LABEL_NULLABLE} column values.
     */
    public enum Nullable
            implements _IntFieldEnum<Nullable> {

        /**
         * A value for {@link DatabaseMetaData#attributeNoNulls}({@value DatabaseMetaData#attributeNoNulls}).
         */
        ATTRIBUTE_NO_NULLS(DatabaseMetaData.attributeNoNulls), // 0

        /**
         * A value for {@link DatabaseMetaData#attributeNullable}({@value DatabaseMetaData#attributeNullable}).
         */
        ATTRIBUTE_NULLABLE(DatabaseMetaData.attributeNullable), // 1

        /**
         * A value for
         * {@link DatabaseMetaData#attributeNullableUnknown}({@value DatabaseMetaData#attributeNullableUnknown}).
         */
        ATTRIBUTE_NULLABLE_UNKNOWN(DatabaseMetaData.attributeNullableUnknown) // 2
        ;

        /**
         * Finds the value for specified {@link Attribute#COLUMN_LABEL_NULLABLE} attribute value.
         *
         * @param fieldValue the value of {@link Attribute#COLUMN_LABEL_NULLABLE} attribute to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Nullable valueOfFieldValue(final int fieldValue) {
            return _IntFieldEnum.valueOfFieldValue(Nullable.class, fieldValue);
        }

        Nullable(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_LABEL_IS_NULLABLE = "IS_NULLABLE";

    // ---------------------------------------------------------------------------------------------------- CONSTRUCTORS

    /**
     * Creates a new instance.
     */
    protected Attribute() {
        super();
    }

    // -------------------------------------------------------------------------------------------------------- tableCat

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_CAT} column.
     */
    @jakarta.annotation.Nullable
    public String getTypeCat() {
        return typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem

    /**
     * Returns the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     *
     * @return the value of {@value #COLUMN_LABEL_TYPE_SCHEM} column.
     */
    @jakarta.annotation.Nullable
    public String getTypeSchem() {
        return typeSchem;
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

    // -------------------------------------------------------------------------------------------------------- attrName

    /**
     * Returns the value of {@value #COLUMN_LABEL_ATTR_NAME} column.
     *
     * @return the value of {@value #COLUMN_LABEL_ATTR_NAME} column.
     */
    public String getAttrName() {
        return attrName;
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

    // -------------------------------------------------------------------------------------------------------- nullable
    public Nullable getNullableAsEnum() {
        return Optional.ofNullable(getNullable())
                .map(Nullable::valueOfFieldValue)
                .orElse(null);
    }

    public void setNullableAsEnum(Nullable nullableAsEnum) {
        setNullable(
                Optional.ofNullable(nullableAsEnum)
                        .map(_IntFieldEnum::fieldValueAsInt)
                        .orElse(null)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    @EqualsAndHashCode.Include
    private String typeCat;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    @EqualsAndHashCode.Include
    private String typeSchem;

    @_ColumnLabel(COLUMN_LABEL_TYPE_NAME)
    @EqualsAndHashCode.Include
    private String typeName;

    @_ColumnLabel(COLUMN_LABEL_ATTR_NAME)
    @EqualsAndHashCode.Include
    private String attrName;

    // -----------------------------------------------------------------------------------------------------------------
    @_ColumnLabel(COLUMN_LABEL_DATA_TYPE)
    private Integer dataType;

    @_ColumnLabel("ATTR_TYPE_NAME")
    private String attrTypeName;

    @_ColumnLabel("ATTR_SIZE")
    private Integer attrSize;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("ATTR_DEF")
    private String attrDef;

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

    @Pattern(regexp = YesNoEmptyConstants.REGEXP_YES_NO_EMPTY)
    @_ColumnLabel(COLUMN_LABEL_IS_NULLABLE)
    private String isNullable;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;
}
