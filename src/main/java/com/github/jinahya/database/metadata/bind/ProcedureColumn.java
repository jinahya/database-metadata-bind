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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String,
 * java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedureColumns(String, String, String, String, Collection)
 */
@XmlRootElement
public class ProcedureColumn implements MetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Constants for column types of procedure columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for {@link DatabaseMetaData#procedureColumnUnknown} whose value is {@value
         * DatabaseMetaData#procedureColumnUnknown}.
         */
        PROCEDURE_COLUMN_UNKNOWN(DatabaseMetaData.procedureColumnUnknown), // 0

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnIn} whose value is {@value
         * DatabaseMetaData#procedureColumnIn}.
         */
        PROCEDURE_COLUMN_IN(DatabaseMetaData.procedureColumnIn), // 1

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnInOut} whose value is {@value
         * DatabaseMetaData#procedureColumnInOut}.
         */
        PROCEDURE_COLUMN_IN_OUT(DatabaseMetaData.procedureColumnInOut), // 2

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnResult} whose value is {@value
         * DatabaseMetaData#procedureColumnResult}.
         */
        PROCEDURE_COLUMN_RESULT(DatabaseMetaData.functionColumnResult), // 3

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnOut} whose value is {@value
         * DatabaseMetaData#procedureColumnOut}.
         */
        PROCEDURE_COLUMN_OUT(DatabaseMetaData.procedureColumnOut), // 4

        /**
         * Constant for {@link DatabaseMetaData#procedureColumnReturn} whose value is {@value
         * DatabaseMetaData#procedureColumnReturn}.
         */
        PROCEDURE_COLUMN_RETURN(DatabaseMetaData.procedureColumnReturn); // 5

        /**
         * Returns the constant whose raw value equals to given. An instance of {@link IllegalArgumentException} will be
         * thrown if no constant matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static ColumnType valueOf(final int rawValue) {
            return IntFieldEnums.valueOfRawValue(ColumnType.class, rawValue);
        }

        ColumnType(final int rawValue) {
            this.rawValue = rawValue;
        }

        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public ProcedureColumn() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "procedureCat=" + procedureCat
               + ",procedureSchem=" + procedureSchem
               + ",procedureName=" + procedureName
               + ",columnName=" + columnName
               + ",columnType=" + columnType
               + ",dataType=" + dataType
               + ",typeName=" + typeName
               + ",precision=" + precision
               + ",length=" + length
               + ",scale=" + scale
               + ",radix=" + radix
               + ",nullable=" + nullable
               + ",remarks=" + remarks
               + ",columnDef=" + columnDef
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",charOctetLength=" + charOctetLength
               + ",ordinalPosition=" + ordinalPosition
               + ",isNullable=" + isNullable
               + ",specificName=" + specificName
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final ProcedureColumn that = (ProcedureColumn) obj;
        return columnType == that.columnType
               && dataType == that.dataType
               && precision == that.precision
               && length == that.length
               && radix == that.radix
               && nullable == that.nullable
               && ordinalPosition == that.ordinalPosition
               && Objects.equals(procedureCat, that.procedureCat)
               && Objects.equals(procedureSchem, that.procedureSchem)
               && Objects.equals(procedureName, that.procedureName)
               && Objects.equals(columnName, that.columnName)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(scale, that.scale)
               && Objects.equals(remarks, that.remarks)
               && Objects.equals(columnDef, that.columnDef)
               && Objects.equals(sqlDataType, that.sqlDataType)
               && Objects.equals(sqlDatetimeSub, that.sqlDatetimeSub)
               && Objects.equals(charOctetLength, that.charOctetLength)
               && Objects.equals(isNullable, that.isNullable)
               && Objects.equals(specificName, that.specificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(procedureCat,
                            procedureSchem,
                            procedureName,
                            columnName,
                            columnType,
                            dataType,
                            typeName,
                            precision,
                            length,
                            scale,
                            radix,
                            nullable,
                            remarks,
                            columnDef,
                            sqlDataType,
                            sqlDatetimeSub,
                            charOctetLength,
                            ordinalPosition,
                            isNullable,
                            specificName);
    }

    // ---------------------------------------------------------------------------------------------------- procedureCat
    public String getProcedureCat() {
        return procedureCat;
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // -------------------------------------------------------------------------------------------------- procedureSchem
    public String getProcedureSchem() {
        return procedureSchem;
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // --------------------------------------------------------------------------------------------------- procedureName
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // ------------------------------------------------------------------------------------------------------ columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // ------------------------------------------------------------------------------------------------------ columnType
    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(final short columnType) {
        this.columnType = columnType;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ------------------------------------------------------------------------------------------------------- precision
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    // ---------------------------------------------------------------------------------------------------------- length
    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    // ----------------------------------------------------------------------------------------------------------- scale
    public Short getScale() {
        return scale;
    }

    public void setScale(final Short scale) {
        this.scale = scale;
    }

    // ----------------------------------------------------------------------------------------------------------- radix
    public short getRadix() {
        return radix;
    }

    public void setRadix(final short radix) {
        this.radix = radix;
    }

    // -------------------------------------------------------------------------------------------------------- nullable
    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
        this.nullable = nullable;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------------------------------------------------- columnDef
    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(final String columnDef) {
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

    // ---------------------------------------------------------------------------------------------------- specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PROCEDURE_CAT")
    private String procedureCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("PROCEDURE_SCHEM")
    private String procedureSchem;

    @XmlElement(required = true)
    @Label("PROCEDURE_NAME")
    private String procedureName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("COLUMN_NAME")
    private String columnName;

    @XmlElement(required = true)
    @Label("COLUMN_TYPE")
    private short columnType;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(required = true)
    @Label("PRECISION")
    private int precision;

    @XmlElement(required = true)
    @Label("LENGTH")
    private int length;

    // https://issues.apache.org/jira/browse/DERBY-7103
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SCALE")
    private Short scale;

    @XmlElement(required = true)
    @Label("RADIX")
    private short radix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private short nullable;

    @XmlElement(required = true, nillable = true)
    @MayBeNullByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("COLUMN_DEF")
    private String columnDef;

    @XmlElement(required = true, nillable = true)
    @Reserved
    @Label("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @XmlElement(required = true, nillable = true)
    @Reserved
    @Label("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @XmlElement(required = true)
    @Label("ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement(required = true)
    @Label("IS_NULLABLE")
    private String isNullable;

    @XmlElement(required = true)
    @Label("SPECIFIC_NAME")
    private String specificName;
}
