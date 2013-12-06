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


package com.github.jinahya.sql.databasemetadata;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "attrName", "dataType", "attrTypeName", "attrSize", "decimalDigits",
        "numPrecRadix", "nullable", "remarks", "attrDef", "charOctetLength",
        "ordinalPosition", "isNullable", "sourceDataType"
    }
)
public class Attribute {


    /**
     * logger.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(Attribute.class);


    /**
     *
     * @param suppression
     * @param resultSet
     *
     * @return
     *
     * @throws SQLException
     */
    public static Attribute retrieve(final Suppression suppression,
                                     final ResultSet resultSet)
        throws SQLException {

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(resultSet, "null resultSet");

        final Attribute instance = new Attribute();

        ColumnRetriever.retrieve(
            Attribute.class, instance, suppression, resultSet);

        return instance;
    }


    /**
     *
     * @param database
     * @param suppression
     * @param catalog
     * @param schemaPattern
     * @param typeNamePattern
     * @param attributeNamePattern
     * @param attributes
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getColumns(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final String catalog,
                                final String schemaPattern,
                                final String typeNamePattern,
                                final String attributeNamePattern,
                                final Collection<? super Attribute> attributes)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(attributes, "null attributes");

        if (suppression.isSuppressed(
            UserDefinedType.SUPPRESSION_PATH_ATTRIBUTES)) {
            return;
        }

        final ResultSet resultSet = database.getColumns(
            catalog, schemaPattern, typeNamePattern, attributeNamePattern);
        try {
            while (resultSet.next()) {
                attributes.add(retrieve(suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    /**
     *
     * @param database
     * @param suppression
     * @param userDefinedType
     *
     * @throws SQLException if a database access error occurs
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final UserDefinedType userDefinedType)
        throws SQLException {

        Objects.requireNonNull(database, "null database");

        Objects.requireNonNull(suppression, "null suppression");

        Objects.requireNonNull(userDefinedType, "null userDefinedType");

        retrieve(database, suppression,
                 userDefinedType.getSchema().getCatalog().getTableCat(),
                 userDefinedType.getSchema().getTableSchem(),
                 userDefinedType.getTypeName(), null,
                 userDefinedType.getAttributes());

        for (final Attribute child : userDefinedType.getAttributes()) {
            child.setUserDefinedType(userDefinedType);
        }
    }


    /**
     * Creates a new instance.
     */
    public Attribute() {

        super();
    }


    // --------------------------------------------------------- userDefinedType
    public UserDefinedType getUserDefinedType() {

        return userDefinedType;
    }


    public void setUserDefinedType(final UserDefinedType userDefinedType) {

        this.userDefinedType = userDefinedType;
    }


    // ---------------------------------------------------------------- attrName
    public String getAttrName() {

        return attrName;
    }


    public void setAttrName(final String attrName) {

        this.attrName = attrName;
    }


    // ---------------------------------------------------------------- dataType
    public int getDataType() {

        return dataType;
    }


    public void setDataType(final int dataType) {

        this.dataType = dataType;
    }


    // ------------------------------------------------------------ attrTypeName
    public String getAttrTypeName() {

        return attrTypeName;
    }


    public void setAttrTypeName(final String attrTypeName) {

        this.attrTypeName = attrTypeName;
    }


    // ---------------------------------------------------------------- attrSize
    public int getAttrSize() {

        return attrSize;
    }


    public void setAttrSize(final int attrSize) {

        this.attrSize = attrSize;
    }


    // ----------------------------------------------------------- decimalDigits
    public Integer getDecimalDigits() {

        return decimalDigits;
    }


    public void setDecimalDigits(final Integer decimalDigits) {

        this.decimalDigits = decimalDigits;
    }


    // ------------------------------------------------------------ numPrecRadix
    public int getNumPrecRadix() {

        return numPrecRadix;
    }


    public void setNumPrecRadix(final int numPrecRadix) {

        this.numPrecRadix = numPrecRadix;
    }


    // ---------------------------------------------------------------- NULLABLE
    public int getNullable() {

        return nullable;
    }


    public void setNullable(final int nullable) {

        this.nullable = nullable;
    }


    // ----------------------------------------------------------------- REMARKS
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // --------------------------------------------------------------- columnDef
    public String getColumnDef() {

        return attrDef;
    }


    public void setColumnDef(final String columnDef) {

        this.attrDef = columnDef;
    }


    // --------------------------------------------------------- charOctetLength
    public int getCharOctetLength() {

        return charOctetLength;
    }


    public void setCharOctetLength(final int charOctetLength) {

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


    // ---------------------------------------------------------- sourceDataType
    public Short getSourceDataType() {

        return sourceDataType;
    }


    public void setSourceDataType(final Short sourceDataType) {

        this.sourceDataType = sourceDataType;
    }


    /**
     * type catalog (may be {@code null})
     */
    @ColumnLabel("TYPE_CAT")
    @SuppressionPath("attribute/typeCat")
    @XmlAttribute
    protected String typeCat;


    /**
     * type schema (may be {@code null})
     */
    @ColumnLabel("TYPE_SCHEM")
    @SuppressionPath("attribute/typeSchem")
    @XmlAttribute
    protected String typeSchem;


    /**
     * type name.
     */
    @ColumnLabel("TYPE_NAME")
    @XmlAttribute
    protected String typeName;


    @XmlTransient
    private UserDefinedType userDefinedType;


    /**
     * attribute name.
     */
    @ColumnLabel("ATTR_NAME")
    @XmlElement(required = true)
    protected String attrName;


    /**
     * attribute type SQL type from {@link java.sql.Types}.
     */
    @ColumnLabel("DATA_TYPE")
    @XmlElement(required = true)
    protected int dataType;


    /**
     * Data source dependent type name. For a UDT, the type name is fully
     * qualified. For a REF, the type name is fully qualified and represents the
     * target type of the reference type.
     */
    @ColumnLabel("ATTR_TYPE_NAME")
    @XmlElement(required = true)
    protected String attrTypeName;


    /**
     * column size. For char or date types this is the maximum number of
     * characters; for numeric or decimal types this is precision.
     */
    @ColumnLabel("ATTR_SIZE")
    @XmlElement(required = true)
    protected int attrSize;


    /**
     * the number of fractional digits. Null is returned for data types where
     * DECIMAL_DIGITS is not applicable.
     */
    @ColumnLabel("DECIMAL_DIGITS")
    @XmlElement(required = true)
    protected Integer decimalDigits;


    /**
     * Radix (typically either 10 or 2).
     */
    @ColumnLabel("NUM_PREC_RADIX")
    @XmlElement(required = true)
    protected int numPrecRadix;


    /**
     * whether NULL is allowed.
     */
    @ColumnLabel("NULLABLE")
    @XmlElement(required = true)
    protected int nullable;


    /**
     * comment describing column.
     */
    @ColumnLabel("REMARKS")
    @XmlElement(nillable = true, required = true)
    @XmlNillableBySpecification
    protected String remarks;


    /**
     * default value.
     */
    @ColumnLabel("ATTR_DEF")
    @XmlElement(nillable = true, required = true)
    @XmlNillableBySpecification
    protected String attrDef;


    @ColumnLabel("SQL_DATA_TYPE")
    @NotUsed
    protected int sqlDataType;


    @ColumnLabel("SQL_DATETIME_SUB")
    @NotUsed
    protected int sqlDatetimeSub;


    /**
     * for char types the maximum number of bytes in the column.
     */
    @ColumnLabel("CHAR_OCTET_LENGTH")
    @XmlElement(required = true)
    protected int charOctetLength;


    /**
     * index of the attribute in the UDT (starting at 1).
     */
    @ColumnLabel("ORDINAL_POSITION")
    @XmlElement(required = true)
    protected int ordinalPosition;


    /**
     * ISO rules are used to determine the nullability for a attribute.
     */
    @ColumnLabel("IS_NULLABLE")
    @XmlElement(required = true)
    protected String isNullable;


    /**
     * source type of a distinct type or user-generated Ref type,SQL type from
     * {@link java.sql.Types} ({@code null} if {@link #data DATA_TYPE} isn't
     * {@link java.sql.Types#DISTINCT DISTINCT} or user-generated
     * {@link java.sql.Types#REF REF})
     */
    @ColumnLabel("SOURCE_DATA_TYPE")
    @XmlElement(nillable = true, required = true)
    @XmlNillableBySpecification()
    protected Short sourceDataType;


}

