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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.Setter;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getFunctions(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see java.sql.DatabaseMetaData#getFunctions(java.lang.String,
 * java.lang.String, java.lang.String)
 * @see MetadataContext#getFunctions(java.lang.String, java.lang.String,
 * java.lang.String)
 */
@XmlRootElement
@XmlType(propOrder = {
    "functionName", "remarks", "functionType", "specificName",
    // ---------------------------------------------------------------------
    "functionColumns"
})
public class Function implements Serializable {

    private static final long serialVersionUID = -3318947900237453301L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(Function.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "functionCat=" + functionCat
               + ",functionSchem=" + functionSchem
               + ",functionName=" + functionName
               + ",remarks=" + remarks
               + ",functionType=" + functionType
               + ",specificName=" + specificName
               + "}";
    }

    // ------------------------------------------------------------- functionCat
//    public String getFunctionCat() {
//        return functionCat;
//    }
//
//    public void setFunctionCat(final String functionCat) {
//        this.functionCat = functionCat;
//    }
    // ----------------------------------------------------------- functionSchem
//    public String getFunctionSchem() {
//        return functionSchem;
//    }
//
//    public void setFunctionSchem(final String functionSchem) {
//        this.functionSchem = functionSchem;
//    }
    // ------------------------------------------------------------ functionName
//    public String getFunctionName() {
//        return functionName;
//    }
//
//    public void setFunctionName(final String functionName) {
//        this.functionName = functionName;
//    }
    // ----------------------------------------------------------------- remarks
//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(final String remarks) {
//        this.remarks = remarks;
//    }
    // ------------------------------------------------------------ functionType
//    public short getFunctionType() {
//        return functionType;
//    }
//
//    public void setFunctionType(final short functionType) {
//        this.functionType = functionType;
//    }
    // ------------------------------------------------------------ specificName
//    public String getSpecificName() {
//        return specificName;
//    }
//
//    public void setSpecificName(final String specificName) {
//        this.specificName = specificName;
//    }
    // --------------------------------------------------------- functionColumns
    public List<FunctionColumn> getFunctionColumns() {
        if (functionColumns == null) {
            functionColumns = new ArrayList<FunctionColumn>();
        }
        return functionColumns;
    }

    @Deprecated
    public void setFunctionColumns(final List<FunctionColumn> functionColumns) {
        this.functionColumns = functionColumns;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Labeled("FUNCTION_CAT")
    @Nillable
    @Getter
    @Setter
    private String functionCat;

    @XmlAttribute
    @Labeled("FUNCTION_SCHEM")
    @Nillable
    @Getter
    @Setter
    private String functionSchem;

    // -------------------------------------------------------------------------
    @XmlElement(required = true)
    @Labeled("FUNCTION_NAME")
    @Getter
    @Setter
    private String functionName;

    @XmlElement(required = true)
    @Labeled("REMARKS")
    @Getter
    @Setter
    private String remarks;

    @XmlElement(required = true)
    @Labeled("FUNCTION_TYPE")
    @Getter
    @Setter
    private short functionType;

    @XmlElement(required = true)
    @Labeled("SPECIFIC_NAME")
    @Getter
    @Setter
    private String specificName;

    // -------------------------------------------------------------------------
    @XmlElementRef
    @Invokable(name = "getFunctionColumns",
               types = {String.class, String.class, String.class, String.class},
               args = {
                   @Literals({":functionCat", ":functionSchem", ":functionName",
                              "null"})
               }
    )
    private List<FunctionColumn> functionColumns;
}
