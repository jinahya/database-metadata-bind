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


package com.github.jinahya.sql.database.meta.data.bind;


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
        "minimumScale", "maximumScale", "numPrecRadix"
    }
)
public class TypeInfo {


    /**
     *
     * @param database
     * @param suppression
     * @param typeInfo
     *
     * @throws SQLException
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

        for (final TypeInfo typeInfo : metadata.getTypeInfo()) {
            typeInfo.setMetadata(metadata);
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


    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
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


    // -------------------------------------------------------- unsignedAttribute
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


    @ColumnLabel("TYPE_NAME")
    @SuppressionPath("typeInfo/typeName")
    @XmlElement(required = true)
    String typeName;


    @ColumnLabel("DATA_TYPE")
    @SuppressionPath("typeInfo/dataType")
    @XmlElement(required = true)
    int dataType;


    @ColumnLabel("PRECISION")
    @SuppressionPath("typeInfo/precision")
    @XmlElement(required = true)
    int precision;


    @ColumnLabel("LITERAL_PREFIX")
    @SuppressionPath("typeInfo/literalPrefix")
    @XmlElement(nillable = true, required = true)
    String literalPrefix;


    @ColumnLabel("LITERAL_SUFFIX")
    @SuppressionPath("typeInfo/literalSuffix")
    @XmlElement(nillable = true, required = true)
    String literalSuffix;


    @ColumnLabel("CREATE_PARAMS")
    @SuppressionPath("typeInfo/createParams")
    @XmlElement(nillable = true, required = true)
    String createParams;


    @ColumnLabel("NULLABLE")
    @SuppressionPath("typeInfo/nullable")
    @XmlElement(required = true)
    short nullable;


    @ColumnLabel("CASE_SENSITIVE")
    @SuppressionPath("typeInfo/caseSensitive")
    @XmlElement(required = true)
    boolean caseSensitive;


    @ColumnLabel("SEARCHABLE")
    @SuppressionPath("typeInfo/searchable")
    @XmlElement(required = true)
    short searchable;


    @ColumnLabel("UNSIGNED_ATTRIBUTE")
    @SuppressionPath("typeInfo/unsignedAttribute")
    @XmlElement(required = true)
    boolean unsignedAttribute;


    @ColumnLabel("FIXED_PREC_SCALE")
    @SuppressionPath("typeInfo/fixedPrecScale")
    @XmlElement(required = true)
    boolean fixedPrecScale;


    @ColumnLabel("AUTO_INCREMENT")
    @SuppressionPath("typeInfo/autoIncrement")
    @XmlElement(required = true)
    boolean autoIncrement;


    @ColumnLabel("LOCAL_TYPE_NAME")
    @SuppressionPath("typeInfo/localTypeName")
    @XmlElement(nillable = true, required = true)
    String localTypeName;


    @ColumnLabel("MINIMUM_SCALE")
    @SuppressionPath("typeInfo/minimumScale")
    @XmlElement(required = true)
    short minimumScale;


    @ColumnLabel("MAXIMUM_SCALE")
    @SuppressionPath("typeInfo/maximumScale")
    @XmlElement(required = true)
    short maximumScale;


    @ColumnLabel("NUM_PREC_RADIX")
    @SuppressionPath("typeInfo/numPrecRadix")
    @XmlElement(required = true)
    int numPrecRadix;


}

