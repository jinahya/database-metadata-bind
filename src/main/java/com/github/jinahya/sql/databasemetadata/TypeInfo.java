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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlType(
    propOrder = {
        "typeName", "dataType", "precision", "literalPrefix", "literalSuffix",
        "createParams", "nullable", "caseSensitive", "searchable",
        "unsignedAttribute", "fixedPrecScale", "autoIncrement", "localTypeName",
        "minimumScale", "maximumScale", "sqlDataType", "sqlDatetimeSub",
        "numPrecRadix"
    }
)
public class TypeInfo {


    /**
     *
     * @param database
     * @param suppression
     * @param typeInfo
     *
     * @throws SQLException if a database access error occurs.
     *
     * @see DatabaseMetaData#getTypeInfo()
     */
    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Collection<? super TypeInfo> typeInfo)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (typeInfo == null) {
            throw new NullPointerException("null typeInfo");
        }

        if (suppression.isSuppressed(Metadata.SUPPRESSION_PATH_TYPE_INFO)) {
            return;
        }

        final ResultSet resultSet = database.getTypeInfo();
        try {
            while (resultSet.next()) {
                typeInfo.add(ColumnRetriever.retrieve(
                    TypeInfo.class, suppression, resultSet));
            }
        } finally {
            resultSet.close();
        }
    }


    public static void retrieve(final DatabaseMetaData database,
                                final Suppression suppression,
                                final Metadata metadata)
        throws SQLException {

        if (database == null) {
            throw new NullPointerException("null database");
        }

        if (suppression == null) {
            throw new NullPointerException("null suppression");
        }

        if (metadata == null) {
            throw new NullPointerException("null metadata");
        }

        retrieve(database, suppression, metadata.getTypeInfo());

        for (final TypeInfo typeInfo_ : metadata.getTypeInfo()) {
            typeInfo_.metadata = metadata;
        }
    }


    /**
     * Creates a new instance.
     */
    public TypeInfo() {

        super();
    }


    // ---------------------------------------------------------------- metadata
    public Metadata getMetadata() {

        return metadata;
    }


    // ---------------------------------------------------------------- typeName
    public String getTypeName() {

        return typeName;
    }


    public void setTypeName(final String typeName) {

        this.typeName = typeName;
    }


    // ------------------------------------------------------------- getDataType
    public int getDataType() {

        return dataType;
    }


    public void setDataType(final int dataType) {

        this.dataType = dataType;
    }


    // --------------------------------------------------------------- precesion
    public int getPrecision() {

        return precision;
    }


    public void setPrecision(final int precision) {

        this.precision = precision;
    }


    // ----------------------------------------------------------- literalPrefix
    public String getLiteralPrefix() {

        return literalPrefix;
    }


    public void setLiteralPrefix(final String literalPrefix) {

        this.literalPrefix = literalPrefix;
    }


    // ----------------------------------------------------------- literalSuffix
    public String getLiteralSuffix() {

        return literalSuffix;
    }


    public void setLiteralSuffix(final String literalSuffix) {

        this.literalSuffix = literalSuffix;
    }


    // ------------------------------------------------------------ createParams
    public String getCreateParams() {

        return createParams;
    }


    public void setCreateParams(final String createParams) {

        this.createParams = createParams;
    }


    // ---------------------------------------------------------------- nullable
    public short getNullable() {

        return nullable;
    }


    public void setNullable(final short nullable) {

        this.nullable = nullable;
    }


    // ----------------------------------------------------------- caseSensitive
    public boolean getCaseSensitive() {
        return caseSensitive;
    }


    public void setCaseSensitive(final boolean caseSensitive) {

        this.caseSensitive = caseSensitive;
    }


    // -------------------------------------------------------------- searchable
    public short getSearchable() {
        return searchable;
    }


    public void setSearchable(final short searchable) {

        this.searchable = searchable;
    }


    // ------------------------------------------------------- unsignedAttribute
    public boolean getUnsignedAttribute() {

        return unsignedAttribute;
    }


    public void setUnsignedAttribute(final boolean unsignedAttribute) {

        this.unsignedAttribute = unsignedAttribute;
    }


    // ---------------------------------------------------------- fixedPrecScale
    public boolean getFixedPrecScale() {

        return fixedPrecScale;
    }


    public void setFixedPrecScale(final boolean fixedPrecScale) {

        this.fixedPrecScale = fixedPrecScale;
    }


    // ----------------------------------------------------------- autoIncrement
    public boolean getAutoIncrement() {

        return autoIncrement;
    }


    public void setAutoIncrement(final boolean autoIncrement) {

        this.autoIncrement = autoIncrement;
    }


    // ----------------------------------------------------------- localTypeName
    public String getLocalTypeName() {

        return localTypeName;
    }


    public void setLocalTypeName(final String localTypeName) {

        this.localTypeName = localTypeName;
    }


    // ------------------------------------------------------------ minimumScale
    public short getMinimumScale() {

        return minimumScale;
    }


    public void setMinimumScale(final short minimumScale) {

        this.minimumScale = minimumScale;
    }


    // ------------------------------------------------------------ maximumScale
    public short getMaximumScale() {

        return maximumScale;
    }


    public void setMaximumScale(final short maximumScale) {

        this.maximumScale = maximumScale;
    }


    // ------------------------------------------------------------ numPrecRadix
    public int getNumPrecRadix() {

        return numPrecRadix;
    }


    public void setNumPrecRadix(final int numPrecRadix) {

        this.numPrecRadix = numPrecRadix;
    }


    /**
     * parent metadata.
     */
    @XmlTransient
    private Metadata metadata;


    /**
     * Type name.
     */
    @ColumnLabel("TYPE_NAME")
    @SuppressionPath("typeInfo/typeName")
    @XmlElement(required = true)
    String typeName;


    /**
     * SQL data type from {@link java.sql.Types}.
     */
    @ColumnLabel("DATA_TYPE")
    @SuppressionPath("typeInfo/dataType")
    @XmlElement(required = true)
    int dataType;


    /**
     * maximum precision.
     */
    @ColumnLabel("PRECISION")
    @SuppressionPath("typeInfo/precision")
    @XmlElement(required = true)
    int precision;


    /**
     * prefix used to quote a literal (may be {@code null}).
     */
    @ColumnLabel("LITERAL_PREFIX")
    @SuppressionPath("typeInfo/literalPrefix")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String literalPrefix;


    /**
     * suffix used to quote a literal (may be {@code null}).
     */
    @ColumnLabel("LITERAL_SUFFIX")
    @SuppressionPath("typeInfo/literalSuffix")
    @XmlElement(nillable = true, required = true)
    String literalSuffix;


    /**
     * parameters used in creating the type (may be {@code null}).
     */
    @ColumnLabel("CREATE_PARAMS")
    @SuppressionPath("typeInfo/createParams")
    @XmlElement(nillable = true, required = true)
    String createParams;


    /**
     * can you use NULL for this type.
     * <ul>
     * <li>{@link DatabaseMetaData#typeNoNulls} - does not allow NULL
     * values</li>
     * <li>{@link DatabaseMetaData#typeNullable} - allows NULL values</li>
     * <li>{@link DatabaseMetaData#typeNullableUnknown} - nullability
     * unknown</li>
     * </ul>
     */
    @ColumnLabel("NULLABLE")
    @SuppressionPath("typeInfo/nullable")
    @XmlElement(required = true)
    short nullable;


    /**
     * is it case sensitive..
     */
    @ColumnLabel("CASE_SENSITIVE")
    @SuppressionPath("typeInfo/caseSensitive")
    @XmlElement(required = true)
    boolean caseSensitive;


    /**
     * can you use "WHERE" based on this type:
     * <ul>
     * <li>{@link DatabaseMetaData#typePredNone} - No support</li>
     * <li>{@link DatabaseMetaData#typePredChar} - Only supported with WHERE ..
     * LIKE</li>
     * <li>{@link DatabaseMetaData#typePredBasic} - Supported except for WHERE
     * .. LIKE</li>
     * <li>{@link DatabaseMetaData#typeSearchable} - Supported for all WHERE
     * ..</li>
     * </ul>
     */
    @ColumnLabel("SEARCHABLE")
    @SuppressionPath("typeInfo/searchable")
    @XmlElement(required = true)
    short searchable;


    /**
     * is it unsigned.
     */
    @ColumnLabel("UNSIGNED_ATTRIBUTE")
    @SuppressionPath("typeInfo/unsignedAttribute")
    @XmlElement(required = true)
    boolean unsignedAttribute;


    /**
     * can it be a money value.
     */
    @ColumnLabel("FIXED_PREC_SCALE")
    @SuppressionPath("typeInfo/fixedPrecScale")
    @XmlElement(required = true)
    boolean fixedPrecScale;


    /**
     * can it be used for an auto-increment value.
     */
    @ColumnLabel("AUTO_INCREMENT")
    @SuppressionPath("typeInfo/autoIncrement")
    @XmlElement(required = true)
    boolean autoIncrement;


    /**
     * localized version of type name (may be {@code null}).
     */
    @ColumnLabel("LOCAL_TYPE_NAME")
    @SuppressionPath("typeInfo/localTypeName")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    String localTypeName;


    /**
     * minimum scale supported.
     */
    @ColumnLabel("MINIMUM_SCALE")
    @SuppressionPath("typeInfo/minimumScale")
    @XmlElement(required = true)
    short minimumScale;


    /**
     * maximum scale supported.
     */
    @ColumnLabel("MAXIMUM_SCALE")
    @SuppressionPath("typeInfo/maximumScale")
    @XmlElement(required = true)
    short maximumScale;


    /**
     * unused.
     */
    @ColumnLabel("SQL_DATA_TYPE")
    @SuppressionPath("typeInfo/sqlDataType")
    @NotUsed
    @XmlElement(required = true)
    int sqlDataType;


    /**
     * unused.
     */
    @ColumnLabel("SQL_DATA_TYPE")
    @SuppressionPath("typeInfo/sqlDatetimeSub")
    @NotUsed
    @XmlElement(required = true)
    int sqlDatetimeSub;


    /**
     * usually 2 or 10.
     */
    @ColumnLabel("NUM_PREC_RADIX")
    @SuppressionPath("typeInfo/numPrecRadix")
    @XmlElement(required = true)
    int numPrecRadix;


}

