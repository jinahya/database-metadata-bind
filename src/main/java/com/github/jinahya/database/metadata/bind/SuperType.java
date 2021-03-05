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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * An entity class for binding the result of {@link java.sql.DatabaseMetaData#getSuperTypes(java.lang.String,
 * java.lang.String, java.lang.String)}.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class SuperType extends UDTChild {

    private static final long serialVersionUID = 4603878785941565029L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public SuperType() {
        super();
    }

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final SuperType that = (SuperType) obj;
        return Objects.equals(typeCat, that.typeCat)
               && Objects.equals(typeSchem, that.typeSchem)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(supertypeCat, that.supertypeCat)
               && Objects.equals(supertypeSchem, that.supertypeSchem)
               && Objects.equals(supertypeName, that.supertypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCat,
                            typeSchem,
                            typeName,
                            supertypeCat,
                            supertypeSchem,
                            supertypeName);
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
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(required = true)
    @Label("TYPE_NAME")
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SUPERTYPE_CAT")
    private String supertypeCat;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("SUPERTYPE_SCHEM")
    private String supertypeSchem;

    @XmlElement(required = true)
    @Label("SUPERTYPE_NAME")
    private String supertypeName;
}
