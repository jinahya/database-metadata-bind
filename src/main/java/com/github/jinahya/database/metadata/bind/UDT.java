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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An entity class for user defined types.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 */
@XmlRootElement
public class UDT extends SchemaChild {

    private static final long serialVersionUID = 8665246093405057553L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public UDT() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "typeCat=" + typeCat
               + ",typeSchem=" + typeSchem
               + ",typeName=" + typeName
               + ",className=" + className
               + ",dataType=" + dataType
               + ",remarks=" + remarks
               + ",baseType=" + baseType
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final UDT that = (UDT) obj;
        return dataType == that.dataType
               && Objects.equals(typeCat, that.typeCat)
               && Objects.equals(typeSchem, that.typeSchem)
               && Objects.equals(typeName, that.typeName)
               && Objects.equals(className, that.className)
               && Objects.equals(remarks, that.remarks)
               && Objects.equals(baseType, that.baseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCat,
                            typeSchem,
                            typeName,
                            className,
                            dataType,
                            remarks,
                            baseType);
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

    // ------------------------------------------------------------------------------------------------------- className
    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    // -------------------------------------------------------------------------------------------------------- dataType
    public int getDataType() {
        return dataType;
    }

    public void setDataType(final int dataType) {
        this.dataType = dataType;
    }

    // --------------------------------------------------------------------------------------------------------- remarks
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    // -------------------------------------------------------------------------------------------------------- baseType
    public Short getBaseType() {
        return baseType;
    }

    public void setBaseType(final Short baseType) {
        this.baseType = baseType;
    }

    // ------------------------------------------------------------------------------------------------------ attributes
    List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    // ------------------------------------------------------------------------------------------------------ superTypes
    List<SuperType> getSuperTypes() {
        if (superTypes == null) {
            superTypes = new ArrayList<>();
        }
        return superTypes;
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
    @Bind(label = "TYPE_NAME")
    private String typeName;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(required = true)
    @Label("CLASS_NAME")
    private String className;

    @XmlElement(required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(required = true)
    @Label("REMARKS")
    private String remarks;

    @XmlElement(required = true, nillable = true)
    @MayBeNull
    @Label("BASE_TYPE")
    private Short baseType;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    private List<@Valid @NotNull Attribute> attributes;

    @XmlElementRef
    private List<@Valid @NotNull SuperType> superTypes;
}
