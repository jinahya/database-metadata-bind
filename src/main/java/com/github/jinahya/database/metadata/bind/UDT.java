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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for user defined types.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "typeName", "className", "dataType", "remarks", "baseType",
    // ---------------------------------------------------------------------
    "attributes", "superTypes"
})
public class UDT implements Serializable {

    private static final long serialVersionUID = 8665246093405057553L;

    // -------------------------------------------------------------------------
    private static final Logger logger = getLogger(UDT.class.getName());

    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",className=" + className
               + ",dataType=" + dataType
               + ",remarks=" + remarks
               + ",baseType=" + baseType
               + "}";
    }

    // ----------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // --------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
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

    // -------------------------------------------------------------- attributes
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        return attributes;
    }

    // -------------------------------------------------------------- superTypes
    public List<SuperType> getSuperTypes() {
        if (superTypes == null) {
            superTypes = new ArrayList<SuperType>();
        }
        return superTypes;
    }

    // -------------------------------------------------------------------------
    @XmlAttribute
    @Label("TYPE_CAT")
    @Bind(label = "TYPE_CAT", nillable = true)
    @Nillable
    private String typeCat;

    @XmlAttribute
    @Nillable
    @Label("TYPE_SCHEM")
    @Bind(label = "TYPE_SCHEM", nillable = true)
    private String typeSchem;

    // -------------------------------------------------------------------------
    @XmlElement
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement
    @Label("CLASS_NAME")
    @Bind(label = "CLASS_NAME")
    private String className;

    @XmlElement
    @Label("DATA_TYPE")
    @Bind(label = "DATA_TYPE")
    private int dataType;

    @XmlElement
    @Label("REMARKS")
    @Bind(label = "REMARKS")
    private String remarks;

    @XmlElement(nillable = true)
    @Label("BASE_TYPE")
    @Bind(label = "BASE_TYPE", nillable = true)
    @Nillable
    private Short baseType;

    @XmlElementRef
    @Invoke(name = "getAttributes",
            types = {String.class, String.class, String.class, String.class},
            parameters = {
                @Literals({":typeCat", ":typeSchem", ":typeName", "null"})
            }
    )
    private List<Attribute> attributes;

    @XmlElementRef
    @Invoke(name = "getSuperTypes",
            types = {String.class, String.class, String.class},
            parameters = {
                @Literals({":typeCat", ":typeSchem", ":typeName"})
            }
    )
    private List<SuperType> superTypes;
}
