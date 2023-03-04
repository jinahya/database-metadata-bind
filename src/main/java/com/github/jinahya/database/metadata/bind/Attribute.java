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
import lombok.Setter;
import lombok.ToString;

import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of the {@link DatabaseMetaData#getAttributes(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getAttributes(String, String, String, String)
 * @see Nullable
 */
@_ChildOf(UDT.class)
@Setter
@Getter
@ToString(callSuper = true)
public class Attribute extends AbstractMetadataType {

    private static final long serialVersionUID = 1913681105410440186L;

    static final Comparator<Attribute> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Attribute::getUDTId, UDTId.CASE_INSENSITIVE_ORDER)
                    .thenComparingInt(Attribute::getOrdinalPosition);

    static final Comparator<Attribute> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Attribute::getUDTId, UDTId.LEXICOGRAPHIC_ORDER)
                    .thenComparingInt(Attribute::getOrdinalPosition);

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

    public static final String COLUMN_VALUE_IS_NULLABLE_YES = "YES";

    public static final String COLUMN_VALUE_IS_NULLABLE_NO = "NO";

    public static final String COLUMN_VALUE_IS_NULLABLE_EMPTY = "";

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attribute)) return false;
        final Attribute that = (Attribute) obj;
        return Objects.equals(getTypeCatNonNull(), that.getTypeCatNonNull()) &&
               Objects.equals(getTypeSchemNonNull(), that.getTypeSchemNonNull()) &&
               Objects.equals(typeName, that.typeName) &&
               Objects.equals(attrName, that.attrName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getTypeCatNonNull(),
                getTypeSchemNonNull(),
                typeName,
                attrName
        );
    }

    String getTypeCatNonNull() {
        return Optional.ofNullable(getTypeCat()).orElse(Catalog.COLUMN_VALUE_TABLE_CAT_EMPTY);
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
        attributeId = null;
    }

    String getTypeSchemNonNull() {
        return Optional.ofNullable(getTypeSchem()).orElse(Schema.COLUMN_VALUE_TABLE_SCHEM_EMPTY);
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
        attributeId = null;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
        attributeId = null;
    }

    public void setAttrName(final String attrName) {
        this.attrName = attrName;
        attributeId = null;
    }

    @_NullableBySpecification
    @_ColumnLabel("TYPE_CAT")
    private String typeCat;

    @_NullableBySpecification
    @_ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @_ColumnLabel("TYPE_NAME")
    private String typeName;

    @_ColumnLabel("ATTR_NAME")
    private String attrName;

    @_ColumnLabel("DATA_TYPE")
    private int dataType;

    @_ColumnLabel("ATTR_TYPE_NAME")
    private String attrTypeName;

    @_ColumnLabel("ATTR_SIZE")
    private int attrSize;

    @_NullableBySpecification
    @_ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @_ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @_NotNull
    @_ColumnLabel(COLUMN_LABEL_NULLABLE)
    private Integer nullable;

    @_NullableBySpecification
    @_ColumnLabel("REMARKS")
    private String remarks;

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
    private int charOctetLength;

    @_ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @_ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @_NullableBySpecification
    @_ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @_NullableBySpecification
    @_ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @_NullableBySpecification
    @_ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @_NullableBySpecification
    @_ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;

    AttributeId getAttributeId() {
        if (attributeId == null) {
            attributeId = new AttributeId(
                    UDTId.of(
                            getTypeCatNonNull(),
                            getTypeSchemNonNull(),
                            getTypeName()
                    ),
                    getAttrName()
            );
        }
        return attributeId;
    }

    private UDTId getUDTId() {
        return getAttributeId().getUdtId();
    }

    TableId getScopeTableId() {
        if (dataType != Types.REF) {
            throw new IllegalStateException("dataType != Types.REF(" + Types.REF + ")");
        }
        return TableId.of(
                Objects.requireNonNull(getScopeCatalog(), "scopeCatalog is null"),
                Objects.requireNonNull(getScopeSchema(), "scopeSchema is null"),
                Objects.requireNonNull(getScopeTable(), "scopeTable is null")
        );
    }

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient AttributeId attributeId;
}
