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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for binding results of {@link DatabaseMetaData#getAttributes(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getAttributes(String, String, String, String)
 * @see NullableEnum
 */
@ChildOf(UDT.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public class Attribute extends AbstractMetadataType {

    private static final long serialVersionUID = 1913681105410440186L;

    public static final Comparator<Attribute> CASE_INSENSITIVE_ORDER =
            Comparator.comparing(Attribute::getAttributeId, AttributeId.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Attribute> LEXICOGRAPHIC_ORDER =
            Comparator.comparing(Attribute::getAttributeId, AttributeId.LEXICOGRAPHIC_ORDER);

    public static final String COLUMN_LABEL_NULLABLE = "NULLABLE";

    public enum NullableEnum implements _IntFieldEnum<NullableEnum> {

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
        public static NullableEnum valueOfNullable(final int nullable) {
            return _IntFieldEnum.valueOfFieldValue(NullableEnum.class, nullable);
        }

        NullableEnum(final int fieldValue) {
            this.fieldValue = fieldValue;
        }

        @Override
        public int fieldValueAsInt() {
            return fieldValue;
        }

        private final int fieldValue;
    }

    public static final String VALUE_IS_NULLABLE_YES = "YES";

    public static final String VALUE_IS_NULLABLE_NO = "NO";

    public static final String VALUE_IS_NULLABLE_EMPTY = "";

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attribute)) return false;
        final Attribute that = (Attribute) obj;
        return Objects.equals(getAttributeId(), that.getAttributeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttributeId());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public AttributeId getAttributeId() {
        if (attributeId == null) {
            attributeId = AttributeId.of(
                    UDTId.of(
                            getTypeCatNonNull(),
                            getTypeSchemNonNull(),
                            getTypeName()
                    ),
                    getAttrName(),
                    getOrdinalPosition()
            );
        }
        return attributeId;
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

    // -----------------------------------------------------------------------------------------------------------------
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

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
        attributeId = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient AttributeId attributeId;

    // -----------------------------------------------------------------------------------------------------------------
    @NullableBySpecification
    @ColumnLabel("TYPE_CAT")
    private String typeCat;

    @NullableBySpecification
    @ColumnLabel("TYPE_SCHEM")
    private String typeSchem;

    @ColumnLabel("TYPE_NAME")
    private String typeName;

    @ColumnLabel("ATTR_NAME")
    private String attrName;

    @ColumnLabel("DATA_TYPE")
    private int dataType;

    @ColumnLabel("ATTR_TYPE_NAME")
    private String attrTypeName;

    @ColumnLabel("ATTR_SIZE")
    private int attrSize;

    @NullableBySpecification
    @ColumnLabel("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @ColumnLabel("NUM_PREC_RADIX")
    private int numPrecRadix;

    @ColumnLabel(COLUMN_LABEL_NULLABLE)
    private int nullable;

    @NullableBySpecification
    @ColumnLabel("REMARKS")
    private String remarks;

    @NullableBySpecification
    @ColumnLabel("ATTR_DEF")
    private String attrDef;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @NotUsedBySpecification
    @ColumnLabel("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @ColumnLabel("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @ColumnLabel("ORDINAL_POSITION")
    private int ordinalPosition;

    @ColumnLabel("IS_NULLABLE")
    private String isNullable;

    @NullableBySpecification
    @ColumnLabel("SCOPE_CATALOG")
    private String scopeCatalog;

    @NullableBySpecification
    @ColumnLabel("SCOPE_SCHEMA")
    private String scopeSchema;

    @NullableBySpecification
    @ColumnLabel("SCOPE_TABLE")
    private String scopeTable;

    @NullableBySpecification
    @ColumnLabel("SOURCE_DATA_TYPE")
    private Integer sourceDataType;
}
