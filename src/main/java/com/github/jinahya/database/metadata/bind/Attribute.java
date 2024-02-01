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

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * A class for binding results of the {@link DatabaseMetaData#getAttributes(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getAttributes(String, String, String, String)
 * @see Nullable
 */
@Setter
@Getter
@ToString(callSuper = true)
public class Attribute extends AbstractMetadataType {

    private static final long serialVersionUID = 1913681105410440186L;

    static final Comparator<Attribute> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Attribute::getTypeCat, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Attribute::getTypeSchem, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparing(Attribute::getTypeName, nullsFirst(String.CASE_INSENSITIVE_ORDER))
                    .thenComparingInt(Attribute::getOrdinalPosition);

    static final Comparator<Attribute> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Attribute::getTypeCat, nullsFirst(naturalOrder()))
                    .thenComparing(Attribute::getTypeSchem, nullsFirst(naturalOrder()))
                    .thenComparing(Attribute::getTypeName, nullsFirst(naturalOrder()))
                    .thenComparingInt(Attribute::getOrdinalPosition);

    // -------------------------------------------------------------------------------------------------------- TYPE_CAT
    public static final String COLUMN_LABEL_TYPE_CAT = "TYPE_CAT";

    // ------------------------------------------------------------------------------------------------------ TYPE_SCHEM
    public static final String COLUMN_LABEL_TYPE_SCHEM = "TYPE_SCHEM";

    // -------------------------------------------------------------------------------------------------------- NULLABLE
    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    /**
     * Constants for {@value #COLUMN_LABEL_NULLABLE} column values.
     */
    public enum Nullable implements _IntFieldEnum<Nullable> {

        /**
         * A value for {@link DatabaseMetaData#attributeNoNulls}({@value DatabaseMetaData#attributeNoNulls}).
         */
        ATTRIBUTE_NO_NULLS(DatabaseMetaData.attributeNoNulls),// 0

        /**
         * A value for {@link DatabaseMetaData#attributeNullable}({@value DatabaseMetaData#attributeNullable}).
         */
        ATTRIBUTE_NULLABLE(DatabaseMetaData.attributeNullable), // 1

        /**
         * A value for
         * {@link DatabaseMetaData#attributeNullableUnknown}({@value DatabaseMetaData#attributeNullableUnknown}).
         */
        PSEUDO(DatabaseMetaData.attributeNullableUnknown) // 2
        ;

        /**
         * Finds the value for specified {@link Attribute#COLUMN_LABEL_NULLABLE} attribute value.
         *
         * @param nullable the value of {@link Attribute#COLUMN_LABEL_NULLABLE} attribute to match.
         * @return the value matched.
         * @throws IllegalStateException when no value matched.
         */
        public static Nullable valueOfNullable(final int nullable) {
            return _IntFieldEnum.valueOfFieldValue(Nullable.class, nullable);
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

    public static final String COLUMN_VALUE_IS_NULLABLE_YES = "YES";

    public static final String COLUMN_VALUE_IS_NULLABLE_NO = "NO";

    public static final String COLUMN_VALUE_IS_NULLABLE_EMPTY = "";

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attribute)) return false;
        final Attribute that = (Attribute) obj;
        return Objects.equals(typeCatNonNull(), that.typeCatNonNull()) &&
               Objects.equals(typeSchemNonNull(), that.typeSchemNonNull()) &&
               Objects.equals(typeName, that.typeName) &&
               Objects.equals(attrName, that.attrName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                typeCatNonNull(),
                typeSchemNonNull(),
                typeName,
                attrName
        );
    }

    // -------------------------------------------------------------------------------------------------------- tableCat
    String typeCatNonNull() {
        final String typeCat_ = getTypeCat();
        if (typeCat_ != null) {
            return typeCat_;
        }
        return Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY;
    }

    // ------------------------------------------------------------------------------------------------------ tableSchem
    String typeSchemNonNull() {
        final String typeSchem_ = getTypeSchem();
        if (typeSchem_ != null) {
            return typeSchem_;
        }
        return Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_CAT)
    private String typeCat;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel(COLUMN_LABEL_TYPE_SCHEM)
    private String typeSchem;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_ColumnLabel("ATTR_NAME")
    private String attrName;

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    @_NonNullBySpecification
    @_ColumnLabel("DATA_TYPE")
    private Integer dataType;

    @_ColumnLabel("ATTR_TYPE_NAME")
    private String attrTypeName;

    @NotNull
    @_NonNullBySpecification
    @_ColumnLabel("ATTR_SIZE")
    private Integer attrSize;

    @jakarta.annotation.Nullable
    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @NotNull
    @_NonNullBySpecification
    @_ColumnLabel("NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @NotNull
    @_NonNullBySpecification
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

    @NotNull
    @_NonNullBySpecification
    @_ColumnLabel("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @NotNull
    @_NonNullBySpecification
    @_ColumnLabel("ORDINAL_POSITION")
    private Integer ordinalPosition;

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
