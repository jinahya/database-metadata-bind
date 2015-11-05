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


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon <onacit at gmail.com>
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "functionName", "remarks", "functionType", "specificName"
    }
)
public class Function {


    // ------------------------------------------------------------ functionName
    public String getFunctionName() {

        return functionName;
    }


    public void setFuntionName(final String functionName) {

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

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    @ColumnLabel("FUNCTION_CAT")
    @XmlAttribute(required = false)
    private String functionCat;


    @ColumnLabel("FUNCTION_SCHEM")
    @XmlAttribute(required = false)
    private String functionSchem;


    @ColumnLabel("FUNCTION_NAME")
    @XmlElement(required = true)
    private String functionName;


    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @ColumnLabel("FUNCTION_TYPE")
    @XmlElement(nillable = true, required = true)
    Short functionType;


    @ColumnLabel("SPECIFIC_NAME")
    @XmlElement(nillable = true, required = true)
    String specificName;


    @XmlTransient
    private Schema schema;


}

