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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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
@ChildOf__(Schema.class)
@ParentOf(Attribute.class)
@Data
@NoArgsConstructor
@SuperBuilder
public class UDT
        implements MetadataType,
                   ChildOf<Schema> {

    private static final long serialVersionUID = 8665246093405057553L;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public void retrieveChildren(final Context context) throws SQLException {
        {
            context.getAttributes(getTypeCat(), getTypeSchem(), getTypeName(), "%", getAttributes());
            for (final Attribute attribute : getAttributes()) {
                attribute.retrieveChildren(context);
            }
        }
    }

    @Override
    public Schema extractParent() {
        return Schema.builder()
                .tableCatalog(getTypeCat())
                .tableSchem(getTypeSchem())
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_CAT")
    private String typeCat;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("TYPE_SCHEM")
    private String typeSchem;

    @XmlElement(nillable = false, required = true)
    @NotBlank
    @Label("TYPE_NAME")
    private String typeName;

    @XmlElement(nillable = false, required = true)
    @Label("CLASS_NAME")
    private String className;

    @XmlElement(nillable = false, required = true)
    @Label("DATA_TYPE")
    private int dataType;

    @XmlElement(nillable = false, required = true)
    @Label("REMARKS")
    private String remarks;

    @XmlElement(nillable = true, required = true)
    @NullableBySpecification
    @Label("BASE_TYPE")
    private Integer baseType;

    // -----------------------------------------------------------------------------------------------------------------
    @XmlElementRef
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<@Valid @NotNull Attribute> attributes;
}
