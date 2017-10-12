/*
 * Copyright 2013 Jin Kwon <onacit at gmail.com>.
 *
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
 */
package com.github.jinahya.database.metadata.bind;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import static java.sql.DatabaseMetaData.functionColumnResult;
import static java.sql.DatabaseMetaData.procedureColumnIn;
import static java.sql.DatabaseMetaData.procedureColumnInOut;
import static java.sql.DatabaseMetaData.procedureColumnOut;
import static java.sql.DatabaseMetaData.procedureColumnReturn;
import static java.sql.DatabaseMetaData.procedureColumnUnknown;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getProcedureColumns(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "columnName", "columnType", "dataType", "typeName",
    "precision", "length", "scale", "radix", "nullable", "remarks",
    "columnDef", "sqlDataType", "sqlDatetimeSub", "charOctetLength",
    "ordinalPosition", "isNullable", "specificName"
})
public class ProcedureColumn implements Serializable {

    private static final long serialVersionUID = 3894753719381358829L;

    // -------------------------------------------------------------------------
    private static final Logger logger
            = getLogger(ProcedureColumn.class.getName());

    // -------------------------------------------------------------------------
    /**
     * Constants for column types of procedure columns.
     *
     * @see DatabaseMetaData#getFunctionColumns(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    public static enum ColumnType implements IntFieldEnum<ColumnType> {

        /**
         * Constant for {@link DatabaseMetaData#procedureColumnUnknown} whose
         * value is {@value DatabaseMetaData#procedureColumnUnknown}.
         */
        PROCEDURE_COLUMN_UNKNOWN(procedureColumnUnknown), // 0
        /**
         * Constants for {@link DatabaseMetaData#procedureColumnIn} whose value
         * is {@value DatabaseMetaData#procedureColumnIn}.
         */
        PROCEDURE_COLUMN_IN(procedureColumnIn), // 1
        /**
         * Constants for {@link DatabaseMetaData#procedureColumnInOut} whose
         * value is {@value DatabaseMetaData#procedureColumnInOut}.
         */
        PROCEDURE_COLUMN_IN_OUT(procedureColumnInOut), // 2
        /**
         * Constants for {@link DatabaseMetaData#procedureColumnResult} whose
         * value is {@value DatabaseMetaData#procedureColumnResult}.
         */
        PROCEDURE_COLUMN_RESULT(functionColumnResult), // 3
        /**
         * Constants for {@link DatabaseMetaData#procedureColumnOut} whose value
         * is {@value DatabaseMetaData#procedureColumnOut}.
         */
        PROCEDURE_COLUMN_OUT(procedureColumnOut), // 4
        /**
         * Constant for {@link DatabaseMetaData#procedureColumnReturn} whose
         * value is {@value DatabaseMetaData#procedureColumnReturn}.
         */
        PROCEDURE_COLUMN_RETURN(procedureColumnReturn); // 5

        // ---------------------------------------------------------------------
        /**
         * Returns the constant whose raw value equals to given. An instance of
         * {@link IllegalArgumentException} will be thrown if no constant
         * matches.
         *
         * @param rawValue the raw value
         * @return the constant whose raw value equals to given.
         */
        public static ColumnType valueOf(final int rawValue) {
            return IntFieldEnums.valueOf(ColumnType.class, rawValue);
        }

        // ---------------------------------------------------------------------
        private ColumnType(final int rawValue) {
            this.rawValue = rawValue;
        }

        // ---------------------------------------------------------------------
        /**
         * Returns the raw value of this constant.
         *
         * @return the raw value of this constant.
         */
        @Override
        public int getRawValue() {
            return rawValue;
        }

        // ---------------------------------------------------------------------
        private final int rawValue;
    }

    // -------------------------------------------------------------------------
    /**
     * Creates a new instance.
     */
    public ProcedureColumn() {
        super();
    }

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
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
               + "}";
    }

    // ------------------------------------------------------------ procedureCat
    public String getProcedureCat() {
        return procedureCat;
    }

    public void setProcedureCat(final String procedureCat) {
        this.procedureCat = procedureCat;
    }

    // ---------------------------------------------------------- procedureSchem
    public String getProcedureSchem() {
        return procedureSchem;
    }

    public void setProcedureSchem(final String procedureSchem) {
        this.procedureSchem = procedureSchem;
    }

    // ----------------------------------------------------------- procedureName
    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(final String procedureName) {
        this.procedureName = procedureName;
    }

    // -------------------------------------------------------------- columnName
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    // -------------------------------------------------------------- columnType
    public short getColumnType() {
        return columnType;
    }

    public void setColumnType(final short columnType) {
        this.columnType = columnType;
    }

    // ---------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // ---------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // --------------------------------------------------------------- precision
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    // ------------------------------------------------------------------ length
    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    // ------------------------------------------------------------------- scale
    public Short getScale() {
        return scale;
    }

    public void setScale(final Short scale) {
        this.scale = scale;
    }

    // ------------------------------------------------------------------- radix
    public short getRadix() {
        return radix;
    }

    public void setRadix(final short radix) {
        this.radix = radix;
    }

    // ---------------------------------------------------------------- nullable
    public short getNullable() {
        return nullable;
    }

    public void setNullable(final short nullable) {
        this.nullable = nullable;
    }

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // --------------------------------------------------------------- columnDef
    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(final String columnDef) {
        this.columnDef = columnDef;
    }

    // ------------------------------------------------------------- sqlDataType
    public Integer getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(final Integer sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    // ---------------------------------------------------------- sqlDatetimeSub
    public Integer getSqlDatetimeSub() {
        return sqlDatetimeSub;
    }

    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
        this.sqlDatetimeSub = sqlDatetimeSub;
    }

    // --------------------------------------------------------- charOctetLength
    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public void setCharOctetLength(final Integer charOctetLength) {
        this.charOctetLength = charOctetLength;
    }

    // --------------------------------------------------------- ordinalPosition
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(final int ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    // -------------------------------------------------------------- isNullable
    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(final String isNullable) {
        this.isNullable = isNullable;
    }

    // ------------------------------------------------------------ specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Label("PROCEDURE_CAT")
    @Bind(label = "PROCEDURE_CAT", nillable = true)
    @Nillable
    private String procedureCat;

    @XmlAttribute
    @Label("PROCEDURE_SCHEM")
    @Bind(label = "PROCEDURE_SCHEM", nillable = true)
    @Nillable
    private String procedureSchem;

    @XmlAttribute
    @Label("PROCEDURE_NAME")
    @Bind(label = "PROCEDURE_NAME")
    private String procedureName;

    // -------------------------------------------------------------------------
    @XmlElement
    @Label("COLUMN_NAME")
    @Bind(label = "COLUMN_NAME")
    private String columnName;

    @XmlElement
    @Label("COLUMN_TYPE")
    @Bind(label = "COLUMN_TYPE")
    private short columnType;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Label("PRECISION")
    @Bind(label = "PRECISION")
    private int precision;

    @XmlElement
    @Label("LENGTH")
    @Bind(label = "LENGTH")
    private int length;

    @XmlElement
    @Label("SCALE")
    @Bind(label = "SCALE", nillable = true)
    @Nillable
    private Short scale;

    @XmlElement
    @Label("RADIX")
    @Bind(label = "RADIX")
    private short radix;

    @XmlElement
    @Label("NULLABLE")
    @Bind(label = "NULLABLE")
    private short nullable;

    @XmlElement
    @Label("REMARKS")
    @Bind(label = "REMARKS")
    private String remarks;

    @XmlElement(nillable = true)
    @Label("COLUMN_DEF")
    @Bind(label = "COLUMN_DEF", nillable = true)
    @Nillable
    private String columnDef;

    @XmlElement(nillable = true)
    @Label("SQL_DATA_TYPE")
    @Bind(label = "SQL_DATA_TYPE", reserved = true)
    @Reserved
    private Integer sqlDataType;

    @XmlElement(nillable = true)
    @Label("SQL_DATETIME_SUB")
    @Bind(label = "SQL_DATETIME_SUB", reserved = true)
    @Reserved
    private Integer sqlDatetimeSub;

    @XmlElement(nillable = true)
    @Label("CHAR_OCTET_LENGTH")
    @Bind(label = "CHAR_OCTET_LENGTH", nillable = true)
    @Nillable
    private Integer charOctetLength;

    @XmlElement(nillable = true)
    @Label("ORDINAL_POSITION")
    @Bind(label = "ORDINAL_POSITION")
    private int ordinalPosition;

    @XmlElement
    @Label("IS_NULLABLE")
    @Bind(label = "IS_NULLABLE")
    private String isNullable;

    @XmlElement
    @Label("SPECIFIC_NAME")
    @Bind(label = "SPECIFIC_NAME")
    private String specificName;

    // -------------------------------------------------------------------------
    @Deprecated
    private Procedure procedure;
}
