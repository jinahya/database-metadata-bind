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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.CompareToBuilder;

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
public class Function extends AbstractChild<Schema> {

    public static Comparator<Function> natural() {
        return new Comparator<Function>() {
            @Override
            public int compare(Function o1, Function o2) {
                // by FUNCTION_CAT, FUNCTION_SCHEM, FUNCTION_NAME
                // and SPECIFIC_NAME.
                return new CompareToBuilder()
                        .append(o1.getFunctionCat(), o2.getFunctionCat())
                        .append(o1.getFunctionSchem(), o2.getFunctionSchem())
                        .append(o1.getFunctionName(), o2.getFunctionName())
                        .append(o1.getSpecificName(), o2.getSpecificName())
                        .build();
            }
        };
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + "functionCat=" + functionCat
               + ", functionSchem=" + functionSchem
               + ", functionName=" + functionName
               + ", remarks=" + remarks
               + ", functionType=" + functionType
               + ", specificName=" + specificName
               + "}";
    }

    // ------------------------------------------------------------- functionCat
    public String getFunctionCat() {
        return functionCat;
    }

    public void setFunctionCat(final String functionCat) {
        this.functionCat = functionCat;
    }

    // ----------------------------------------------------------- functionSchem
    public String getFunctionSchem() {
        return functionSchem;
    }

    public void setFunctionSchem(final String functionSchem) {
        this.functionSchem = functionSchem;
    }

    // ------------------------------------------------------------ functionName
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    // ----------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // ------------------------------------------------------------ functionType
    public short getFunctionType() {
        return functionType;
    }

    public void setFunctionType(short functionType) {
        this.functionType = functionType;
    }

    // ------------------------------------------------------------ specificName
    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(final String specificName) {
        this.specificName = specificName;
    }

    // ------------------------------------------------------------------ schema
    public Schema getSchema() {
        return getParent();
    }

    public void setSchema(final Schema schema) {
        setParent(schema);
    }

    // --------------------------------------------------------- functionColumns
    public List<FunctionColumn> getFunctionColumns() {
        if (functionColumns == null) {
            functionColumns = new ArrayList<FunctionColumn>();
        }
        return functionColumns;
    }

    public void setFunctionColumns(List<FunctionColumn> functionColumns) {
        this.functionColumns = functionColumns;
    }

    // -------------------------------------------------------------------------
    @_Label("FUNCTION_CAT")
    @_NillableBySpecification
    @XmlAttribute
    private String functionCat;

    @_Label("FUNCTION_SCHEM")
    @_NillableBySpecification
    @XmlAttribute
    private String functionSchem;

    @_Label("FUNCTION_NAME")
    @XmlElement(required = true)
    private String functionName;

    @_Label("REMARKS")
    @XmlElement(required = true)
    private String remarks;

    @_Label("FUNCTION_TYPE")
    @XmlElement(required = true)
    private short functionType;

    @_Label("SPECIFIC_NAME")
    @XmlElement(required = true)
    private String specificName;

    @_Invocation(
            name = "getFunctionColumns",
            types = {String.class, String.class, String.class, String.class},
            argsarr = {
                @_InvocationArgs({
            ":functionCat", ":functionSchem", ":functionName", "null"
        })})
    @XmlElementRef
    private List<FunctionColumn> functionColumns;
}
