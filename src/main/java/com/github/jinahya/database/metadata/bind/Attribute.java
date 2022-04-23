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

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;

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

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(required = true)
    @NotBlank
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("ATTR_NAME")
    private String attrName;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("ATTR_TYPE_NAME")
    private String attrTypeName;

    @XmlElement(required = true)
    @Label("ATTR_SIZE")
    private int attrSize;

    @XmlElement(required = true)
    @Label("DECIMAL_DIGITS")
    private Integer decimalDigits;

    @XmlElement(required = true)
    @Label("NUM_PREC_RADIX")
    private int numPrecRadix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private int nullable;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("ATTR_DEF")
    private String attrDef;

    @XmlElement(nillable = true, required = true)
    @Unused
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(required = true, nillable = true)
    @Unused
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(required = true)
    @Label("CHAR_OCTET_LENGTH")
    private int charOctetLength;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("SOURCE_DATA_TYPE")
    private Short sourceDataType;
}
