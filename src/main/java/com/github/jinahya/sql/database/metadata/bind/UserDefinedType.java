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
        "typeName", "className", "dataType", "remarks", "baseType", "attributes"
    }
)
public class UserDefinedType {


    // ----------------------------------------------------------------- typeCat
    public String getTypeCat() {

        return typeCat;
    }


    // --------------------------------------------------------------- typeSchem
    public String getTypeSchem() {

        return typeSchem;
    }


    // ---------------------------------------------------------------- typeName
    public String getTypeName() {

        return typeName;
    }


    public void setTypeName(final String typeName) {

        this.typeName = typeName;
    }


    // --------------------------------------------------------------- className
    public String getClassName() {

        return className;
    }


    public void setClassName(final String className) {

        this.className = className;
    }


    // ---------------------------------------------------------------- dataType
    public int getDataType() {

        return dataType;
    }


    public void setDataType(final int dataType) {

        this.dataType = dataType;
    }


    // ----------------------------------------------------------------- remarks
    public String getRemarks() {

        return remarks;
    }


    public void setRemarks(final String remarks) {

        this.remarks = remarks;
    }


    // ---------------------------------------------------------------- baseType
    public Short getBaseType() {

        return baseType;
    }


    public void setBaseType(final Short baseType) {

        this.baseType = baseType;
    }


    // ------------------------------------------------------------------ schema
    public Schema getSchema() {

        return schema;
    }


    public void setSchema(final Schema schema) {

        this.schema = schema;
    }


    // -------------------------------------------------------------- attributes
    public List<Attribute> getAttributes() {

        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }

        return attributes;
    }


    @ColumnLabel("TYPE_CAT")
    @XmlAttribute
    private String typeCat;


    @ColumnLabel("TYPE_SCHEM")
    @XmlAttribute
    private String typeSchem;


    @ColumnLabel("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;


    @ColumnLabel("CLASS_NAME")
    @XmlElement(required = true)
    private String className;


    @ColumnLabel("DATA_TYPE")
    @XmlElement(required = true)
    private int dataType;


    @ColumnLabel("REMARKS")
    @XmlElement(required = true)
    private String remarks;


    @ColumnLabel("BASE_TYPE")
    @XmlElement(nillable = true, required = true)
    @NillableBySpecification
    private Short baseType;


    @XmlTransient
    private Schema schema;


    @MethodInvocation(name = "getAttributes",
                    types = {String.class, String.class, String.class, String.class},
                    args = {@MethodInvocationArgs({":tableCat", ":tableSchem", ":typeName", "null"})})
    @XmlElementRef
    private List<Attribute> attributes;


}

