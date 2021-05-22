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

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A class for binding results of {@link DatabaseMetaData#getUDTs(String, String, String, int[])} method.
 *
 * @author Jin Kwon &lt;jinahya_at_gmail.com&gt;
 * @see Context#getUDTs(String, String, String, int[], Collection)
 */
@XmlRootElement
@Data
@NoArgsConstructor
public class UDT
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = 8665246093405057553L;

    // ------------------------------------------------------------------------------------------------------ attributes
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    // ------------------------------------------------------------------------------------------------------ superTypes
    public List<SuperType> getSuperTypes() {
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
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Attribute> attributes;

    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull SuperType> superTypes;
}
