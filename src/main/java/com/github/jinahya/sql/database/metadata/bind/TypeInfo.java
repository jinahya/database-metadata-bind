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
package com.github.jinahya.sql.database.metadata.bind;

import java.io.Serializable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getTypeInfo() getTypeInfo()}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "typeName", "dataType", "precision", "literalPrefix", "literalSuffix",
    "createParams", "nullable", "caseSensitive", "searchable",
    "unsignedAttribute", "fixedPrecScale", "autoIncrement", "localTypeName",
    "minimumScale", "maximumScale", "sqlDataType", "sqlDatetimeSub",
    "numPrecRadix"
})
public class TypeInfo implements Serializable {

    private static final long serialVersionUID = -3964147654019495313L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(TypeInfo.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "typeName=" + typeName
               + ",dataType=" + dataType
               + ",precision=" + precision
               + ",literalPrefix=" + literalPrefix
               + ",literalSuffix=" + literalSuffix
               + ",createParams=" + createParams
               + ",nullable=" + nullable
               + ",caseSensitive=" + caseSensitive
               + ",searchable=" + searchable
               + ",unsignedAttribute=" + unsignedAttribute
               + ",fixedPrecScale=" + fixedPrecScale
               + ",autoIncrement=" + autoIncrement
               + ",localTypeName=" + localTypeName
               + ",minimumScale=" + minimumScale
               + ",maximumScale=" + maximumScale
               + ",sqlDataType=" + sqlDataType
               + ",sqlDatetimeSub=" + sqlDatetimeSub
               + ",numPrecRadix=" + numPrecRadix
               + "}";
    }

    // ---------------------------------------------------------------- typeName
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(final String typeName) {
//        this.typeName = typeName;
//    }

    // ------------------------------------------------------------- getDataType
//    public int getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(final int dataType) {
//        this.dataType = dataType;
//    }

    // --------------------------------------------------------------- precesion
//    public int getPrecision() {
//        return precision;
//    }
//
//    public void setPrecision(final int precision) {
//        this.precision = precision;
//    }

    // ----------------------------------------------------------- literalPrefix
//    public String getLiteralPrefix() {
//        return literalPrefix;
//    }
//
//    public void setLiteralPrefix(final String literalPrefix) {
//        this.literalPrefix = literalPrefix;
//    }

    // ----------------------------------------------------------- literalSuffix
//    public String getLiteralSuffix() {
//        return literalSuffix;
//    }
//
//    public void setLiteralSuffix(final String literalSuffix) {
//        this.literalSuffix = literalSuffix;
//    }

    // ------------------------------------------------------------ createParams
//    public String getCreateParams() {
//        return createParams;
//    }
//
//    public void setCreateParams(final String createParams) {
//        this.createParams = createParams;
//    }

    // ---------------------------------------------------------------- nullable
//    public short getNullable() {
//        return nullable;
//    }
//
//    public void setNullable(final short nullable) {
//        this.nullable = nullable;
//    }

    // ----------------------------------------------------------- caseSensitive
//    public boolean getCaseSensitive() {
//        return caseSensitive;
//    }
//
//    public void setCaseSensitive(final boolean caseSensitive) {
//        this.caseSensitive = caseSensitive;
//    }

    // -------------------------------------------------------------- searchable
//    public short getSearchable() {
//        return searchable;
//    }
//
//    public void setSearchable(final short searchable) {
//        this.searchable = searchable;
//    }

    // ------------------------------------------------------- unsignedAttribute
//    public boolean getUnsignedAttribute() {
//        return unsignedAttribute;
//    }
//
//    public void setUnsignedAttribute(final boolean unsignedAttribute) {
//        this.unsignedAttribute = unsignedAttribute;
//    }

    // ---------------------------------------------------------- fixedPrecScale
//    public boolean getFixedPrecScale() {
//        return fixedPrecScale;
//    }
//
//    public void setFixedPrecScale(final boolean fixedPrecScale) {
//        this.fixedPrecScale = fixedPrecScale;
//    }

    // ----------------------------------------------------------- autoIncrement
//    public boolean getAutoIncrement() {
//        return autoIncrement;
//    }
//
//    public void setAutoIncrement(final boolean autoIncrement) {
//        this.autoIncrement = autoIncrement;
//    }

    // ----------------------------------------------------------- localTypeName
//    public String getLocalTypeName() {
//        return localTypeName;
//    }
//
//    public void setLocalTypeName(final String localTypeName) {
//        this.localTypeName = localTypeName;
//    }

    // ------------------------------------------------------------ minimumScale
//    public short getMinimumScale() {
//        return minimumScale;
//    }
//
//    public void setMinimumScale(final short minimumScale) {
//        this.minimumScale = minimumScale;
//    }

    // ------------------------------------------------------------ maximumScale
//    public short getMaximumScale() {
//        return maximumScale;
//    }
//
//    public void setMaximumScale(final short maximumScale) {
//        this.maximumScale = maximumScale;
//    }

    // ------------------------------------------------------------- sqlDataType
//    public Integer getSqlDataType() {
//        return sqlDataType;
//    }
//
//    public void setSqlDataType(final Integer sqlDataType) {
//        this.sqlDataType = sqlDataType;
//    }

    // ---------------------------------------------------------- sqlDatetimeSub
//    public Integer getSqlDatetimeSub() {
//        return sqlDatetimeSub;
//    }
//
//    public void setSqlDatetimeSub(final Integer sqlDatetimeSub) {
//        this.sqlDatetimeSub = sqlDatetimeSub;
//    }

    // ------------------------------------------------------------ numPrecRadix
//    public int getNumPrecRadix() {
//        return numPrecRadix;
//    }
//
//    public void setNumPrecRadix(final int numPrecRadix) {
//        this.numPrecRadix = numPrecRadix;
//    }

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    @Labeled("TYPE_NAME")
    @Getter
    @Setter
    private String typeName;

    @XmlElement(required = true)
    @Labeled("DATA_TYPE")
    @Getter
    @Setter
    private int dataType;

    @XmlElement(required = true)
    @Labeled("PRECISION")
    @Getter
    @Setter
    private int precision;

    @XmlElement(nillable = true, required = true)
    @Labeled("LITERAL_PREFIX")
    @Nillable
    @Getter
    @Setter
    private String literalPrefix;

    @XmlElement(nillable = true, required = true)
    @Labeled("LITERAL_SUFFIX")
    @Nillable
    @Getter
    @Setter
    private String literalSuffix;

    @XmlElement(nillable = true, required = true)
    @Labeled("CREATE_PARAMS")
    @Nillable
    @Getter
    @Setter
    private String createParams;

    @XmlElement(required = true)
    @Labeled("NULLABLE")
    @Getter
    @Setter
    private short nullable;

    @XmlElement(required = true)
    @Labeled("CASE_SENSITIVE")
    @Getter
    @Setter
    private boolean caseSensitive;

    @XmlElement(required = true)
    @Labeled("SEARCHABLE")
    @Getter
    @Setter
    private short searchable;

    @XmlElement(required = true)
    @Labeled("UNSIGNED_ATTRIBUTE")
    @Getter
    @Setter
    private boolean unsignedAttribute;

    @XmlElement(required = true)
    @Labeled("FIXED_PREC_SCALE")
    @Getter
    @Setter
    private boolean fixedPrecScale;

    @XmlElement(required = true)
    @Labeled("AUTO_INCREMENT")
    @Getter
    @Setter
    private boolean autoIncrement;

    @XmlElement(nillable = true, required = true)
    @Labeled("LOCAL_TYPE_NAME")
    @Nillable
    @Getter
    @Setter
    private String localTypeName;

    @XmlElement(required = true)
    @Labeled("MINIMUM_SCALE")
    @Getter
    @Setter
    private short minimumScale;

    @XmlElement(required = true)
    @Labeled("MAXIMUM_SCALE")
    @Getter
    @Setter
    private short maximumScale;

    @XmlElement(nillable = true, required = true)
    @Labeled("SQL_DATA_TYPE")
    @Unused
    @Getter
    @Setter
    private Integer sqlDataType;

    @XmlElement(nillable = true, required = true)
    @Labeled("SQL_DATETIME_SUB")
    @Unused
    @Getter
    @Setter
    private Integer sqlDatetimeSub;

    @XmlElement(required = true)
    @Labeled("NUM_PREC_RADIX")
    @Getter
    @Setter
    private int numPrecRadix;
}
