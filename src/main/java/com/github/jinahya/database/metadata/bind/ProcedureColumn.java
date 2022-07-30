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

/**
 * A class for binding results of
 * {@link DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String,
 * java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getProcedureColumns(String, String, String, String, Collection)
 */
@XmlRootElement
@ChildOf(Procedure.class)
public class ProcedureColumn
        implements MetadataType {

    private static final long serialVersionUID = 3894753719381358829L;

    /**
     * Constants for column types of procedure columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for
         * {@link DatabaseMetaData#procedureColumnUnknown}({@value java.sql.DatabaseMetaData#procedureColumnUnknown}).
         */
        PROCEDURE_COLUMN_UNKNOWN(DatabaseMetaData.procedureColumnUnknown), // 0

        /**
         * Constants for
         * {@link DatabaseMetaData#procedureColumnIn}({@value java.sql.DatabaseMetaData#procedureColumnIn}).
         */
        PROCEDURE_COLUMN_IN(DatabaseMetaData.procedureColumnIn), // 1

        /**
         * Constants for
         * {@link DatabaseMetaData#procedureColumnInOut}({@value java.sql.DatabaseMetaData#procedureColumnInOut}).
         */
        PROCEDURE_COLUMN_IN_OUT(DatabaseMetaData.procedureColumnInOut), // 2

        /**
         * Constants for
         * {@link DatabaseMetaData#procedureColumnResult}({@value java.sql.DatabaseMetaData#procedureColumnResult}).
         */
        PROCEDURE_COLUMN_RESULT(DatabaseMetaData.functionColumnResult), // 3

        /**
         * Constants for {@link DatabaseMetaData#procedureColumnOut}({@value DatabaseMetaData#procedureColumnOut}).
         */
        PROCEDURE_COLUMN_OUT(DatabaseMetaData.procedureColumnOut), // 4

        /**
         * Constant for
         * {@link DatabaseMetaData#procedureColumnReturn}({@value java.sql.DatabaseMetaData#procedureColumnReturn}).
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
        public int rawValue() {
            return rawValue;
        }

        private final int rawValue;
    }

    /**
     * Creates a new instance.
     */
    public ProcedureColumn() {
        super();
    }

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

    public String getProcedureCat() {
        return procedureCat;
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    public String getProcedureSchem() {
        return procedureSchem;
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(final short columnType) {
        this.columnType = columnType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public Short getScale() {
        return scale;
    }

    public void setScale(final Short scale) {
        this.scale = scale;
    }

    public short getRadix() {
        return radix;
    }

    public void setRadix(final short radix) {
        this.radix = radix;
    }

    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
    }

    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PROCEDURE_CAT")
    private String procedureCat;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
    @Label("PROCEDURE_SCHEM")
    private String procedureSchem;

    @XmlElement(required = true)
    @Label("PROCEDURE_NAME")
    private String procedureName;

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
    @NullableBySpecification
    @Label("SCALE")
    private Short scale;

    @XmlElement(required = true)
    @Label("RADIX")
    private short radix;

    @XmlElement(required = true)
    @Label("NULLABLE")
    private short nullable;

    @XmlElement(required = true, nillable = true)
    @NullableByVendor("derby") // https://issues.apache.org/jira/browse/DERBY-7101
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @NullableBySpecification
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
    @NullableBySpecification
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
