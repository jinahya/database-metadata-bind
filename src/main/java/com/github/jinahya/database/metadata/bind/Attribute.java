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

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Objects;

import static java.sql.DatabaseMetaData.attributeNoNulls;
import static java.sql.DatabaseMetaData.attributeNullable;
import static java.sql.DatabaseMetaData.attributeNullableUnknown;

/**
 * An entity class for type attributes.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getAttributes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
@XmlRootElement
public class Attribute extends UDTChild {

    private static final long serialVersionUID = 5020389308460154799L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for nullabilities of an attribute.
     */
    public enum Nullable implements IntFieldEnum<Nullable> {

        // -------------------------------------------------------------------------------------------------------------
        /**
         * Constant for {@link DatabaseMetaData#attributeNoNulls}.
         */
        ATTRIBUTE_NO_NULLS(attributeNoNulls),

        /**
         * Constant for {@link DatabaseMetaData#attributeNullable}.
         */
        ATTRIBUTE_NULLABLE(attributeNullable),

        /**
         * Constant for {@link DatabaseMetaData#attributeNullableUnknown}.
         */
        ATTRIBUTE_NULLABLE_UNKNOWN(attributeNullableUnknown);

        // -------------------------------------------------------------------------------------------------------------
        public static Nullable valueOf(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(Nullable.class, rawValue);
        }

        // -------------------------------------------------------------------------------------------------------------
        Nullable(final int rawValue) {
            this.rawValue = rawValue;
        }

        // -------------------------------------------------------------------------------------------------------------
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // -------------------------------------------------------------------------------------------------------------
        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Attribute() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",attrName=" + attrName
               + ",dataType=" + dataType
               + ",attrTypeName=" + attrTypeName
               + ",attrSize=" + attrSize
               + ",decimalDigits=" + decimalDigits
               + ",numPrecRadix=" + numPrecRadix
               + ",nullable=" + nullable
               + ",remarks=" + remarks
               + ",attrDef=" + attrDef
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",sourceDataType=" + sourceDataType
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Attribute that = (Attribute) obj;
        return dataType == that.dataType
               && attrSize == that.attrSize
               && numPrecRadix == that.numPrecRadix
               && nullable == that.nullable
               && charOctetLength == that.charOctetLength
               && ordinalPosition == that.ordinalPosition
               && Objects.equals(typeCat, that.typeCat)
               && Objects.equals(typeSchem, that.typeSchem)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(attrName, that.attrName)
               && Objects.equals(attrTypeName, that.attrTypeName)
               && Objects.equals(decimalDigits, that.decimalDigits)
               && Objects.equals(remarks, that.remarks)
               && Objects.equals(attrDef, that.attrDef)
               && Objects.equals(sqlDataType, that.sqlDataType)
               && Objects.equals(sqlDatetimeSub, that.sqlDatetimeSub)
               && Objects.equals(isNullable, that.isNullable)
               && Objects.equals(sourceDataType, that.sourceDataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCat,
                            typeSchem,
                            typeName,
                            attrName,
                            dataType,
                            attrTypeName,
                            attrSize,
                            decimalDigits,
                            numPrecRadix,
                            nullable,
                            remarks,
                            attrDef,
                            sqlDataType,
                            sqlDatetimeSub,
                            charOctetLength,
                            ordinalPosition,
                            isNullable,
                            sourceDataType);
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // -------------------------------------------------------------------------------------------------------- attrName
    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(final String attrName) {
        this.attrName = attrName;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // ---------------------------------------------------------------------------------------------------- attrTypeName
    public String getAttrTypeName() {
        return attrTypeName;
    }

    public void setAttrTypeName(final String attrTypeName) {
        this.attrTypeName = attrTypeName;
    }

    // -------------------------------------------------------------------------------------------------------- attrSize
    public int getAttrSize() {
        return attrSize;
    }

    public void setAttrSize(final int attrSize) {
        this.attrSize = attrSize;
    }

    // --------------------------------------------------------------------------------------------------- decimalDigits
    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(final Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    // ---------------------------------------------------------------------------------------------------- numPrecRadix
    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public void setNumPrecRadix(final int numPrecRadix) {
        this.numPrecRadix = numPrecRadix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public int getNullable() {
        return nullable;
    }

    public void setNullable(final int nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------------------------------------------------- attrDef
    public String getAttrDef() {
        return attrDef;
    }

    public void setAttrDef(final String attrDef) {
        this.attrDef = attrDef;
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
    public int getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final int charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // ------------------------------------------------------------------------------------------------- ordinalPosition
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
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
    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public void setScopeCatalog(String scopeCatalog) {
        this.scopeCatalog = scopeCatalog;
    }

    // ----------------------------------------------------------------------------------------------------- scopeSchema
    public String getScopeSchema() {
        return scopeSchema;
    }

    public void setScopeSchema(final String scopeSchema) {
        this.scopeSchema = scopeSchema;
    }

    // ------------------------------------------------------------------------------------------------------ scopeTable
    public String getScopeTable() {
        return scopeTable;
    }

    public void setScopeTable(final String scopeTable) {
        this.scopeTable = scopeTable;
    }

    // -------------------------------------------------------------------------------------------------- sourceDataType
    public Short getSourceDataType() {
        return sourceDataType;
    }

    public void setSourceDataType(final Short sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @MayBeNull
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @MayBeNull
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(required = true)
    @NotBlank
    @Label("TYPE_NAME")
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
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
    @MayBeNull
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
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
    @MayBeNull
    @Label("SCOPE_CATALOG")
    private String scopeCatalog;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_SCHEMA")
    private String scopeSchema;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCOPE_TABLE")
    private String scopeTable;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SOURCE_DATA_TYPE")
    private Short sourceDataType;
}
