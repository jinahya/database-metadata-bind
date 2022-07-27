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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.Objects;

/**
 * A class for binding results of {@link DatabaseMetaData#getAttributes(String, String, String, String)} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getAttributes(String, String, String, String, Collection)
 * @see Context#getAttributes(UDT, String, Collection)
 */
@XmlRootElement
@ChildOf(UDT.class)
@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Attribute
        implements MetadataType {

    private static final long serialVersionUID = 5020389308460154799L;

    /**
     * Constants for nullabilities of an attribute.
     */
    @XmlEnum
    public enum Nullable implements IntFieldEnum<Nullable> {

        /**
         * Constant for {@link DatabaseMetaData#attributeNoNulls}({@value java.sql.DatabaseMetaData#attributeNoNulls}).
         */
        ATTRIBUTE_NO_NULLS(DatabaseMetaData.attributeNoNulls),

        /**
         * Constant for
         * {@link DatabaseMetaData#attributeNullable}({@value java.sql.DatabaseMetaData#attributeNullable}).
         */
        ATTRIBUTE_NULLABLE(DatabaseMetaData.attributeNullable),

        /**
         * Constant for
         * {@link DatabaseMetaData#attributeNullableUnknown}({@value
         * java.sql.DatabaseMetaData#attributeNullableUnknown}).
         */
        ATTRIBUTE_NULLABLE_UNKNOWN(DatabaseMetaData.attributeNullableUnknown);

        /**
         * Returns the value whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the {@code rawValue} to match.
         * @return a matched value.
         */
        public static Nullable valueOfRawValue(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Nullable.class, rawValue);
        }

        Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    public static final String VALUE_IS_NULLABLE_YES = "YES";

    public static final String VALUE_IS_NULLABLE_NO = "NO";

    public static final String VALUE_IS_NULLABLE_EMPTY = "";

    @XmlEnum
    public enum IsNullable implements FieldEnum<IsNullable, String> {

        @XmlEnumValue(VALUE_IS_NULLABLE_YES)
        YES(VALUE_IS_NULLABLE_YES),

        @XmlEnumValue(VALUE_IS_NULLABLE_NO)
        NO(VALUE_IS_NULLABLE_NO),

        @XmlEnumValue(VALUE_IS_NULLABLE_EMPTY)
        EMPTY(VALUE_IS_NULLABLE_EMPTY);

        /**
         * Returns the value whose {@link #getRawValue() rawValue} matches to specified value.
         *
         * @param rawValue the {@code rawValue} to match.
         * @return a matched value.
         */
        public static IsNullable valueOfRawValue(final String rawValue) {
            return FieldEnums.valueOfRawValue(IsNullable.class, rawValue);
        }

        IsNullable(final String rawValue) {
            this.rawValue = Objects.requireNonNull(rawValue, "rawValue is null");
        }

        @Override
        public String getRawValue() {
            return rawValue;
        }

        private final String rawValue;
    }

    public Nullable getNullableAsEnum() {
        return Nullable.valueOfRawValue(getNullable());
    }

    public void setNullableAsEnum(final Nullable nullableAsEnum) {
        setNullable(Objects.requireNonNull(nullableAsEnum, "nullableAsEnum is null").getRawValue());
    }

    public IsNullable getIsNullableAsEnum() {
        return IsNullable.valueOfRawValue(getIsNullable());
    }

    public void setIsNullableAsEnum(final IsNullable isNullableAsEnum) {
        setIsNullable(Objects.requireNonNull(isNullableAsEnum, "isNullableAsEnum is null").getRawValue());
    }

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(nillable = false, required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = false, required = true)
    @Label("ATTR_NAME")
    private String attrName;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @Label("ATTR_TYPE_NAME")
    private String attrTypeName;

    @XmlElement(nillable = false, required = true)
    @Label("ATTR_SIZE")
    private int attrSize;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(nillable = false, required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("ATTR_DEF")
    private String attrDef;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @NotUsedBySpecification
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(nillable = false, required = true)
    @Label("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(nillable = false, required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(nillable = false, required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("SOURCE_DATA_TYPE")
    private Integer sourceDataType;
}
