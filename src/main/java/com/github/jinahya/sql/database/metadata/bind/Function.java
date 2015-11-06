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
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(
    propOrder = {
        "functionName", "remarks", "functionType", "specificName",
        // ---------------------------------------------------------------------
        "functionColumns"

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


    // --------------------------------------------------------- functionColumns
    public List<FunctionColumn> getFunctionColumns() {

        if (functionColumns == null) {
            functionColumns = new ArrayList<FunctionColumn>();
        }

        return functionColumns;
    }


    @Label("FUNCTION_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String functionCat;


    @Label("FUNCTION_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String functionSchem;


    @Label("FUNCTION_NAME")
    @XmlElement(required = true)
    private String functionName;


    @Label("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @Label("FUNCTION_TYPE")
    @XmlElement(required = true)
    private short functionType;


    @Label("SPECIFIC_NAME")
    @XmlElement(required = true)
    private String specificName;


    @XmlTransient
    private Schema schema;


    @Invocation(
        name = "getFunctionColumns",
        types = {String.class, String.class, String.class, String.class},
        argsarr = {
            @InvocationArgs({
                ":functionCat", ":functionSchem", ":functionName", "null"
            })
        }
    )
    @XmlElementRef
    private List<FunctionColumn> functionColumns;


}

