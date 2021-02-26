package com.github.jinahya.database.metadata.bind;

/*-
 * #%L
 * database-metadata-bind
 * %%
 * Copyright (C) 2011 - 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getSuperTypes(java.lang.String,
 * java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class SuperType extends AbstractChild<UDT> {

    private static final long serialVersionUID = 4603878785941565029L;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",supertypeCat=" + supertypeCat
               + ",supertypeSchem=" + supertypeSchem
               + ",supertypeName=" + supertypeName
               + '}';
    }

    // ------------------------------------------------------------------------------------------------------------- UDT
    @XmlTransient
    UDT getUDT_() {
        return getParent_();
    }

    void setUDT_(final UDT udt) {
        setParent_(udt);
    }

    // --------------------------------------------------------------------------------------------------------- typeCat
    public String getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(final String typeCat) {
        this.typeCat = typeCat;
    }

    // ------------------------------------------------------------------------------------------------------- typeSchem
    public String getTypeSchem() {
        return typeSchem;
    }

    public void setTypeSchem(final String typeSchem) {
        this.typeSchem = typeSchem;
    }

    // -------------------------------------------------------------------------------------------------------- typeName
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    // ---------------------------------------------------------------------------------------------------- supertypeCat
    public String getSupertypeCat() {
        return supertypeCat;
    }

    public void setSupertypeCat(final String supertypeCat) {
        this.supertypeCat = supertypeCat;
    }

    // -------------------------------------------------------------------------------------------------- supertypeSchem
    public String getSupertypeSchem() {
        return supertypeSchem;
    }

    public void setSupertypeSchem(final String supertypeSchem) {
        this.supertypeSchem = supertypeSchem;
    }

    // --------------------------------------------------------------------------------------------------- supertypeName
    public String getSupertypeName() {
        return supertypeName;
    }

    public void setSupertypeName(final String supertypeName) {
        this.supertypeName = supertypeName;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlAttribute
    @MayBeNull
    @Label("TYPE_CAT")
    @Bind(label = "TYPE_CAT", nillable = true)
    private String typeCat;

    @XmlAttribute
    @MayBeNull
    @Label("TYPE_SCHEM")
    @Bind(label = "TYPE_SCHEM", nillable = true)
    private String typeSchem;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement
    @Label("TYPE_NAME")
    @Bind(label = "TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("SUPERTYPE_CAT")
    @Bind(label = "SUPERTYPE_CAT", nillable = true)
    private String supertypeCat;

    @XmlElement(nillable = true)
    @MayBeNull
    @Label("SUPERTYPE_SCHEM")
    @Bind(label = "SUPERTYPE_SCHEM", nillable = true)
    private String supertypeSchem;

    @XmlElement
    @Label("SUPERTYPE_NAME")
    @Bind(label = "SUPERTYPE_NAME")
    private String supertypeName;
}
