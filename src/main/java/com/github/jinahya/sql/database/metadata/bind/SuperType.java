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
import javax.xml.bind.annotation.XmlType;

/**
 * An entity class for binding the result of
 * {@link java.sql.DatabaseMetaData#getSuperTypes(java.lang.String, java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
@XmlType(propOrder = {
    "typeName", "supertypeCat", "supertypeSchem", "supertypeName"
})
public class SuperType {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",supertypeCat=" + supertypeCat
               + ",supertypeSchem=" + supertypeSchem
               + ",supertypeName=" + supertypeName
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

    // ------------------------------------------------------------ supertypeCat
    public String getSupertypeCat() {
        return supertypeCat;
    }

    public void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
    }

    // ---------------------------------------------------------- supertypeSchem
    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    public void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
    }

    // ----------------------------------------------------------- supertypeName
    public String getSupertypeName() {
        return supertypeName;
    }

    public void setSupertypeName(final String supertypeName) {
        this.supertypeName = supertypeName;
    }

    // -------------------------------------------------------------------------
    @Label("TYPE_CAT")
    @NillableBySpecification
    @XmlAttribute
    private String typeCat;

    @Label("TYPE_SCHEM")
    @NillableBySpecification
    @XmlAttribute
    private String typeSchem;

    @Label("TYPE_NAME")
    @XmlElement(required = true)
    private String typeName;

    @Label("SUPERTYPE_CAT")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String supertypeCat;

    @Label("SUPERTYPE_SCHEM")
    @NillableBySpecification
    @XmlElement(nillable = true, required = true)
    private String supertypeSchem;

    @Label("SUPERTYPE_NAME")
    @XmlElement(required = true)
    private String supertypeName;

}
